package net.virifi.android.spmodesettings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;


public class MainActivity extends FragmentActivity implements
		ScrapingFragment.OnStateChangedListener,
		MainFragment.OnListItemClickListener,
		AddressChangeFragment.OnChangeAddressClickListener,
		PasswordChangeFragment.OnChangePasswordClickListener {
	
	ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager manager = getSupportFragmentManager();
		
		// 初めに表示されるFragment
		MainFragment mainFragment = new MainFragment();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(android.R.id.content, mainFragment);
		
		// スクレイピングをするためのView無しFragmentを登録
		ScrapingFragment scrapingFragment = new ScrapingFragment();
		transaction.add(scrapingFragment, "ScrapingFragment");
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private ScrapingFragment getScrapingFragment() {
		FragmentManager manager = getSupportFragmentManager();
		ScrapingFragment fragment = (ScrapingFragment) manager
				.findFragmentByTag("ScrapingFragment");
		return fragment;
	}
	
	private void prepareProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("処理中です");
		mProgressDialog.setOnCancelListener(new OnCancelListener() {		
			@Override
			public void onCancel(DialogInterface dialog) {
				getScrapingFragment().cancel();
			}
		});
		mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "キャンセル", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mProgressDialog.cancel();
			}
		});
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mProgressDialog.show();
	}

	// JavaScript内でjs_scraper.pagestepが呼び出された場合に呼び出される
	@Override
	public void onPageStepChanged(int maxStep, int currentStep) {
		mProgressDialog.setProgress((int)(((float) currentStep / maxStep) * 100));
	}

	// JavaScript内でjs_scraper.scraping_finishedが呼び出された場合に呼び出される
	@Override
	public void onScrapingFinished(String message, Bundle results) {
		mProgressDialog.dismiss();
		new AlertDialog.Builder(this)
			.setMessage(message)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.setTitle("成功")
			.show();
	}

	// JavaScript内でjs_scraper.scraping_errorが呼び出された場合に呼び出される
	@Override
	public void onScrapingError(String message) {
		mProgressDialog.dismiss();
		new AlertDialog.Builder(this)
		.setMessage(message)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setTitle("エラー")
		.show();
	}

	// MainFragmentのリストビューのアイテムがクリックされた場合に呼び出される
	@Override
	public void onItemClick(int fragmentId) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment fragment;
		if (fragmentId == MainFragment.ADDRESS_CHANGE_FRAGMENT) {
			fragment = new AddressChangeFragment();
		} else if (fragmentId == MainFragment.PASSWORD_CHANGE_FRAGMENT) { 
			fragment = new PasswordChangeFragment();
		} else {
			return;
		}
		transaction.replace(android.R.id.content, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	// AddressChangeFragmentで変更ボタンが押された場合に呼び出される
	@Override
	public void onChangeAddressClicked(String address, String password) {
		Bundle bundle = new Bundle();
		bundle.putString("address", address);
		bundle.putString("password", password);
		prepareProgressDialog();
		getScrapingFragment().startScraping("https://spmode.ne.jp/setting/", "change_address.js", bundle);
	}

	// PasswordChangeFragmentで変更ボタンが押された場合に呼び出される
	@Override
	public void onChangePasswordClicked(String prevPass, String newPass) {
		Bundle bundle = new Bundle();
		bundle.putString("prev_password", prevPass);
		bundle.putString("new_password", newPass);
		prepareProgressDialog();
		getScrapingFragment().startScraping("https://spmode.ne.jp/setting/", "change_password.js", bundle);
	}
}
