package com.jinchao.population.realpopulation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.RealPeopleSearchBean;
import com.jinchao.population.entity.RealPeopleinHouseBean;
import com.jinchao.population.entity.SortbyRoomCodeClass;
import com.jinchao.population.entity.SortbyTimeClass;
import com.jinchao.population.entity.SortbyzaizhuClass;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.GsonTools;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/11/25.
 */

@org.xutils.view.annotation.ContentView(R.layout.activity_searchpeoplereal)
public class SearchPeopleRealActivity extends BaseReaderActiviy implements  IDReader.IDReaderListener{
    @ViewInject(R.id.lv) private ListView lv;
    @ViewInject(R.id.rg)private RadioGroup rg_sort;
    @ViewInject(R.id.edt_content) private EditText edt_content;
    @ViewInject(R.id.btn_seach) private TextView btn_seach;
    private CommonAdapter<RealPeopleinHouseBean.RealPeopleinHouseOne> adapter;
    private List<RealPeopleinHouseBean.RealPeopleinHouseOne> listreal=new ArrayList<RealPeopleinHouseBean.RealPeopleinHouseOne>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rg_sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (listreal.size()>0) {
                    switch (checkedId) {
                        case R.id.rb_time:
                            if (adapter!=null) {
                                Collections.sort(listreal, new SortbyTimeClass());
                                adapter.notifyDataSetChanged();
                                lv.setSelection(0);
                            }
                            break;
                        case R.id.rb_shihao:
                            if (adapter!=null) {
                                Collections.sort(listreal, new SortbyRoomCodeClass());
                                adapter.notifyDataSetChanged();
                                lv.setSelection(0);
                            }
                            break;
                        case R.id.rb_zaizhu:
                            if (adapter!=null) {
                                Collections.sort(listreal, new SortbyzaizhuClass());
                                adapter.notifyDataSetChanged();
                                lv.setSelection(0);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onReadCardSuccess(final PersonInfo personInfo) {
        try {
            link.disconnect();
        } catch (Exception e) {
            Log.e("readcard", e.getMessage());
        } finally {
            link = null;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProcessDialog();
//                showIDCardInfo(false,personInfo,null);
            }
        });

    }

    @Override
    public void onReadCardFailed(final String s) {
        try {
            link.disconnect();
        } catch (Exception e) {
            Log.e("readcard", e.getMessage());
        } finally {
            link = null;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProcessDialog();
                showError(s);
            }
        });

    }
    private void getPeopleinHouse(String house_id){
        showProcessDialog("数据加载中，请稍等...");
        RequestParams params=new RequestParams(Constants.URL+"syrkServer.aspx");
        params.addBodyParameter("type","peoplelist");
        params.addBodyParameter("house_id",house_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processPeoleinHouse(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {hideProcessDialog();}
        });
    }
    private void processPeoleinHouse(String json){
        try {
            RealPeopleSearchBean realPeopleinHouseBean = GsonTools.changeGsonToBean(json, RealPeopleSearchBean.class);
            listreal.clear();
            listreal.addAll(realPeopleinHouseBean.data);
            SortbyTimeClass sortbyTimeClass =new SortbyTimeClass();
            Collections.sort(listreal, sortbyTimeClass);
            adapter = new CommonAdapter<RealPeopleinHouseBean.RealPeopleinHouseOne>(SearchPeopleRealActivity.this,listreal,R.layout.item_realhousepopulation) {
                @Override
                public void convert(ViewHolder helper, RealPeopleinHouseBean.RealPeopleinHouseOne item, int position) {
                    String sQx = "在住";
                    if (item.status == -1){
                        sQx = "所内他处";
                    }else if (item.status == -2){
                        sQx = "区内所外";
                    }else if (item.status == -3){
                        sQx = "市内区外";
                    }else if (item.status == -4){
                        sQx = "市外";
                    }else if (item.status == -5){
                        sQx = "其他";
                    }

                    helper.setText(R.id.tv_top,item.sname.trim()+"     "+item.idcard+"     "+item.sex);
                    String[] split = item.chkudt.split("\\s+");
                    if (item.idcard.trim().substring(0, 4).equals("3205")||item.house_addr.trim().contains("苏州")||item.house_addr.trim().contains("昆山")||item.house_addr.trim().contains("吴江")||item.house_addr.trim().contains("张家港")||item.house_addr.trim().contains("太仓")||item.house_addr.trim().contains("常熟")) {
                        ((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
                        helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
                        helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
                        helper.setText(R.id.tv_time, item.chkudt);
                    }else{
                        if (split.length>1) {
                            if (isOneYearLater(split[0])) {
                                ((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#ff0000"));
                                helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
                                helper.setText(R.id.tv_time, Html.fromHtml( "<font color=\'#ff0000\'>"+item.chkudt+"</font>"));
                                helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#ff0000\'>室号："+item.roomCode+"</font>"));
                            }else{
                                ((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
                                helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
                                helper.setText(R.id.tv_time, item.chkudt);
                                helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
                            }
                        }else{
                            ((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
                            helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
                            helper.setText(R.id.tv_time, item.chkudt);
                            helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
                        }
                    }


                }
            };
            lv.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Event(value={R.id.btn_seach})
    private void seachClick(View view){
        if (!CommonUtils.isFangwuBianHao(edt_content.getText().toString().trim())){
            Toast.makeText(SearchPeopleRealActivity.this,"请输入6位的房屋编号！",Toast.LENGTH_SHORT).show();
        }
        getPeopleinHouse(edt_content.getText().toString().trim());
    }
    public static boolean isOneYearLater(String day) {
        String[] s=day.split("-");
        String dayf=s[0]+"-"+(s[1].length()==1?("0"+s[1]):s[1])+"-"+(s[2].length()==1?("0"+s[2]):s[2]);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(dayf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + (long)365 * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        int falg=dateOk.compareTo(simpleDateFormat.format(new Date()));
        if (falg>=0) {
            return false;
        }else{
            return true;
        }
    }
}
