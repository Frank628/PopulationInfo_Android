package com.jinchao.population.alienPeople.housemanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.jinchao.population.entity.HouseInfoBean;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.XMLParserUtil;
import com.jinchao.population.utils.nfcutil.NfcOperation;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.StringWheel;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2017/2/28.
 */
@ContentView(R.layout.fragment_rentalhousing_dataeditor2)
public class RentalHousingDataEditorFragment2 extends BaseFragment{
    @ViewInject(R.id.root)RelativeLayout root;
    @ViewInject(R.id.ll_edit)LinearLayout ll_edit;
    @ViewInject(R.id.btn_search)Button btn_search;
    @ViewInject(R.id.edt_content)ValidateEidtText edt_content;
    @ViewInject(R.id.edt_name)ValidateEidtText edt_name;
    @ViewInject(R.id.edt_sfz)ValidateEidtText edt_sfz;
    @ViewInject(R.id.edt_phone)ValidateEidtText edt_phone;
    @ViewInject(R.id.edt_mj)ValidateEidtText edt_mj;
    @ViewInject(R.id.edt_hs)ValidateEidtText edt_hs;
    @ViewInject(R.id.tv_noresult)TextView tv_noresult;
    @ViewInject(R.id.tv_address)TextView tv_address;
    @ViewInject(R.id.tv_fwlx)TextView tv_fwlx;
    @ViewInject(R.id.tv_fwjg)TextView tv_fwjg;
    @ViewInject(R.id.tv_fwyt)TextView tv_fwyt;
    @ViewInject(R.id.tv_zzlx)TextView tv_zzlx;
    private boolean[] yixuan_fwyt=new boolean[]{false,false,false,false};
    private StringBuffer sb_code;
    private String fzxm="",fzsfz="",fzdh="",fwyt="",jzhs="",jzmj="",fwjg="",fwlx="",zzlx="";
    private DbUtils dbUtils;
    public int database_tableNo=0;//当前登录账号使用的哪个数据库，0:未下载的地址库，1：表1,2：表2.。。。

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_edit.setVisibility(View.GONE);
        edt_name.setText("");
        edt_sfz.setText("");
        edt_phone.setText("");
        edt_content.setText("");
        tv_noresult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();

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
        SearchHouse(code);

    }
    private void readFail(String error){
        ll_edit.setVisibility(View.GONE);
        tv_noresult.setVisibility(View.VISIBLE);
        tv_noresult.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.icon_noresult),null,null);
        tv_noresult.setText(error);
    }
    private void readSuccess(String json){
        try {
            NFCJsonBean nfcJsonBean= GsonTools.changeGsonToBean(json,NFCJsonBean.class);
            SearchhasHouse(nfcJsonBean);
        } catch (Exception e) {
            Toast.makeText(getActivity(),"标签内容不符合规范",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void SearchNoHouse(){
//        ll_edit.setVisibility(View.GONE);
//        edt_name.setText("");
//        edt_sfz.setText("");
//        edt_phone.setText("");
//        tv_noresult.setVisibility(View.VISIBLE);
//        tv_noresult.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.icon_noresult),null,null);
//        tv_noresult.setText("无此房屋编号！");
    }
    private void SearchhasHouse(NFCJsonBean nfcJsonBean){
        tv_address.setText(nfcJsonBean.getAdd());
//        tv_noresult.setVisibility(View.GONE);
//        ll_edit.setVisibility(View.VISIBLE);
//        edt_content.setText(nfcJsonBean.code);
//        edt_content.setSelection(nfcJsonBean.code.length());
//        edt_name.setText(nfcJsonBean.name);
//        edt_sfz.setText(nfcJsonBean.idcard);
//        edt_phone.setText(nfcJsonBean.phone);
    }
    @Event(value = {R.id.btn_submit})
    private void submit(View view){
        String scode=edt_content.getText().toString().trim();
        String name=edt_name.getText().toString().trim();
        String idcard=edt_sfz.getText().toString().trim();
        String phone=edt_phone.getText().toString().trim();
        String mj=edt_mj.getText().toString().trim();
        String hs=edt_hs.getText().toString().trim();
        if (TextUtils.isEmpty(scode)){
            Toast.makeText(getActivity(),"请先输入房屋编号查询！",Toast.LENGTH_SHORT).show();
            return;
        }
        editHouseInfo(scode,name,idcard,phone,mj,hs);
    }
    private void bindData(HouseInfoBean houseInfo){
        tv_noresult.setVisibility(View.GONE);
        ll_edit.setVisibility(View.VISIBLE);
        fzxm=houseInfo.fzxm;fzsfz=houseInfo.fzsfz;fzdh=houseInfo.fzdh;fwyt=houseInfo.fwyt;jzhs=houseInfo.jzhs;jzmj=houseInfo.jzmj;fwjg=houseInfo.fwjg;fwlx=houseInfo.fwlx;zzlx=houseInfo.zzlx;
        edt_name.setText(fzxm);
        edt_phone.setText(fzdh);
        edt_sfz.setText(fzsfz);
        edt_hs.setText(jzhs);
        edt_mj.setText(jzmj);
        String[] fwyts=fwyt.split(",");
        StringBuffer sb_fwyt = new StringBuffer(20);
        for (int i=0;i<fwyts.length;i++){
            for (int j=0;j<Constants.FANGWUYONGTU_CODE.length;j++){
                if (fwyts[i].equals(Constants.FANGWUYONGTU_CODE[j])){
                    yixuan_fwyt[j]=true;
                    if (TextUtils.isEmpty(sb_fwyt)){
                        sb_fwyt.append(Constants.FANGWUYONGTU[j]);
                    }else{
                        sb_fwyt.append(","+Constants.FANGWUYONGTU[j]);
                    }
                }
            }
        }
        tv_fwyt.setText(sb_fwyt);
        for(int i=0;i<Constants.FANGWULEIXING_CODE.length;i++){
            if (fwlx.trim().equals(Constants.FANGWULEIXING_CODE[i])){
                tv_fwlx.setText(Constants.FANGWULEIXING[i]);
            }
        }
        for(int i=0;i<Constants.FANGWUJIEGOU_CODE.length;i++){
            if (fwjg.trim().equals(Constants.FANGWUJIEGOU_CODE[i])){
                tv_fwjg.setText(Constants.FANGWUJIEGOU[i]);
            }
        }
        for(int i=0;i<Constants.ZUZHULEIXING_CODE.length;i++){
            if (zzlx.trim().equals(Constants.ZUZHULEIXING_CODE[i])){
                tv_zzlx.setText(Constants.ZUZHULEIXING[i]);
            }
        }


    }
    private void SearchHouse(final String scode){
        showProcessDialog("房屋信息查询中...");
        seachLocal(scode);
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","searchhouse");
        params.addBodyParameter("sqdm", MyInfomationManager.getSQCODE(getActivity()));
        params.addBodyParameter("scode",scode);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                HouseInfoBean houseInfoBean=XMLParserUtil.parseHouseInfor(result);
                if (TextUtils.isEmpty(houseInfoBean.getMsg())){
                    bindData(houseInfoBean);
                }else{
                    ll_edit.setVisibility(View.GONE);
                    tv_noresult.setVisibility(View.VISIBLE);
                    tv_noresult.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.icon_noresult),null,null);
                    tv_noresult.setText("无此房屋编号！");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(),"房屋信息查询失败，请稍后重试",Toast.LENGTH_SHORT).show();
                hideProcessDialog();
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() { hideProcessDialog();}
        });
    }
    private void editHouseInfo(final String scode, final String name, final String idcard, final String phone,String mj,String hs){
        showProcessDialog("数据提交中...");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","updateqzf");
        params.addBodyParameter("sqdm", MyInfomationManager.getSQCODE(getActivity()));
        params.addBodyParameter("scode",scode);
        params.addBodyParameter("fzxm",name);
        params.addBodyParameter("fzsfz",idcard);
        params.addBodyParameter("fzdh",phone);
        params.addBodyParameter("fwlx",fwlx);
        params.addBodyParameter("fwjg",fwjg);
        params.addBodyParameter("fwmj",mj);
        params.addBodyParameter("fwyt",fwyt);
        params.addBodyParameter("zzhs",hs);
        params.addBodyParameter("zzlx",zzlx);
        Log.d("ddd",fwlx+","+fwjg+","+mj+","+fwyt+","+hs+","+zzlx);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("rr",result);
                XMLParserUtil.parseXMLforReportLoss(result, new XMLParserUtil.OnXmlParserListener() {
                    @Override
                    public void success(String result) {
                        updateHouseTable(scode,idcard,name,phone);
                        editHouseInfoLocal(scode,name,idcard,phone);
                        Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                        hideProcessDialog();
                    }

                    @Override
                    public void fail(String error) {
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
    private void editHouseInfoLocal( String scode, String name,String idcard,String phone){
        RequestParams params=new RequestParams(Constants.URL+"HouseSave.aspx");
        params.addBodyParameter("type","update");
        params.addBodyParameter("user_id", MyInfomationManager.getUserID(getActivity()));
        params.addBodyParameter("scode",scode);
        params.addBodyParameter("fzxm",name);
        params.addBodyParameter("fzsfz",idcard);
        params.addBodyParameter("fzdh",phone);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("dd",result);
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
    @Event(value = {R.id.ll_fwlx})
    private void ll_fwlxClick(View view){
        StringWheel stringWheel=new StringWheel(getActivity(), Constants.FANGWULEIXING, new StringWheel.OnEnsureClickListener() {
            @Override
            public void OnEnSureClick(String str) {
                for(int i=0;i<Constants.FANGWULEIXING.length;i++){
                    if (str.equals(Constants.FANGWULEIXING[i])){
                        fwlx=Constants.FANGWULEIXING_CODE[i];
                    }
                }
                tv_fwlx.setText(str);
            }
        });
        stringWheel.showAtLocation(root, Gravity.BOTTOM, 0, 0);
    }
    @Event(value = {R.id.ll_fwjg})
    private void ll_fwjgClick(View view){
        StringWheel stringWheel2=new StringWheel(getActivity(), Constants.FANGWUJIEGOU, new StringWheel.OnEnsureClickListener() {
            @Override
            public void OnEnSureClick(String str) {
                tv_fwjg.setText(str);
                for(int i=0;i<Constants.FANGWUJIEGOU.length;i++){
                    if (str.equals(Constants.FANGWUJIEGOU[i])){
                        fwjg=Constants.FANGWUJIEGOU_CODE[i];
                    }
                }
            }
        });
        stringWheel2.showAtLocation(root, Gravity.BOTTOM, 0, 0);
    }
    @Event(value = {R.id.ll_zzlx})
    private void ll_zzlxClick(View view){
        StringWheel stringWheel3=new StringWheel(getActivity(), Constants.ZUZHULEIXING, new StringWheel.OnEnsureClickListener() {
            @Override
            public void OnEnSureClick(String str) {
                tv_zzlx.setText(str);
                for(int i=0;i<Constants.ZUZHULEIXING.length;i++){
                    if (str.equals(Constants.ZUZHULEIXING[i])){
                        zzlx=Constants.ZUZHULEIXING_CODE[i];
                    }
                }
            }
        });
        stringWheel3.showAtLocation(root, Gravity.BOTTOM, 0, 0);
    }
    @Event(value = {R.id.ll_fwyt})
    private void ll_fwytClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("房屋用途（可多选）");
        builder.setMultiChoiceItems(Constants.FANGWUYONGTU, yixuan_fwyt, new DialogInterface.OnMultiChoiceClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked){
                if(isChecked){
                    yixuan_fwyt [which]=true;
                }else{
                    yixuan_fwyt [which]=false;
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                StringBuffer sb = new StringBuffer(20);
                sb_code= new StringBuffer(10);
                for(int i=0;i<yixuan_fwyt.length;i++){
                    if(yixuan_fwyt[i]){
                        if (TextUtils.isEmpty(sb)){
                            sb_code.append(Constants.FANGWUYONGTU_CODE[i]);
                            sb.append(Constants.FANGWUYONGTU[i]);
                        }else{
                            sb_code.append(","+Constants.FANGWUYONGTU_CODE[i]);
                            sb.append(","+Constants.FANGWUYONGTU[i]);
                        }
                    }
                }
                fwyt=sb_code.toString();
                tv_fwyt.setText(sb);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        builder.show();
    }



    private void updateHouseTable(String code, String idcard, String name, String phone){
        try {
            dbUtils= DeviceUtils.getDbUtils(getActivity());
            switch (database_tableNo){


                case 1:
                    HouseAddressOldBean houseAddressOldBean = dbUtils.findFirst(Selector.from(HouseAddressOldBean.class).where("scode", "like", code));
                    if (houseAddressOldBean!=null) {
                        houseAddressOldBean.setIdcard(idcard);
                        houseAddressOldBean.setHrs_pname(name);
                        houseAddressOldBean.setTelphone(phone);
                        houseAddressOldBean.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean);
                    }
                    break;
                case 2:
                    HouseAddressOldBean2 houseAddressOldBean2 = dbUtils.findFirst(Selector.from(HouseAddressOldBean2.class).where("scode", "like", code));
                    if (houseAddressOldBean2!=null) {
                        houseAddressOldBean2.setIdcard(idcard);
                        houseAddressOldBean2.setHrs_pname(name);
                        houseAddressOldBean2.setTelphone(phone);
                        houseAddressOldBean2.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean2);
                    }
                    break;
                case 3:
                    HouseAddressOldBean3 houseAddressOldBean3 = dbUtils.findFirst(Selector.from(HouseAddressOldBean3.class).where("scode", "like", code));
                    if (houseAddressOldBean3!=null) {
                        houseAddressOldBean3.setIdcard(idcard);
                        houseAddressOldBean3.setHrs_pname(name);
                        houseAddressOldBean3.setTelphone(phone);
                        houseAddressOldBean3.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean3);
                    }
                    break;
                case 4:
                    HouseAddressOldBean4 houseAddressOldBean4 = dbUtils.findFirst(Selector.from(HouseAddressOldBean4.class).where("scode", "like", code));
                    if (houseAddressOldBean4!=null) {
                        houseAddressOldBean4.setIdcard(idcard);
                        houseAddressOldBean4.setHrs_pname(name);
                        houseAddressOldBean4.setTelphone(phone);
                        houseAddressOldBean4.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean4);
                    }
                    break;
                case 5:
                    HouseAddressOldBean5 houseAddressOldBean5 = dbUtils.findFirst(Selector.from(HouseAddressOldBean5.class).where("scode", "like", code));
                    if (houseAddressOldBean5!=null) {
                        houseAddressOldBean5.setIdcard(idcard);
                        houseAddressOldBean5.setHrs_pname(name);
                        houseAddressOldBean5.setTelphone(phone);
                        houseAddressOldBean5.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean5);
                    }
                    break;
                case 6:
                    HouseAddressOldBean6 houseAddressOldBean6 = dbUtils.findFirst(Selector.from(HouseAddressOldBean6.class).where("scode", "like", code));
                    if (houseAddressOldBean6!=null) {
                        houseAddressOldBean6.setIdcard(idcard);
                        houseAddressOldBean6.setHrs_pname(name);
                        houseAddressOldBean6.setTelphone(phone);
                        houseAddressOldBean6.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean6);
                    }
                    break;
                case 7:
                    HouseAddressOldBean7 houseAddressOldBean7 = dbUtils.findFirst(Selector.from(HouseAddressOldBean7.class).where("scode", "like", code));
                    if (houseAddressOldBean7!=null) {
                        houseAddressOldBean7.setIdcard(idcard);
                        houseAddressOldBean7.setHrs_pname(name);
                        houseAddressOldBean7.setTelphone(phone);
                        houseAddressOldBean7.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean7);
                    }
                    break;
                case 8:
                    HouseAddressOldBean8 houseAddressOldBean8 = dbUtils.findFirst(Selector.from(HouseAddressOldBean8.class).where("scode", "like", code));
                    if (houseAddressOldBean8!=null) {
                        houseAddressOldBean8.setIdcard(idcard);
                        houseAddressOldBean8.setHrs_pname(name);
                        houseAddressOldBean8.setTelphone(phone);
                        houseAddressOldBean8.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean8);
                    }
                    break;
                case 9:
                    HouseAddressOldBean9 houseAddressOldBean9 = dbUtils.findFirst(Selector.from(HouseAddressOldBean9.class).where("scode", "like", code));
                    if (houseAddressOldBean9!=null) {
                        houseAddressOldBean9.setIdcard(idcard);
                        houseAddressOldBean9.setHrs_pname(name);
                        houseAddressOldBean9.setTelphone(phone);
                        houseAddressOldBean9.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean9);
                    }
                    break;
                case 10:
                    HouseAddressOldBean10 houseAddressOldBean10 = dbUtils.findFirst(Selector.from(HouseAddressOldBean10.class).where("scode", "like", code));
                    if (houseAddressOldBean10!=null) {
                        houseAddressOldBean10.setIdcard(idcard);
                        houseAddressOldBean10.setHrs_pname(name);
                        houseAddressOldBean10.setTelphone(phone);
                        houseAddressOldBean10.setSource_id("4");
                        dbUtils.saveOrUpdate(houseAddressOldBean10);
                    }
                    break;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private void seachLocal(String code){
        try {
            dbUtils= DeviceUtils.getDbUtils(getActivity());
            switch (database_tableNo){
                case 0:
                    tv_address.setText("请先全库地址下载");
                    break;
                case 1:
                    HouseAddressOldBean houseAddressOldBean = dbUtils.findFirst(Selector.from(HouseAddressOldBean.class).where("scode", "like", code));
                    if (houseAddressOldBean!=null) {
                        SearchhasHouse(houseAddressOldBean.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 2:
                    HouseAddressOldBean2 houseAddressOldBean2 = dbUtils.findFirst(Selector.from(HouseAddressOldBean2.class).where("scode", "like", code));
                    if (houseAddressOldBean2!=null) {
                        SearchhasHouse(houseAddressOldBean2.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 3:
                    HouseAddressOldBean3 houseAddressOldBean3 = dbUtils.findFirst(Selector.from(HouseAddressOldBean3.class).where("scode", "like", code));
                    if (houseAddressOldBean3!=null) {
                        SearchhasHouse(houseAddressOldBean3.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 4:
                    HouseAddressOldBean4 houseAddressOldBean4 = dbUtils.findFirst(Selector.from(HouseAddressOldBean4.class).where("scode", "like", code));
                    if (houseAddressOldBean4!=null) {
                        SearchhasHouse(houseAddressOldBean4.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 5:
                    HouseAddressOldBean5 houseAddressOldBean5 = dbUtils.findFirst(Selector.from(HouseAddressOldBean5.class).where("scode", "like", code));
                    if (houseAddressOldBean5!=null) {
                        SearchhasHouse(houseAddressOldBean5.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 6:
                    HouseAddressOldBean6 houseAddressOldBean6 = dbUtils.findFirst(Selector.from(HouseAddressOldBean6.class).where("scode", "like", code));
                    if (houseAddressOldBean6!=null) {
                        SearchhasHouse(houseAddressOldBean6.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 7:
                    HouseAddressOldBean7 houseAddressOldBean7 = dbUtils.findFirst(Selector.from(HouseAddressOldBean7.class).where("scode", "like", code));
                    if (houseAddressOldBean7!=null) {
                        SearchhasHouse(houseAddressOldBean7.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 8:
                    HouseAddressOldBean8 houseAddressOldBean8 = dbUtils.findFirst(Selector.from(HouseAddressOldBean8.class).where("scode", "like", code));
                    if (houseAddressOldBean8!=null) {
                        SearchhasHouse(houseAddressOldBean8.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 9:
                    HouseAddressOldBean9 houseAddressOldBean9 = dbUtils.findFirst(Selector.from(HouseAddressOldBean9.class).where("scode", "like", code));
                    if (houseAddressOldBean9!=null) {
                        SearchhasHouse(houseAddressOldBean9.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 10:
                    HouseAddressOldBean10 houseAddressOldBean10 = dbUtils.findFirst(Selector.from(HouseAddressOldBean10.class).where("scode", "like", code));
                    if (houseAddressOldBean10!=null) {
                        SearchhasHouse(houseAddressOldBean10.toNFCJsonBean());
                    }else{
                        SearchNoHouse();
                    }
                    break;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
