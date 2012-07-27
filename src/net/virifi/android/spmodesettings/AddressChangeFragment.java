package net.virifi.android.spmodesettings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddressChangeFragment extends Fragment {
	
	public interface OnChangeAddressClickListener {
		public void onChangeAddressClicked(String address, String password);
	}
	
	private OnChangeAddressClickListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnChangeAddressClickListener == false) {
			throw new ClassCastException("activity does not implement OnChangeAddressClickListener");
		}
		mListener = (OnChangeAddressClickListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.change_address_fragment, container, false);
		
		final EditText addressEditText = (EditText) v.findViewById(R.id.address_edit_text);
		final EditText passwordEditText = (EditText) v.findViewById(R.id.password_edit_text);
		
		Button changeButton = (Button) v.findViewById(R.id.change_button);
		changeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String address = addressEditText.getText().toString();
				if (address.length() < 3 || address.length() > 30) {
					Toast.makeText(getActivity(), "メールアドレスの長さは3文字以上30文字以内でなければなりません。", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!address.substring(0, 1).matches("[a-zA-Z]")) {
					Toast.makeText(getActivity(), "メールアドレスの先頭は英字でなければなりません。", Toast.LENGTH_SHORT).show();
					return;
				}
				String password = passwordEditText.getText().toString();
				if (password.length() != 4) {
					Toast.makeText(getActivity(), "暗証番号は4文字です。4文字入力してください。", Toast.LENGTH_SHORT).show();
					return;
				}
				mListener.onChangeAddressClicked(address, password);
			}
		});
		
		Button cancelButton = (Button) v.findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
		
		return v;
	}
}
