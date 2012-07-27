package net.virifi.android.spmodesettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ScrapingFragment extends Fragment {
	private static final String TAG = "ScrapingFragment";
	private boolean mJqueryLoaded = false;
	private boolean mJSScraperLoaded = false;
	private boolean mMainScriptLoaded = false;
	private Bundle mBundle;
	private Bundle mResultBundle;
	private String mScriptName;
	private boolean mCancel = false;
	
	WebView mWebView;
	
	OnStateChangedListener mListener;

	public interface OnStateChangedListener {
		public void onPageStepChanged(int maxSteps, int currentStep);

		public void onScrapingFinished(String message, Bundle results);

		public void onScrapingError(String message);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof OnStateChangedListener == false) {
			throw new ClassCastException(
					"activity does not implements OnStateChangedListener");
		}
		mListener = (OnStateChangedListener) activity;

		mWebView = new WebView(activity);
		mWebView.getSettings().setLoadsImagesAutomatically(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		// mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.0.1; ja-jp; Galaxy Nexus Build/123) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
		mWebView.addJavascriptInterface(new WebViewInterface(), "__webViewInterface");
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (mCancel) {
					mWebView.stopLoading();
					mListener.onScrapingError("キャンセルされました。");
					return;
				}
				if (!mJqueryLoaded) {				
					try {
						view.loadUrl(scriptToString("jquery-1.7.2.min.js"));
					} catch (IOException e) {
						e.printStackTrace();
						mListener.onScrapingError("実行に必要なスクリプトを読み込めませんでした。 : jquery-1.7.2.min.js");
					}
					mJqueryLoaded = true;
				}
				if (!mJSScraperLoaded) {
					try {
						view.loadUrl(scriptToString("js_scraper.js"));
					} catch (IOException e) {
						e.printStackTrace();
						mListener.onScrapingError("実行に必要なスクリプトを読み込めませんでした。 : js_scraper.js");
					}
				}
				if (!mMainScriptLoaded) {
					try {
						view.loadUrl(scriptToString(mScriptName));
					} catch (IOException e) {
						e.printStackTrace();
						mListener.onScrapingError("実行に必要なスクリプトを読み込めませんでした。 : " + mScriptName);
					}
					mMainScriptLoaded = true;
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
	}
	
	public void startScraping(String url, String scriptName, Bundle args) {
		if (args == null) {
			mBundle = new Bundle();
		} else {
			mBundle = args;
		}
		mScriptName = scriptName;
		mResultBundle = new Bundle();
		mCancel = false;
		resetScriptLoaded();
		mWebView.loadUrl(url);
	}

	private String scriptToString(String fileName) throws IOException {
		StringBuilder sb = new StringBuilder("javascript:");
		InputStream is = null;
		BufferedReader br = null;
		is = getActivity().getResources().getAssets().open(fileName);
		br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();
		is.close();

		return sb.toString();
	}
	
	public void cancel() {
		mCancel = true;
	}
	
	private void resetScriptLoaded() {
		mJqueryLoaded = false;
		mJSScraperLoaded = false;
		mMainScriptLoaded = false;
	}

	private class WebViewInterface {
		public String getBundleString(String key) {
			return mBundle.getString(key);
		}
		
		public void setResultString(String key, String value) {
			mResultBundle.putString(key, value);
		}

		public void prepare() {
			resetScriptLoaded();
		}
		
		public void onPageStepChanged(final int maxStep, final int currentStep) {
			getActivity().runOnUiThread(new Runnable() {	
				@Override
				public void run() {
					mListener.onPageStepChanged(maxStep, currentStep);
				}
			});
		}
		
		public void onScrapingError(final String message) {
			getActivity().runOnUiThread(new Runnable() {			
				@Override
				public void run() {					
					mListener.onScrapingError(message);
				}
			});
			prepare();
		}
		
		public void onScrapingFinished(final String message) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mListener.onScrapingFinished(message, mResultBundle);
				}
			});
			prepare();
		}

		public void log(final String message) {
			Log.d(TAG, message);
		}
	}
}
