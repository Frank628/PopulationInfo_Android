package com.jinchao.population.alienPeople;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.mainmenu.SearchPeopleActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/1/22.
 */
@ContentView(R.layout.activity_idcard_result)
public class IDCardResultActivity extends BaseActiviy{
    @ViewInject(R.id.tv_content)TextView tv_content;
    @ViewInject(R.id.bottom)LinearLayout bottom;
    People peoplefromXml;
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
                peoplefromXml=XmlUtils.parseXMLtoPeople(result);
                if (XmlUtils.parseXMLIsBanzheng(result)){
                    bottom.setVisibility(View.VISIBLE);
                }else {
                    bottom.setVisibility(View.GONE);
                    Dialog.showRadioDialog(IDCardResultActivity.this, "此人未办理居住证，立即办理居住证！", new Dialog.DialogClickListener() {
                        @Override
                        public void confirm() {
                            Intent intent=new Intent(IDCardResultActivity.this, RegisterActivity.class);
                            intent.putExtra("fromotherway",true);
                            intent.putExtra("isHandle",true);
                            intent.putExtra("idcard",getIntent().getStringExtra("idcard"));
                            intent.putExtra("isSecond",getIntent().getIntExtra("isSecond",0));
                            intent.putExtra("people",getIntent().getSerializableExtra("people"));
                            intent.putExtra("pic",getIntent().getParcelableExtra("pic"));
                            startActivity(intent);
                            IDCardResultActivity.this.finish();
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
        intent.putExtra("pic",getIntent().getParcelableExtra("pic"));
        startActivity(intent);
        IDCardResultActivity.this.finish();
    }
    @Event(value={R.id.btn_unregist})
    private void zhuxiao(View view){
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date =sDateFormat.format(new java.util.Date());
        People people=new People(peoplefromXml.name, date, peoplefromXml.cardno, "注销", CommonUtils.GenerateGUID(), "2",
                MyInfomationManager.getUserName(IDCardResultActivity.this), peoplefromXml.homecode,peoplefromXml.ResidentAddress);
        DbUtils dbUtils = DeviceUtils.getDbUtils(this);
        List<People> list=new ArrayList<People>();
        try {
            list = dbUtils.findAll(Selector.from(People.class).where("cardno", "=", peoplefromXml.cardno));
            if (list!=null) {
                if (list.size()>0) {
                    dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", peoplefromXml.cardno));
                }
            }
            dbUtils.save(people);
            Toast.makeText(this, "注销成功~", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "注销失败~", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
