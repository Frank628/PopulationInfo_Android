package com.jinchao.population.mainmenu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.MaturityListBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.BadgeView;
import com.jinchao.population.widget.LoadMoreListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by OfferJiShu01 on 2016/12/13.
 */
@ContentView(R.layout.activity_maturehouse)
public class MaturityPeopleActivity extends BaseActiviy{
    @ViewInject(R.id.loadmorelv) private LoadMoreListView loadmorelv;
    @ViewInject(R.id.rotate_header_list_view_frame) private PtrClassicFrameLayout mPtrFrame;
    private MaturityListBean.MatureHouseOne matureHouseOne;
    private boolean  isfromreal=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("到期人员");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        isfromreal=getIntent().getBooleanExtra(Constants.IS_FROM_REALPOPULATION,false);
        matureHouseOne=(MaturityListBean.MatureHouseOne)getIntent().getSerializableExtra("list");
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                processData(matureHouseOne);
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

    @Override
    protected void onResume() {
        super.onResume();
        processData(matureHouseOne);
    }

    private void processData(MaturityListBean.MatureHouseOne matureHouseOne){
                    mPtrFrame.refreshComplete();
                    loadmorelv.setTotalNum(matureHouseOne.peoplelist.size());
                    CommonAdapter<MaturityListBean.MaturePeopleOne> adapter =new CommonAdapter<MaturityListBean.MaturePeopleOne>(MaturityPeopleActivity.this,matureHouseOne.peoplelist,R.layout.item_maturepeople) {
                        @Override
                        public void convert(ViewHolder helper, MaturityListBean.MaturePeopleOne item, int position) {
                            helper.setText(R.id.tv_idcard,item.idcard);
                            helper.setText(R.id.tv_shihao,item.shihao);
                            helper.setText(R.id.tv_name,item.sname);
                            helper.setText(R.id.tv_time,item.write_time);
                            DbUtils dbUtils = DeviceUtils.getDbUtils(MaturityPeopleActivity.this);
                            People people = null;
                            try {
                                people=dbUtils.findFirst(Selector.from(People.class).where("cardno", "=", item.idcard.trim()));
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            if (people!=null){
                                helper.setText(R.id.tv_idcard,item.idcard+"【"+people.module+"】");
                            }
                        }
                    };

                    loadmorelv.setAdapter(adapter);
                loadmorelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (isfromreal){
                            Toast.makeText(MaturityPeopleActivity.this,"实有人口暂时只可查看,不可操作！",Toast.LENGTH_SHORT).show();
                        }else{
                            MaturityListBean.MaturePeopleOne item=(MaturityListBean.MaturePeopleOne)((ListView)parent).getItemAtPosition(position);
                            Intent intent =new Intent(MaturityPeopleActivity.this, SearchPeopleDetailActivity.class);
                            RenyuanInHouseBean.RenyuanInhouseOne renyuanInHouseone=new RenyuanInHouseBean.RenyuanInhouseOne(item.write_time, item.house_addr, item.house_code, item.idcard, item.resdients_status, item.shihao, item.sname);
                            intent.putExtra("renyuan", renyuanInHouseone);
                            intent.putExtra("phone",item.tel);
                            startActivity(intent);
                        }

                    }
                });

            }

}
