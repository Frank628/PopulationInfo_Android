package com.jinchao.population.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.NavigationLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/8/18.
 */
@ContentView(R.layout.activity_personinhouse)
public class PersonInHouseActivity extends BaseActiviy {
    private RenyuanInHouseBean renyuanInHouseBean;
    @ViewInject(R.id.rg_zai) private RadioGroup rg_zai;
    @ViewInject(R.id.rb_banli) private RadioButton rb_banli;
    @ViewInject(R.id.rb_zaizhu) private RadioButton rb_zaizhu;
    @ViewInject(R.id.tv_content) private TextView tv_content;
    @ViewInject(R.id.lv) private ListView lv;
    private boolean isZaizhu=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("房屋内人员信息");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String houseid=getIntent().getStringExtra("id");
        if (houseid!=null){
            getRequest(houseid);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (renyuanInHouseBean!=null) {
            processData(renyuanInHouseBean);
        }
    }
    private void getRequest(String str){
        RequestParams params=new RequestParams(Constants.URL+"HousePosition.aspx?type=get_houseinfor&user_id="+ MyInfomationManager.getUserName(this)+"&house_code="+str);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
            try {
                 renyuanInHouseBean= GsonTools.changeGsonToBean(result,RenyuanInHouseBean.class);
                 processData(renyuanInHouseBean);
             } catch (Exception e) {
                e.printStackTrace();
             }

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {}
        });
    }

    private void processData(RenyuanInHouseBean renyuanInHouseBean){
        try {
            if (renyuanInHouseBean.data.house_exist.equals("0")){
                tv_content.setVisibility(View.VISIBLE);
                tv_content.setText("此房屋编号不存在！");
                return;
            }
            if (renyuanInHouseBean.data.people_exist.equals("0")){
                tv_content.setVisibility(View.VISIBLE);
                tv_content.setText("此房屋没有采集过人！");
                return;
            }
            tv_content.setVisibility(View.GONE);
            rg_zai.setVisibility(View.VISIBLE);
            List<RenyuanInHouseBean.RenyuanInhouseOne> list=new ArrayList<RenyuanInHouseBean.RenyuanInhouseOne>();
            list.clear();
            if (isZaizhu) {
                for (int i = 0; i <renyuanInHouseBean.data.peoplelist.size(); i++) {
                    if (renyuanInHouseBean.data.peoplelist.get(i).resdients_status.equals("在住")) {
                        list.add(renyuanInHouseBean.data.peoplelist.get(i));
                    }
                }
                rb_zaizhu.setText("在住("+list.size()+"人)");
                rb_banli.setText("搬离("+(renyuanInHouseBean.data.peoplelist.size()-list.size())+"人)");
            }else{
                for (int i = 0; i <renyuanInHouseBean.data.peoplelist.size(); i++) {
                    if (renyuanInHouseBean.data.peoplelist.get(i).resdients_status.equals("搬离")) {
                        list.add(renyuanInHouseBean.data.peoplelist.get(i));
                    }
                }
                rb_banli.setText("搬离("+list.size()+"人)");
                rb_zaizhu.setText("在住("+(renyuanInHouseBean.data.peoplelist.size()-list.size())+"人)");
            }

            CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne> adapter =new CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne>(PersonInHouseActivity.this,list,R.layout.item_renyuan) {
                @Override
                public void convert(ViewHolder helper, RenyuanInHouseBean.RenyuanInhouseOne item, int position) {
                    helper.setText(R.id.tv_name,"姓名: "+ item.sname);
                    DbUtils dbUtils = DeviceUtils.getDbUtils(PersonInHouseActivity.this);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (falg12>=0&&falg11<0) {
            return true;
        }else{
            return false;
        }

    }
}
