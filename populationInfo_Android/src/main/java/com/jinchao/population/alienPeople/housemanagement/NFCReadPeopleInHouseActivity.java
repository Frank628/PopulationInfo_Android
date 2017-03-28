package com.jinchao.population.alienPeople.housemanagement;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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

import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.adapter.NfcPopIndicatorAdapter;
import com.jinchao.population.alienPeople.SearchTwoWayActivity;
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
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.XMLParserUtil;
import com.jinchao.population.utils.nfcutil.NfcOperation;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.emptyview.HHEmptyView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.DrawableBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

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
    boolean noInit=true;
    NavigationLayout navigationLayout;
    private IndicatorViewPager indicatorViewPager;
    @ViewInject(R.id.moretab_indicator) ScrollIndicatorView scrollIndicatorView;
    @ViewInject(R.id.moretab_viewPager)ViewPager viewPager;
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
                public void cancel() {}
            });
        }
        nfcAdapter=NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        if (!getIntent().getBooleanExtra(Constants.IS_NFC_READER,false)){
            onNewIntent(getIntent());
        }
        scrollIndicatorView.setBackgroundColor(Color.WHITE);
        scrollIndicatorView.setScrollBar(new DrawableBar(this, R.drawable.round_border_white_selector, ScrollBar.Gravity.CENTENT_BACKGROUND) {
            @Override
            public int getHeight(int tabHeight) {
                return tabHeight - CommonUtils.dip2px(NFCReadPeopleInHouseActivity.this,12);
            }
            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - CommonUtils.dip2px(NFCReadPeopleInHouseActivity.this,12);
            }
        });
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.WHITE, Color.BLACK));

        viewPager.setOffscreenPageLimit(0);
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        scrollIndicatorView.setSplitAuto(true);
        scrollIndicatorView.setPinnedTabView(false);
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
            final NFCJsonBean nfcJsonBean= GsonTools.changeGsonToBean(json,NFCJsonBean.class);
            if (nfcJsonBean.sq.equals(MyInfomationManager.getSQNAME(this))){
                ((MyApplication)getApplication()).setDataBaseTableNo(DatabaseUtil.getSQ_DataBase_No(NFCReadPeopleInHouseActivity.this));
                getRooms(nfcJsonBean.code,nfcJsonBean);
                navigationLayout.setRightText("新增/办证", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(NFCReadPeopleInHouseActivity.this,SearchTwoWayActivity.class);
                        intent.putExtra("IS_SHOW_BOTTOM",false);
                        intent.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                        startActivity(intent);
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
    private void getRooms(String scode,final NFCJsonBean nfcJsonBean){
//        showProgressDialog("","正在加载所有室号...");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","searchRoom");
        params.addBodyParameter("sqdm",MyInfomationManager.getSQCODE(this));
        params.addBodyParameter("scode",scode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<String> rooms=XMLParserUtil.parseXMLtoRooms(result);
                rooms.add(0,"搬离人员");
                rooms.add(0,"全部");
                NfcPopIndicatorAdapter adapter=new NfcPopIndicatorAdapter(getSupportFragmentManager());
                adapter.initAdpater(NFCReadPeopleInHouseActivity.this,nfcJsonBean,rooms);
                indicatorViewPager.setAdapter(adapter);
                if (!TextUtils.isEmpty(nfcJsonBean.room)){
                    for (int i=0;i<rooms.size();i++){
                       if (rooms.get(i).equals(nfcJsonBean.room)){
                           indicatorViewPager.setCurrentItem(i,true);
                       }
                    }
                }else{
                    indicatorViewPager.setCurrentItem(0,true);
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });
    }
}
