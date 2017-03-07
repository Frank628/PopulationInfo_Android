package com.jinchao.population.alienPeople.housemanagement;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyApplication;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.dbentity.HouseAddressOldBean;
import com.jinchao.population.dbentity.HouseAddressOldBean10;
import com.jinchao.population.dbentity.HouseAddressOldBean2;
import com.jinchao.population.dbentity.HouseAddressOldBean3;
import com.jinchao.population.dbentity.HouseAddressOldBean4;
import com.jinchao.population.dbentity.HouseAddressOldBean5;
import com.jinchao.population.dbentity.HouseAddressOldBean6;
import com.jinchao.population.dbentity.HouseAddressOldBean7;
import com.jinchao.population.dbentity.HouseAddressOldBean8;
import com.jinchao.population.dbentity.HouseAddressOldBean9;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;

/**
 * Created by OfferJiShu01 on 2017/2/28.
 */
@ContentView(R.layout.fragment_rentalhousing_inquiries)
public class RentalHousingInquiriesFragment extends BaseFragment{

    @ViewInject(R.id.tv_content)TextView tv_content;
    @ViewInject(R.id.btn_search)Button btn_search;
    @ViewInject(R.id.edt_content)ValidateEidtText edt_content;
    @ViewInject(R.id.tv_noresult)TextView tv_noresult;
    private DbUtils dbUtils;
    public int database_tableNo=0;//当前登录账号使用的哪个数据库，0:未下载的地址库，1：表1,2：表2.。。。
    @Override
    public void onStart() {
        super.onStart();
        tv_content.setText("");
        edt_content.setText("");
        if (((MyApplication)getActivity().getApplication()).database_tableNo==0){
            database_tableNo= DatabaseUtil.getNullDB(getActivity());
        }else{
            database_tableNo=((MyApplication)getActivity().getApplication()).database_tableNo;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(detectedTag);
        showProcessDialog("正在读取NFC标签..");
        readNfcTag(intent);
    }
    /**
     * 读取NFC标签文本数据
     */
    private void readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    String textRecord = parseTextRecord(record);
                    readSuccess(textRecord);
                }else{
                    readFail("此标签中无内容");
                }
                hideProcessDialog();
            } catch (Exception e) {
                hideProcessDialog();
                readFail("此标签类型不匹配！");
            }
        }else{
            hideProcessDialog();
            readFail("无效的NFC卡片类型");
        }
    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     * @param ndefRecord
     * @return
     */
    public static String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();

        }
    }
    @Event(value = {R.id.btn_search})
    private void search(View view){
        String code =edt_content.getText().toString().trim();
        if (TextUtils.isEmpty(code)||code.length()!=6){
            Toast.makeText(getActivity(),"请输入6位的房屋编号",Toast.LENGTH_SHORT).show();
            return;
        }
        hidenSoftKeyBoard(edt_content);
        showProcessDialog("正在查询...");
        try {
            dbUtils= DeviceUtils.getDbUtils(getActivity());
            switch (database_tableNo){
                case 1:
                    HouseAddressOldBean houseAddressOldBean = dbUtils.findFirst(Selector.from(HouseAddressOldBean.class).where("scode", "like", code));
                    if (houseAddressOldBean!=null) {
                        SearchhasHouse(houseAddressOldBean.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 2:
                    HouseAddressOldBean2 houseAddressOldBean2 = dbUtils.findFirst(Selector.from(HouseAddressOldBean2.class).where("scode", "like", code));
                    if (houseAddressOldBean2!=null) {
                        SearchhasHouse(houseAddressOldBean2.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 3:
                    HouseAddressOldBean3 houseAddressOldBean3 = dbUtils.findFirst(Selector.from(HouseAddressOldBean3.class).where("scode", "like", code));
                    if (houseAddressOldBean3!=null) {
                        SearchhasHouse(houseAddressOldBean3.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 4:
                    HouseAddressOldBean4 houseAddressOldBean4 = dbUtils.findFirst(Selector.from(HouseAddressOldBean4.class).where("scode", "like", code));
                    if (houseAddressOldBean4!=null) {
                        SearchhasHouse(houseAddressOldBean4.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 5:
                    HouseAddressOldBean5 houseAddressOldBean5 = dbUtils.findFirst(Selector.from(HouseAddressOldBean5.class).where("scode", "like", code));
                    if (houseAddressOldBean5!=null) {
                        SearchhasHouse(houseAddressOldBean5.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 6:
                    HouseAddressOldBean6 houseAddressOldBean6 = dbUtils.findFirst(Selector.from(HouseAddressOldBean6.class).where("scode", "like", code));
                    if (houseAddressOldBean6!=null) {
                        SearchhasHouse(houseAddressOldBean6.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 7:
                    HouseAddressOldBean7 houseAddressOldBean7 = dbUtils.findFirst(Selector.from(HouseAddressOldBean7.class).where("scode", "like", code));
                    if (houseAddressOldBean7!=null) {
                        SearchhasHouse(houseAddressOldBean7.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 8:
                    HouseAddressOldBean8 houseAddressOldBean8 = dbUtils.findFirst(Selector.from(HouseAddressOldBean8.class).where("scode", "like", code));
                    if (houseAddressOldBean8!=null) {
                        SearchhasHouse(houseAddressOldBean8.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 9:
                    HouseAddressOldBean9 houseAddressOldBean9 = dbUtils.findFirst(Selector.from(HouseAddressOldBean9.class).where("scode", "like", code));
                    if (houseAddressOldBean9!=null) {
                        SearchhasHouse(houseAddressOldBean9.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 10:
                    HouseAddressOldBean10 houseAddressOldBean10 = dbUtils.findFirst(Selector.from(HouseAddressOldBean10.class).where("scode", "like", code));
                    if (houseAddressOldBean10!=null) {
                        SearchhasHouse(houseAddressOldBean10.toString());
                    }else{
                        SearchNoHouse();
                    }
                    break;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProcessDialog();
            }
        },200);

    }
    private void readFail(String error){
        tv_content.setText("");
        tv_noresult.setVisibility(View.VISIBLE);
        tv_noresult.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.icon_noresult),null,null);
        tv_noresult.setText(error);
    }
    private void readSuccess(String json){
        try {
            NFCJsonBean nfcJsonBean= GsonTools.changeGsonToBean(json,NFCJsonBean.class);
            edt_content.setText(nfcJsonBean.code);
            tv_noresult.setVisibility(View.GONE);
            tv_content.setText(nfcJsonBean.toString());
        } catch (Exception e) {
            edt_content.setText("");
            tv_content.setText("标签内容不符合规范！");
            e.printStackTrace();
        }
    }
    private void SearchNoHouse(){
        tv_content.setText("");
        tv_noresult.setVisibility(View.VISIBLE);
        tv_noresult.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.icon_noresult),null,null);
        tv_noresult.setText("无此房屋编号！");
    }
    private void SearchhasHouse(String str){
        tv_noresult.setVisibility(View.GONE);
        tv_content.setText(str);
    }
}
