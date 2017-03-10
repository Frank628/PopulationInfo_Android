package com.jinchao.population.alienPeople.housemanagement;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.entity.SortbyRoomCodeRenyuanInhouseOneClass;
import com.jinchao.population.entity.SortbyTimeRenyuanInhouseOneClass;
import com.jinchao.population.main.LoginActivity;
import com.jinchao.population.mainmenu.SearchPeopleActivity;
import com.jinchao.population.mainmenu.SearchPeopleDetailActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.nfcutil.NfcOperation;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.emptyview.HHEmptyView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/3/7.
 */
@ContentView(R.layout.activity_nfc_people)
public class NFCReadPeopleInHouseActivity extends BaseActiviy{
    NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    @ViewInject(R.id.rg_sort)RadioGroup rg_sort;
    @ViewInject(R.id.lv)ListView lv;
    @ViewInject(R.id.tv_nfc_touch)TextView tv_nfc_touch;
    @ViewInject(R.id.empty_view) private HHEmptyView empty_view;
    @ViewInject(R.id.ll_list) private LinearLayout ll_list;
    @ViewInject(R.id.edt_idcard) private EditText edt_idcard;
    @ViewInject(R.id.tv_nopeople) private TextView tv_nopeople;
    @ViewInject(R.id.rb_roomcode)RadioButton rb_roomcode;
    boolean noInit=true;
    NavigationLayout navigationLayout;
    String code="",json="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("人员信息");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (TextUtils.isEmpty(MyInfomationManager.getSQNAME(this))){
            Dialog.showForceDialog(this, "提示", "您尚未登录过，请先登录！", new Dialog.DialogClickListener() {
                @Override
                public void confirm() {
                    Intent intent =new Intent(NFCReadPeopleInHouseActivity.this, LoginActivity.class);
                    intent.putExtra(Constants.IS_NFC_READER,true);
                    startActivity(intent);
                    NFCReadPeopleInHouseActivity.this.finish();
                }

                @Override
                public void cancel() {

                }
            });
        }
        nfcAdapter=NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        empty_view.setLoadingModel(HHEmptyView.MODEL_ALERT);
        empty_view.bindView(ll_list);
        empty_view.setOnBtnClickListener(new HHEmptyView.OnBtnClickListener() {
            @Override
            public void onBtnClick() {
                getPeopleInhouse(code);
            }
        });
        rg_sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_roomcode:
                        if (!TextUtils.isEmpty(json)) {
                            processData(json);
                        }
                        break;
                    case R.id.rb_time:
                        if (!TextUtils.isEmpty(json)) {
                            processData(json);
                        }
                        break;
                }
            }
        });
        edt_idcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                processData(json);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RenyuanInHouseBean.RenyuanInhouseOne renyuanInHouseone=(RenyuanInHouseBean.RenyuanInhouseOne) ((ListView)parent).getItemAtPosition(position);
                Intent intent =new Intent(NFCReadPeopleInHouseActivity.this, SearchPeopleDetailActivity.class);
                intent.putExtra("renyuan", renyuanInHouseone);
                startActivity(intent);
            }
        });
        if (!getIntent().getBooleanExtra(Constants.IS_NFC_READER,false)){
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(detectedTag);
        NfcOperation.NfcreadNDEF(intent, new NfcOperation.NFCReadCallBackListener() {
            @Override
            public void success(String result) {
                readSuccess(result);
            }
            @Override
            public void error(String error) {
                readFail(error);
            }
        });
    }

    private void readFail(String error){
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
    private void readSuccess(String json){
        try {
            NFCJsonBean nfcJsonBean= GsonTools.changeGsonToBean(json,NFCJsonBean.class);
            if (nfcJsonBean.sq.equals(MyInfomationManager.getSQNAME(this))){
                code=nfcJsonBean.code;
                getPeopleInhouse(nfcJsonBean.code);
                navigationLayout.setRightText("", R.drawable.icon_infor, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else{
                Dialog.showForceDialog(this, "提示", "当前登录账号不属于‘" + nfcJsonBean.sq + "’,\n请切换账号！", new Dialog.DialogClickListener() {
                    @Override
                    public void confirm() {
                        Intent intent =new Intent(NFCReadPeopleInHouseActivity.this, LoginActivity.class);
                        intent.putExtra(Constants.IS_NFC_READER,true);
                        startActivity(intent);
                        NFCReadPeopleInHouseActivity.this.finish();
                    }

                    @Override
                    public void cancel() {

                    }
                });

            }
        } catch (Exception e) {
            readFail("标签内容不符合规范！");
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter!=null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
        if (!getIntent().getBooleanExtra(Constants.IS_NFC_READER,false)&&noInit){
            noInit=false;
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter!=null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
    private void getPeopleInhouse(String code){
        empty_view.loading();
        tv_nfc_touch.setVisibility(View.GONE);
        empty_view.setVisibility(View.VISIBLE);
        RequestParams  params=new RequestParams(Constants.URL+"GdPeople.aspx?type=peopleList&sqdm="+MyInfomationManager.getSQCODE(this)+"&scode="+code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                empty_view.setVisibility(View.GONE);
                empty_view.success();
                json=result;
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                empty_view.empty("加载超时");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void noPeopleInHouse(){
        tv_nfc_touch.setVisibility(View.VISIBLE);
        tv_nfc_touch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.icon_noresult),null,null);
        tv_nfc_touch.setTextColor(Color.parseColor("#bfbfbf"));
        tv_nfc_touch.setText("无人居住房屋或无此房屋编号！");
        ll_list.setVisibility(View.GONE);
    }
    private void processData(String  result){
        try {
            RenyuanInHouseBean  renyuanInHouseBean= GsonTools.changeGsonToBean(result,RenyuanInHouseBean.class);
            if (renyuanInHouseBean.data.house_exist.equals("0")){
                noPeopleInHouse();
                return;
            }
            if (renyuanInHouseBean.data.people_exist.equals("0")){
                noPeopleInHouse();
                return;
            }
            ll_list.setVisibility(View.VISIBLE);
            tv_nfc_touch.setVisibility(View.GONE);
            tv_nopeople.setVisibility(View.GONE);
            List<RenyuanInHouseBean.RenyuanInhouseOne> list=new ArrayList<RenyuanInHouseBean.RenyuanInhouseOne>();
            list.clear();
            if(!TextUtils.isEmpty(edt_idcard.getText().toString().trim())){
                for (int i = 0; i <renyuanInHouseBean.data.peoplelist.size(); i++) {
                    if (renyuanInHouseBean.data.peoplelist.get(i).idcard.trim().contains(edt_idcard.getText().toString().trim())){
                        list.add(renyuanInHouseBean.data.peoplelist.get(i));
                    }
                }
                if (list.size()==0){
                    tv_nopeople.setVisibility(View.VISIBLE);
                    tv_nopeople.setText("该房屋无此人");
                }
            }else {
                list.addAll(renyuanInHouseBean.data.peoplelist);
            }
            if (rb_roomcode.isChecked()){
                Collections.sort(list,new SortbyRoomCodeRenyuanInhouseOneClass());
            }else{
                Collections.sort(list,new SortbyTimeRenyuanInhouseOneClass());
            }
            CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne> adapter =new CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne>(NFCReadPeopleInHouseActivity.this,list,R.layout.item_renyuan) {
                @Override
                public void convert(ViewHolder helper, RenyuanInHouseBean.RenyuanInhouseOne item, int position) {
                    helper.setText(R.id.tv_name,"姓名: "+ item.sname);
                    DbUtils dbUtils = DeviceUtils.getDbUtils(NFCReadPeopleInHouseActivity.this);
                    People people = null;
                    try {
                        people=dbUtils.findFirst(Selector.from(People.class).where("cardno", "=", item.idcard.trim()));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    if (people!=null){
                        if (people.module.equals("变更")) {
                            helper.setText(R.id.tv_status, "【延期】");
                        }else if (people.module.equals("注销")) {
                            helper.setText(R.id.tv_status, "【注销】");
                        }else{

                            String[] split = item.write_time.split("\\s+");
                            if (split.length>1) {
                                if (CommonUtils.isBigerthanElevenandSmallthanTwelve(split[0])) {
                                    helper.setText(R.id.tv_status, "【"+item.resdients_status+"?】");
                                }else{
                                    helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
                                }
                            }else{
                                helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
                            }
                        }
                    }else{
                        String[] split = item.write_time.split("\\s+");
                        if (split.length>1) {
                            if (CommonUtils.isBigerthanElevenandSmallthanTwelve(split[0])) {
                                helper.setText(R.id.tv_status, "【"+item.resdients_status+"?】");
                            }else{
                                helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
                            }
                        }else{
                            helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
                        }
                    }
                    helper.setText(R.id.tv_shihao, "室号: "+item.shihao);
                    helper.setText(R.id.tv_time, item.write_time);
                }
            };
            lv.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
