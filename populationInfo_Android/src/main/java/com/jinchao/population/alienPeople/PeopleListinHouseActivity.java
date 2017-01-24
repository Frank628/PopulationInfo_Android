package com.jinchao.population.alienPeople;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.mainmenu.SearchPeopleActivity;
import com.jinchao.population.mainmenu.SearchPeopleDetailActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/1/22.
 */
@ContentView(R.layout.activity_peoplebyhousecode)
public class PeopleListinHouseActivity extends BaseActiviy{
    @ViewInject(R.id.lv)ListView lv;
    @ViewInject(R.id.tv_content)TextView tv_content;
    @ViewInject(R.id.edt_idcard)EditText edt_idcard;
    private RenyuanInHouseBean renyuanInHouseBean;
    People peoplefromXml;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("人员列表");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getData(getIntent().getStringExtra("housecode"));
        edt_idcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (renyuanInHouseBean!=null) {
                    processData(renyuanInHouseBean);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (renyuanInHouseBean!=null) {
            processData(renyuanInHouseBean);
        }
    }

    private void getData(String housecode){
        showProgressDialog("","正在加载，请稍等···");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=peopleList&sqdm="+MyInfomationManager.getSQCODE(PeopleListinHouseActivity.this)+"&scode="+housecode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("tag",result);
                renyuanInHouseBean= GsonTools.changeGsonToBean(result,RenyuanInHouseBean.class);
                processData(renyuanInHouseBean);
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
    private void processData(RenyuanInHouseBean renyuanInHouseBean){
        if (renyuanInHouseBean.data.house_exist.equals("0")){
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText("无人居住房屋或无此房屋编号！");
            return;
        }
        if (renyuanInHouseBean.data.people_exist.equals("0")){
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText("无人居住房屋或无此房屋编号！");
            return;
        }
        List<RenyuanInHouseBean.RenyuanInhouseOne> list=new ArrayList<>();
        String str=edt_idcard.getText().toString().trim();
        for (int i=0;i<renyuanInHouseBean.data.peoplelist.size();i++){
            if (renyuanInHouseBean.data.peoplelist.get(i).idcard.contains(str)){
                list.add(renyuanInHouseBean.data.peoplelist.get(i));
            }
        }
        if ((!TextUtils.isEmpty(str))&&list.size()==0){
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText("没有符合搜索条件的人员~");
        }else{
            tv_content.setVisibility(View.GONE);
            tv_content.setText("");
        }
        CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne> adapter =new CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne>(PeopleListinHouseActivity.this,list,R.layout.item_renyuan) {
            @Override
            public void convert(ViewHolder helper, RenyuanInHouseBean.RenyuanInhouseOne item, int position) {
                helper.setText(R.id.tv_name,"姓名: "+ item.sname);
                DbUtils dbUtils =DeviceUtils.getDbUtils(PeopleListinHouseActivity.this);
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
                            if (isBigerthanElevenandSmallthanTwelve(split[0])) {
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
                        if (isBigerthanElevenandSmallthanTwelve(split[0])) {
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RenyuanInHouseBean.RenyuanInhouseOne renyuanInHouseone=(RenyuanInHouseBean.RenyuanInhouseOne) ((ListView)parent).getItemAtPosition(position);
                Intent intent =new Intent(PeopleListinHouseActivity.this, SearchPeopleDetailActivity.class);
                intent.putExtra("renyuan", renyuanInHouseone);
                startActivity(intent);
            }
        });
    }
    public static boolean isBigerthanElevenandSmallthanTwelve(String day) {
        String[] s=day.split("-");
        String dayf=s[0]+"-"+(s[1].length()==1?("0"+s[1]):s[1])+"-"+(s[2].length()==1?("0"+s[2]):s[2]);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(dayf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date newDate12 = new Date(nowDate.getTime() + (long)365 * 24 * 60 * 60 * 1000);
        Date newDate11 = new Date(nowDate.getTime() + (long)334 * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date12 = simpleDateFormat.format(newDate12);
        String date11 = simpleDateFormat.format(newDate11);
        int falg12=date12.compareTo(simpleDateFormat.format(new Date()));
        int falg11=date11.compareTo(simpleDateFormat.format(new Date()));
        if (falg11<0) {
            return true;
        }else{
            return false;
        }

    }
}
