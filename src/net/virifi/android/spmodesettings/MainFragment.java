package net.virifi.android.spmodesettings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainFragment extends Fragment {
	public static final int ADDRESS_CHANGE_FRAGMENT = 0;
	public static final int PASSWORD_CHANGE_FRAGMENT = 1;
	
	public interface OnListItemClickListener {
		public void onItemClick(int fragmentId);
	}
	
	OnListItemClickListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_fragment, container, false);
		ListView listView = (ListView) v.findViewById(R.id.listView1);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        adapter.add("メールアドレスを変更する");
        adapter.add("暗証番号を変更する");
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	switch (position) {
            	case 0:
            		mListener.onItemClick(ADDRESS_CHANGE_FRAGMENT);
            		break;
            	case 1:
            		mListener.onItemClick(PASSWORD_CHANGE_FRAGMENT);
            		break;
            	}
            }
        });
        
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnListItemClickListener == false) {
			throw new ClassCastException("activity does not implement OnListItemClickListener");
		}
		mListener = (OnListItemClickListener) activity;
	}
}
