package com.jinchao.population.alienPeople.registchangelogoff;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.adapter.NfcPopIndicatorAdapter;
import com.jinchao.population.alienPeople.SearchTwoWayActivity;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.main.LoginActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.XMLParserUtil;
import com.jinchao.population.utils.nfcutil.NfcOperation;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
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

import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/3/7.
 */
@ContentView(R.layout.activity_nfc_people)
public class PeoplesInHouseActivity extends BaseActiviy{
    NavigationLayout navigationLayout;
    private IndicatorViewPager indicatorViewPager;
    @ViewInject(R.id.moretab_indicator) ScrollIndicatorView scrollIndicatorView;
    @ViewInject(R.id.moretab_viewPager)ViewPager viewPager;
    NFCJsonBean nfcJsonBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcJsonBean=(NFCJsonBean) getIntent().getSerializableExtra(Constants.NFCJSONBEAN);
        navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("人员信息");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        navigationLayout.setRightText("新增/办证", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PeoplesInHouseActivity.this,SearchTwoWayActivity.class);
                intent.putExtra("IS_SHOW_BOTTOM",false);
                intent.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                startActivity(intent);
            }
        });
        scrollIndicatorView.setBackgroundColor(Color.WHITE);
        scrollIndicatorView.setScrollBar(new DrawableBar(this, R.drawable.round_border_white_selector, ScrollBar.Gravity.CENTENT_BACKGROUND) {
            @Override
            public int getHeight(int tabHeight) {
                return tabHeight - CommonUtils.dip2px(PeoplesInHouseActivity.this,12);
            }
            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - CommonUtils.dip2px(PeoplesInHouseActivity.this,12);
            }
        });
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.WHITE, Color.BLACK));

        viewPager.setOffscreenPageLimit(0);
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        scrollIndicatorView.setSplitAuto(true);
        scrollIndicatorView.setPinnedTabView(false);
        getRooms(nfcJsonBean);
    }



    private void getRooms(final NFCJsonBean nfcJsonBean){
//        showProgressDialog("","正在加载所有室号...");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","searchRoom");
        params.addBodyParameter("sqdm",MyInfomationManager.getSQCODE(this));
        params.addBodyParameter("scode",nfcJsonBean.code);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<String> rooms=XMLParserUtil.parseXMLtoRooms(result);
                rooms.add(0,"搬离人员");
                rooms.add(0,"全部");
                NfcPopIndicatorAdapter adapter=new NfcPopIndicatorAdapter(getSupportFragmentManager());
                adapter.initAdpater(PeoplesInHouseActivity.this,nfcJsonBean,rooms);
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
