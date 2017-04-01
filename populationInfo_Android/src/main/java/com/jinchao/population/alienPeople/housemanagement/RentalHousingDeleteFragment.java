package com.jinchao.population.alienPeople.housemanagement;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.config.Constants;
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
import com.jinchao.population.entity.DeleteRealPeopleBean;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.XMLParserUtil;
import com.jinchao.population.utils.nfcutil.NfcOperation;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;

/**
 * Created by OfferJiShu01 on 2017/2/28.
 */
@ContentView(R.layout.fragment_rentalhousing_delete)
public class RentalHousingDeleteFragment extends BaseFragment{

    @ViewInject(R.id.tv_content)TextView tv_content;
    @ViewInject(R.id.btn_search)Button btn_search;
    @ViewInject(R.id.edt_content)ValidateEidtText edt_content;
    @ViewInject(R.id.tv_noresult)TextView tv_noresult;
    private DbUtils dbUtils;
    private String house_id;
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
        NfcOperation.NfcreadNDEF(intent, new NfcOperation.NFCReadCallBackListener() {
            @Override
            public void success(String result) {
                readSuccess(result);
                hideProcessDialog();
            }
            @Override
            public void error(String error) {
                readFail(error);
                hideProcessDialog();
            }
        });
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
                        SearchhasHouse(houseAddressOldBean.toString(),houseAddressOldBean.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 2:
                    HouseAddressOldBean2 houseAddressOldBean2 = dbUtils.findFirst(Selector.from(HouseAddressOldBean2.class).where("scode", "like", code));
                    if (houseAddressOldBean2!=null) {
                        SearchhasHouse(houseAddressOldBean2.toString(),houseAddressOldBean2.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 3:
                    HouseAddressOldBean3 houseAddressOldBean3 = dbUtils.findFirst(Selector.from(HouseAddressOldBean3.class).where("scode", "like", code));
                    if (houseAddressOldBean3!=null) {
                        SearchhasHouse(houseAddressOldBean3.toString(),houseAddressOldBean3.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 4:
                    HouseAddressOldBean4 houseAddressOldBean4 = dbUtils.findFirst(Selector.from(HouseAddressOldBean4.class).where("scode", "like", code));
                    if (houseAddressOldBean4!=null) {
                        SearchhasHouse(houseAddressOldBean4.toString(),houseAddressOldBean4.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 5:
                    HouseAddressOldBean5 houseAddressOldBean5 = dbUtils.findFirst(Selector.from(HouseAddressOldBean5.class).where("scode", "like", code));
                    if (houseAddressOldBean5!=null) {
                        SearchhasHouse(houseAddressOldBean5.toString(),houseAddressOldBean5.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 6:
                    HouseAddressOldBean6 houseAddressOldBean6 = dbUtils.findFirst(Selector.from(HouseAddressOldBean6.class).where("scode", "like", code));
                    if (houseAddressOldBean6!=null) {
                        SearchhasHouse(houseAddressOldBean6.toString(),houseAddressOldBean6.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 7:
                    HouseAddressOldBean7 houseAddressOldBean7 = dbUtils.findFirst(Selector.from(HouseAddressOldBean7.class).where("scode", "like", code));
                    if (houseAddressOldBean7!=null) {
                        SearchhasHouse(houseAddressOldBean7.toString(),houseAddressOldBean7.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 8:
                    HouseAddressOldBean8 houseAddressOldBean8 = dbUtils.findFirst(Selector.from(HouseAddressOldBean8.class).where("scode", "like", code));
                    if (houseAddressOldBean8!=null) {
                        SearchhasHouse(houseAddressOldBean8.toString(),houseAddressOldBean8.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 9:
                    HouseAddressOldBean9 houseAddressOldBean9 = dbUtils.findFirst(Selector.from(HouseAddressOldBean9.class).where("scode", "like", code));
                    if (houseAddressOldBean9!=null) {
                        SearchhasHouse(houseAddressOldBean9.toString(),houseAddressOldBean9.id);
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 10:
                    HouseAddressOldBean10 houseAddressOldBean10 = dbUtils.findFirst(Selector.from(HouseAddressOldBean10.class).where("scode", "like", code));
                    if (houseAddressOldBean10!=null) {
                        SearchhasHouse(houseAddressOldBean10.toString(),houseAddressOldBean10.id);
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
    private void SearchhasHouse(String str,String id){
        tv_noresult.setVisibility(View.GONE);
        tv_content.setText(str);
        house_id=id;
    }
    @Event(value = {R.id.btn_unregist})
    private void submit(View view){
        String scode=edt_content.getText().toString().trim();
        if (TextUtils.isEmpty(scode)){
            Toast.makeText(getActivity(),"请先输入房屋编号查询！",Toast.LENGTH_SHORT).show();
            return;
        }
        deleteHouseInfo(scode);
    }
    private void deleteHouseInfo(final String scode){
        showProcessDialog("数据提交中...");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","cancel");
        params.addBodyParameter("sqdm", MyInfomationManager.getSQCODE(getActivity()));
        params.addBodyParameter("scode",scode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("rr",result);
                XMLParserUtil.parseXMLforReportLoss(result, new XMLParserUtil.OnXmlParserListener() {
                    @Override
                    public void success(String result) {
                        deleteHouseTable(house_id);
                        deleteHouseInfoLocal(scode);
                        Toast.makeText(getActivity(),"注销成功",Toast.LENGTH_SHORT).show();
                        hideProcessDialog();
                    }
                    @Override
                    public void fail(String error) {
                        if ("此出租屋信息不存在！".equals(error)){
                            deleteHouseInfoLocal(scode);
                            return;
                        }
                        Dialog.showForceDialog(getActivity(),"提示",error, new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {

                            }

                            @Override
                            public void cancel() {

                            }
                        });
                    }
                });


            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(),"数据提交失败，请稍后重试",Toast.LENGTH_SHORT).show();
                hideProcessDialog();
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() { hideProcessDialog();}
        });
    }
    private void deleteHouseInfoLocal( String scode){
        RequestParams params=new RequestParams(Constants.URL+"HouseSave.aspx");
        params.addBodyParameter("type","housedel");
        params.addBodyParameter("user_id", MyInfomationManager.getUserID(getActivity()));
        params.addBodyParameter("scode",scode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("delete",result);
                try {
                    DeleteRealPeopleBean deleteRealPeopleBean=GsonTools.changeGsonToBean(result,DeleteRealPeopleBean.class);
                    Log.e("delete",deleteRealPeopleBean.data.get(0).success);
                    if (deleteRealPeopleBean.data.get(0).success.equals("true")){
                        deleteHouseTable(house_id);
                        Toast.makeText(getActivity(),"注销成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"注销失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"注销失败",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() { }
        });
    }
    private void deleteHouseTable(String house_id){
        try {
            dbUtils= DeviceUtils.getDbUtils(getActivity());
            switch (database_tableNo){
                case 1:
                    dbUtils.update(new HouseAddressOldBean(house_id,"8"),"source_id");
                    break;
                case 2:
                    dbUtils.update(new HouseAddressOldBean2(house_id,"8"),"source_id");
                    break;
                case 3:
                    dbUtils.update(new HouseAddressOldBean3(house_id,"8"),"source_id");
                    break;
                case 4:
                    dbUtils.update(new HouseAddressOldBean4(house_id,"8"),"source_id");
                    break;
                case 5:
                    dbUtils.update(new HouseAddressOldBean5(house_id,"8"),"source_id");
                    break;
                case 6:
                    dbUtils.update(new HouseAddressOldBean6(house_id,"8"),"source_id");
                    break;
                case 7:
                    dbUtils.update(new HouseAddressOldBean7(house_id,"8"),"source_id");
                    break;
                case 8:
                    dbUtils.update(new HouseAddressOldBean8(house_id,"8"),"source_id");
                    break;
                case 9:
                    dbUtils.update(new HouseAddressOldBean9(house_id,"8"),"source_id");
                    break;
                case 10:
                    dbUtils.update(new HouseAddressOldBean10(house_id,"8"),"source_id");
                    break;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
