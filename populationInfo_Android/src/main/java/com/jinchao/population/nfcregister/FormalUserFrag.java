package com.jinchao.population.nfcregister;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.jinchao.population.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FormalUserFrag extends Fragment {

    Button copyActiveCodeBtn;
    Button btn_replace;
    TextView startTimeTv;
    TextView endTimeTv;
    TextView activeCodeTv;

    public FormalUserFrag() {
        // Required empty public constructor
    }


    public static FormalUserFrag newInstance() {
        FormalUserFrag fragment = new FormalUserFrag();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_formal_user, container, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        startTimeTv = (TextView) view.findViewById(R.id.startTimeTV);
        endTimeTv = (TextView) view.findViewById(R.id.endTimeTV);
        activeCodeTv = (TextView) view.findViewById(R.id.activeCodeTV);
        btn_replace= (Button) view.findViewById(R.id.btn_replace);
        Bundle args = getArguments();
        ActiveInfo activeInfo = null;
        if (args != null) {
            activeInfo = args.getParcelable(ActiveInfo.class.getName());
        }
        if (activeInfo != null) {
            startTimeTv.setText(sdf.format(new Date(activeInfo.getStartTime())));
            endTimeTv.setText(sdf.format(new Date(activeInfo.getEndTime())));
            activeCodeTv.setText(activeInfo.getActCode());
        }

        copyActiveCodeBtn = (Button) view.findViewById(R.id.copyActiveCodeBtn);
        copyActiveCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String activeCode = activeCodeTv.getText().toString();
//                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("simple text", activeCode);
//                clipboardManager.setPrimaryClip(clip);
//                Toast.makeText(getActivity(), "激活码已复制", Toast.LENGTH_SHORT).show();
                  getActivity().onBackPressed();
            }
        });
        btn_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NFCActiveActivity)getActivity()).mainVP.setCurrentItem(NFCActiveViewPagerAdapter.ACTIVE_FRAG, false);
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

}
