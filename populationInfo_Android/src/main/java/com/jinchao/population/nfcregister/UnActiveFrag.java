package com.jinchao.population.nfcregister;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jinchao.population.R;


public class UnActiveFrag extends Fragment {

    UnActiveFragListener listener;
    Button activeFormalBtn;

    public UnActiveFrag() {
        // Required empty public constructor
    }

    public void setListener(UnActiveFragListener listener) {
        this.listener = listener;
    }

    public static UnActiveFrag newInstance() {
        UnActiveFrag fragment = new UnActiveFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_un_active, container, false);
        activeFormalBtn = (Button) view.findViewById(R.id.activeFormalBtn);
        
        activeFormalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onActiveFormalButtonClicked();
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

    public interface UnActiveFragListener {
        void onActiveFormalButtonClicked();
    }
}
