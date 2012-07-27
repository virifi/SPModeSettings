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

public class PasswordChangeFragment extends Fragment {
	
	public interface OnChangePasswordClickListener {
		public void onChangePasswordClicked(String prevPass, String newPass);
	}
	
	private OnChangePasswordClickListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnChangePasswordClickListener == false) {
			throw new ClassCastException("activity does not implement OnChangePasswordClickListener");
		}
		mListener = (OnChangePasswordClickListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.password_change_fragment, container, false);
		
		final EditText prevPassEditText = (EditText) v.findViewById(R.id.prev_password_edit_text);
		final EditText newPassEditText1 = (EditText) v.findViewById(R.id.new_password_edit_text1);
		final EditText newPassEditText2 = (EditText) v.findViewById(R.id.new_password_edit_text2);

		
		Button changeButton = (Button) v.findViewById(R.id.change_button);
		changeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String prevPass, newPass, newPass1, newPass2;
				
				prevPass = prevPassEditText.getText().toString();
				newPass1 = newPassEditText1.getText().toString();
				newPass2 = newPassEditText2.getText().toString();
				if (!newPass1.equals(newPass2)) {
					Toast.makeText(getActivity(), "êVÇµÇ¢à√èÿî‘çÜìØémÇ™àÍívÇµÇ‹ÇπÇÒ", Toast.LENGTH_SHORT).show();
					return;
				}
				if (prevPass.length() != 4 || newPass1.length() != 4 || newPass2.length() != 4) {
					Toast.makeText(getActivity(), "à√èÿî‘çÜÇÕ4åÖÇ≈Ç»ÇØÇÍÇŒÇ»ÇËÇ‹ÇπÇÒ", Toast.LENGTH_SHORT).show();
					return;
				}
				
				newPass = newPass2;
				
				mListener.onChangePasswordClicked(prevPass, newPass);
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
