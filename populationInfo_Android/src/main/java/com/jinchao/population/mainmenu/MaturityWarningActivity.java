package com.jinchao.population.mainmenu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.MaturityListBean;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.BadgeView;
import com.jinchao.population.widget.LoadMoreListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by OfferJiShu01 on 2016/12/13.
 */
@ContentView(R.layout.activity_maturehouse)
public class MaturityWarningActivity extends BaseActiviy{
    @ViewInject(R.id.loadmorelv) private LoadMoreListView loadmorelv;
    @ViewInject(R.id.rotate_header_list_view_frame) private PtrClassicFrameLayout mPtrFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("到期人员所在房屋");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                GetMaturityWarning();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        //the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        loadmorelv.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
    }
    private void GetMaturityWarning() {
        RequestParams params = new RequestParams(Constants.URL + "HousePosition.aspx");
        params.addBodyParameter("type", "get_people");
        params.addBodyParameter("user_id", MyInfomationManager.getUserName(MaturityWarningActivity.this));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println(result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mPtrFrame.refreshComplete();
            }
        });
    }
    private void processData(String json){
        try {
            MaturityListBean maturityListBean= GsonTools.changeGsonToBean(json,MaturityListBean.class);
            if (maturityListBean.data.houselist!=null){
                if (maturityListBean.data.houselist.size()!=0){
                    loadmorelv.setTotalNum(maturityListBean.data.houselist.size());
                    CommonAdapter<MaturityListBean.MatureHouseOne> adapter =new CommonAdapter<MaturityListBean.MatureHouseOne>(MaturityWarningActivity.this,maturityListBean.data.houselist,R.layout.item_maturehouse) {
                        @Override
                        public void convert(ViewHolder helper, MaturityListBean.MatureHouseOne item, int position) {
                            helper.setText(R.id.tv_bianhao,item.house_code);
                            helper.setText(R.id.tv_address,item.house_addr);
                            BadgeView badgeView=new BadgeView(MaturityWarningActivity.this);
                            badgeView.setTargetView(helper.getView(R.id.rl_root));
                            badgeView.setTextSize(14);
                            badgeView.setBackground(9,Color.parseColor("#8a8a8a"));
                            badgeView.setBadgeCount(item.peoplelist.size());
                        }
                    };

                    loadmorelv.setAdapter(adapter);
                    loadmorelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MaturityListBean.MatureHouseOne aa=  (MaturityListBean.MatureHouseOne)((ListView)parent).getItemAtPosition(position);
//                            Toast.makeText(MaturityWarningActivity.this,aa.house_addr,Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(MaturityWarningActivity.this,MaturityPeopleActivity.class);
                            intent.putExtra("list",aa);
                            startActivity(intent);
                        }
                    });
                }else{

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
