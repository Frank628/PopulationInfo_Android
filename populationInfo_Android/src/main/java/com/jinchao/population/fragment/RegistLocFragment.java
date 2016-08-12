package com.jinchao.population.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jinchao.population.MyApplication;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.location.MyLocation;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * Created by OfferJiShu01 on 2016/8/9.
 */
@ContentView(R.layout.fragment_registloc)
public class RegistLocFragment extends BaseFragment {
    @ViewInject(R.id.mapview) MapView mapView;
    @ViewInject(R.id.tv_loc) TextView tv_loc;
    @ViewInject(R.id.ll_loc) LinearLayout ll_loc;
    @ViewInject(R.id.tv_zhan) TextView tv_zhan;
    @ViewInject(R.id.tv_latlng) TextView tv_latlng;
    @ViewInject(R.id.rl_top) RelativeLayout rl_top;
    @ViewInject(R.id.rl_bottom) RelativeLayout rl_bottom;
    private BaiduMap baiduMap;
    private GeoCoder mSearch;
    private boolean isZhan=false;
    private int mHiddenViewMeasuredHeight;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        rl_top.measure(w,h);
        mHiddenViewMeasuredHeight=rl_top.getMeasuredHeight();
        mSearch= GeoCoder.newInstance();
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        MyApplication.myApplication.locationService.start();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {}
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != ReverseGeoCodeResult.ERRORNO.NO_ERROR) {
                    tv_loc.setText("抱歉，未能找到结果");
                    return;
                }
                tv_loc.setText(reverseGeoCodeResult.getAddress());
            }
        });
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                ll_loc.setVisibility(View.GONE);
            }
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {}
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                ll_loc.setVisibility(View.VISIBLE);
                LatLng latLng=baiduMap.getMapStatus().target;
                tv_latlng.setText(latLng.latitude+":"+latLng.longitude);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
            }
        });
    }
    @Event(value = {R.id.rl_zhan})
    private void zhanClick(View view){
        if (!isZhan){
            isZhan=true;
            animationClose(rl_top);
            tv_zhan.setText("收起地图");
            tv_zhan.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_shrinkup2),null,null,null);
        }else{
            isZhan=false;
            animationOpen(rl_top);
            tv_zhan.setText("展开地图");
            tv_zhan.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_shrink2_tint),null,null,null);
        }
    }
    @Event(value = {R.id.iv_requestloc})
    private void requestLocClick(View view){
        MyApplication.myApplication.locationService.start();

    }
    public void onEventMainThread(BDLocation location) {
        if (location == null || mapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        baiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        ll_loc.setVisibility(View.VISIBLE);
        LatLng latLng=baiduMap.getMapStatus().target;
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView=null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void animationClose(final View view){
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    public void animationOpen(View v){
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,mHiddenViewMeasuredHeight);
        animator.start();
    }
    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
