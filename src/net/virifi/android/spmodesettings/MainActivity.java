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
		
		// ���߂ɕ\�������Fragment
		MainFragment mainFragment = new MainFragment();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(android.R.id.content, mainFragment);
		
		// �X�N���C�s���O�����邽�߂�View����Fragment��o�^
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
		mProgressDialog.setTitle("�������ł�");
		mProgressDialog.setOnCancelListener(new OnCancelListener() {		
			@Override
			public void onCancel(DialogInterface dialog) {
				getScrapingFragment().cancel();
			}
		});
		mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "�L�����Z��", new OnClickListener() {
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

	// JavaScript����js_scraper.pagestep���Ăяo���ꂽ�ꍇ�ɌĂяo�����
	@Override
	public void onPageStepChanged(int maxStep, int currentStep) {
		mProgressDialog.setProgress((int)(((float) currentStep / maxStep) * 100));
	}

	// JavaScript����js_scraper.scraping_finished���Ăяo���ꂽ�ꍇ�ɌĂяo�����
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
			.setTitle("����")
			.show();
	}

	// JavaScript����js_scraper.scraping_error���Ăяo���ꂽ�ꍇ�ɌĂяo�����
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
		.setTitle("�G���[")
		.show();
	}

	// MainFragment�̃��X�g�r���[�̃A�C�e�����N���b�N���ꂽ�ꍇ�ɌĂяo�����
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
	
	// AddressChangeFragment�ŕύX�{�^���������ꂽ�ꍇ�ɌĂяo�����
	@Override
	public void onChangeAddressClicked(String address, String password) {
		Bundle bundle = new Bundle();
		bundle.putString("address", address);
		bundle.putString("password", password);
		prepareProgressDialog();
		getScrapingFragment().startScraping("https://spmode.ne.jp/setting/", "change_address.js", bundle);
	}

	// PasswordChangeFragment�ŕύX�{�^���������ꂽ�ꍇ�ɌĂяo�����
	@Override
	public void onChangePasswordClicked(String prevPass, String newPass) {
		Bundle bundle = new Bundle();
		bundle.putString("prev_password", prevPass);
		bundle.putString("new_password", newPass);
		prepareProgressDialog();
		getScrapingFragment().startScraping("https://spmode.ne.jp/setting/", "change_password.js", bundle);
	}
}
