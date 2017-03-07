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
//        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        writeNFCTag(detectedTag,json);
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[] { createTextRecord(json) });
        boolean result = writeTag(ndefMessage, detectedTag);
        if (result){
            writeSuccess();
        } else {
           writeError("写入失败");
        }
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
//    public void writeNFCTag(Tag tag,String json) {
//        if (tag == null) {
//            writeError("无法检测此NFC标签");
//            return;
//        }
//        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{NdefRecord.createApplicationRecord(json)});
//        //转换成字节获得大小
//        int size = ndefMessage.toByteArray().length;
//        try {
//            //2.判断NFC标签的数据类型（通过Ndef.get方法）
//            Ndef ndef = Ndef.get(tag);
//            //判断是否为NDEF标签
//            if (ndef != null) {
//                ndef.connect();
//                //判断是否支持可写
//                if (!ndef.isWritable()) {
//                    writeError("此标签加密锁死或不支持");
//                    return;
//                }
//                //判断标签的容量是否够用
//                if (ndef.getMaxSize() < size) {
//                    writeError("此标签存储空间不足");
//                    return;
//                }
//                //3.写入数据
//                ndef.writeNdefMessage(ndefMessage);
//                writeSuccess();
//                Toast.makeText(getActivity(), "写入成功", Toast.LENGTH_SHORT).show();
//            } else { //当我们买回来的NFC标签是没有格式化的，或者没有分区的执行此步
//                //Ndef格式类
//                NdefFormatable format = NdefFormatable.get(tag);
//                //判断是否获得了NdefFormatable对象，有一些标签是只读的或者不允许格式化的
//                if (format != null) {
//                    //连接
//                    format.connect();
//                    //格式化并将信息写入标签
//                    format.format(ndefMessage);
//                    writeSuccess();
//                    Toast.makeText(getActivity(), "写入成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    writeError("写入失败");
//                    Toast.makeText(getActivity(), "写入失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } catch (Exception e) {
//            writeError("写入失败");
//        }
//    }
    /**
     * 创建NDEF文本数据
     * @param text
     * @return
     */
    public static NdefRecord createTextRecord(String text) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        //将文本转换为UTF-8格式
        byte[] textBytes = text.getBytes(utfEncoding);
        //设置状态字节编码最高位数为0
        int utfBit = 0;
        //定义状态字节
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置第一个状态字节，先将状态码转换成字节
        data[0] = (byte) status;
        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
        //到textBytes.length的位置
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }
    /**
     * 写数据
     * @param ndefMessage 创建好的NDEF文本数据
     * @param tag 标签
     * @return
     */
    public static boolean writeTag(NdefMessage ndefMessage, Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

}
