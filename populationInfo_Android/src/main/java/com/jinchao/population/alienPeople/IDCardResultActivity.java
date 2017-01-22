package com.jinchao.population.alienPeople;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2017/1/22.
 */
@ContentView(R.layout.activity_idcard_result)
public class IDCardResultActivity extends BaseActiviy{
    @ViewInject(R.id.tv_content)TextView tv_content;
    @ViewInject(R.id.bottom)LinearLayout bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("人员查询结果");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String idcard=getIntent().getStringExtra("idcard");
        requestYanZheng(idcard);
    }

    private void requestYanZheng(final String idcard){
        showProgressDialog("","正在查询，请稍等...");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=get_people&idcard="+idcard);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                requestBanzhengSHEQU(idcard, XmlUtils.parseXML(result));
                if (XmlUtils.parseXMLIsBanzheng(result)){
                    bottom.setVisibility(View.VISIBLE);
                }else {
                    bottom.setVisibility(View.GONE);
                    Dialog.showRadioDialog(IDCardResultActivity.this, "此人未办理居住证，立即办理居住证！", new Dialog.DialogClickListener() {
                        @Override
                        public void confirm() {
                            Intent intent=new Intent(IDCardResultActivity.this, RegisterActivity.class);
                            intent.putExtra("fromotherway",true);
                            intent.putExtra("idcard",getIntent().getStringExtra("idcard"));
                            intent.putExtra("isSecond",getIntent().getIntExtra("isSecond",0));
                            intent.putExtra("people",getIntent().getSerializableExtra("people"));
                            startActivity(intent);
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {
            }
        });
    }
    private void requestBanzhengSHEQU(String idcard,final String str){
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=get_jzzStatus&idcard="+idcard);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("quePeople", result);
                tv_content.setText(str.replace("初次办证社区:",XmlUtils.parseBanZhengSheQuXML(result)));

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
    @Event(value={R.id.btn_change})
    private void changeClick(View view){
        Intent intent =new Intent(this,RegisterActivity.class);
        intent.putExtra("fromotherway",true);
        intent.putExtra("idcard",getIntent().getStringExtra("idcard"));
        intent.putExtra("isSecond",getIntent().getIntExtra("isSecond",0));
        intent.putExtra("people",getIntent().getSerializableExtra("people"));
        startActivity(intent);
    }
}
