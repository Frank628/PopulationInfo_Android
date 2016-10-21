package com.jinchao.population.nfcregister;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jinchao.population.R;


public class ActiveFrag extends Fragment {

	EditText activeCodeEt;
	Button activeBtn;
	ActiveFragListener listener;

	public ActiveFrag() {
	}

	public static ActiveFrag newInstance() {
		ActiveFrag fragment = new ActiveFrag();
		return fragment;
	}

	public void setListener(ActiveFragListener listener) {
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_active, container, false);
		activeCodeEt = (EditText) view.findViewById(R.id.activeCodeEt);
		activeBtn = (Button) view.findViewById(R.id.activeBtn);
		activeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String activeCode = activeCodeEt.getText().toString().trim();
				if (activeCode.equals("")) {
					Toast.makeText(getActivity(), "请输入激活码", Toast.LENGTH_SHORT).show();
				} else {
					if (listener != null)
						listener.onActiveButtonClicked(activeCode);
				}
			}
		});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	public interface ActiveFragListener {
		void onActiveButtonClicked(String activeCode);
	}
}
