package com.jinchao.population.nfcregister;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinchao.population.R;


public class FormalExpireFrag extends Fragment {


    public FormalExpireFrag() {
        // Required empty public constructor
    }


    public static FormalExpireFrag newInstance() {
        FormalExpireFrag fragment = new FormalExpireFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_formal_expire, container, false);
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


}
