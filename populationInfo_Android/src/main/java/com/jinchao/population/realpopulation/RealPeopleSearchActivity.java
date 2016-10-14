package com.jinchao.population.realpopulation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.RealHouseBean;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.view.NavigationLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by OfferJiShu01 on 2016/10/13.
 */
@ContentView(R.layout.activity_realpeoplesearchpopulation)
public class RealPeopleSearchActivity extends BaseActiviy{
    @ViewInject(R.id.lv)private ListView lv;
    @ViewInject(R.id.edt_content)private EditText edt_content;
    @ViewInject(R.id.navgation_top)private NavigationLayout navigationLayout;
    @ViewInject(R.id.rotate_header_list_view_frame)private PtrClassicFrameLayout mPtrFrame;
    private boolean isGetAll=true;
    private View footView;
    private TextView tv_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationLayout.setCenterText("实有人口信息采集");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        footView = (TextView) LayoutInflater.from(RealPeopleSearchActivity.this).inflate(R.layout.tv_error, null);
        tv_error = (TextView) footView.findViewById(R.id.tv_error);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (isGetAll) {
                    getHouse();
                }else{
                    String content=edt_content.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(RealPeopleSearchActivity.this, "请输入查询内容！", Toast.LENGTH_SHORT).show();
                        mPtrFrame.refreshComplete();
                        isGetAll=true;
                        return;
                    }
                    getSearchHouse(content);
                }
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
    }

    private void getHouse(){
        lv.setAdapter(null);
        lv.removeFooterView(footView);
        RequestParams params=new RequestParams(Constants.URL+"syrkServer.aspx");
        params.addBodyParameter("type", "loadPage");
        params.addBodyParameter("c_id", MyInfomationManager.getUserID(RealPeopleSearchActivity.this));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result,false);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {mPtrFrame.refreshComplete();}
        });
    }
    private void processData(String json,boolean isSearch){
        try {
            RealHouseBean realHouseBean = GsonTools.changeGsonToBean(json, RealHouseBean.class);
            if (realHouseBean.data.size()==0) {
                if (isSearch) {
                    tv_error.setText("没有符合搜索条件的房屋！");
                }else{
                    tv_error.setText("暂无数据！");
                }
                lv.addFooterView(footView);

            }
            CommonAdapter<RealHouseBean.RealHouseOne> adapter =new CommonAdapter<RealHouseBean.RealHouseOne>(RealPeopleSearchActivity.this,realHouseBean.data,R.layout.item_realpopulation) {
                @Override
                public void convert(ViewHolder helper, RealHouseBean.RealHouseOne item, int position) {
                    helper.setText(R.id.tv_bianhao,"房屋编号 \n"+ item.scode);
                    helper.setText(R.id.tv_address, item.houseAdress);
                }
            };
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                    if ( (RealHouseBean.RealHouseOne)((ListView)arg0).getItemAtPosition(arg2)==null) {
                        return;
                    }
                    Intent intent =new Intent(RealPeopleSearchActivity.this, RegisterActivity.class);
                    intent.putExtra("isreal", true);
                    intent.putExtra("realhouseone", (RealHouseBean.RealHouseOne)((ListView)arg0).getItemAtPosition(arg2));
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Event(value={R.id.btn_seach})
    private void searchClick(View view){
        isGetAll=false;
        mPtrFrame.autoRefresh();
    }
    public boolean isCode(String str){
        String regEx="^[A-Za-z0-9_]+$";
        Pattern p=Pattern.compile(regEx);
        Matcher m=p.matcher(str);
        return m.matches();
    }
    private void getSearchHouse(String content){
        lv.setAdapter(null);
        lv.removeFooterView(footView);
        RequestParams params=new RequestParams(Constants.URL+"syrkServer.aspx");
        Log.i("scode",isCode(content)+"");
        if(isCode(content)){
            params.addBodyParameter("type", "houseByScode");
            params.addBodyParameter("scode", content);
            params.addBodyParameter("c_id",  MyInfomationManager.getUserID(RealPeopleSearchActivity.this));
            Log.i("scode",content);
        }else{
            params.addBodyParameter("type", "houseByAdr");
            params.addBodyParameter("sub_length", content.length()+"");
            params.addBodyParameter("adr", content);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                processData(result,true);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {mPtrFrame.refreshComplete();}
        });
    }

}
