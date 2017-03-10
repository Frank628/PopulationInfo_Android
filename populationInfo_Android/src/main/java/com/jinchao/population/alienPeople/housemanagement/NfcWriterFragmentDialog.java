package com.jinchao.population.alienPeople.housemanagement;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseDialogFragment;
import com.jinchao.population.utils.nfcutil.NfcOperation;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by OfferJiShu01 on 2017/2/28.
 */
@ContentView(R.layout.dialog_nfc)
public class NfcWriterFragmentDialog extends BaseDialogFragment {
    @ViewInject(R.id.iv_reading)ImageView iv_reading;
    @ViewInject(R.id.tv_state)TextView tv_state;
    @ViewInject(R.id.tv_notice)TextView tv_notice;
    private String json="";
    public static NfcWriterFragmentDialog newInstance(String json){
        NfcWriterFragmentDialog nfcWriterFragmentDialog=new NfcWriterFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        nfcWriterFragmentDialog.setArguments(bundle);
        return nfcWriterFragmentDialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        writing();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        json=getArguments().getString("json")==null?"":getArguments().getString("json");
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (tv_notice.getText().toString().trim().equals("写入成功")){
            Toast.makeText(getActivity(),"此信息已写入成功，请返回选择其他房屋！",Toast.LENGTH_SHORT).show();
            return;
        }
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NfcOperation.NfcWriteNDEF(json, detectedTag,new NfcOperation.NFCWriteCallBackListener() {
            @Override
            public void success() {
                writeSuccess();
            }

            @Override
            public void error(String error) {
                writeError(error);
            }
        });

    }
    @Event(value = R.id.tv_state)
    private void okClick(View view){
        this.dismiss();
    }
    private void writing(){
        iv_reading.setImageResource(R.drawable.nfc_reader);
        AnimationDrawable animationDrawable =(AnimationDrawable)iv_reading.getDrawable();
        animationDrawable.setOneShot(false);
        animationDrawable.start();
    }
    private void writeError(String error){
        tv_state.setText("确定");
        tv_notice.setText(error);
        iv_reading.setImageResource(R.drawable.icon_error);
    }
    private void writeSuccess(){
        tv_state.setText("确定");
        tv_state.setTextColor(Color.parseColor("#ffffff"));
        tv_notice.setText("写入成功");
        tv_state.setBackgroundColor(Color.parseColor("#0d5302"));
        iv_reading.setImageResource(R.drawable.icon_correct);
    }


}
