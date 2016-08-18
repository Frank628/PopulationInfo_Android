package com.jinchao.population.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.HouseAddressOldBean;
import com.jinchao.population.location.MyLocation;
import com.jinchao.population.main.MainActivity;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonHttp;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.SharePrefUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by OfferJiShu01 on 2016/8/9.
 */
@ContentView(R.layout.fragment_registloc)
public class RegistLocFragment extends BaseFragment{
    @ViewInject(R.id.mapview) MapView mapView;
    @ViewInject(R.id.tv_loc) TextView tv_loc;
    @ViewInject(R.id.ll_loc) LinearLayout ll_loc;
    @ViewInject(R.id.tv_zhan) TextView tv_zhan;
    @ViewInject(R.id.tv_latlng) TextView tv_latlng;
    @ViewInject(R.id.tv_address) TextView tv_address;
    @ViewInject(R.id.iv_house)  ImageView iv_house;
    @ViewInject(R.id.edt_houseid) EditText edt_houseid;
    @ViewInject(R.id.edt_content) EditText edt_content;
    @ViewInject(R.id.rl_top) RelativeLayout rl_top;
    @ViewInject(R.id.rl_bottom) RelativeLayout rl_bottom;
    private BaiduMap baiduMap;
    private GeoCoder mSearch;
    private boolean isZhan=false,hasTakePic=false;
    private int mHiddenViewMeasuredHeight;
    private LatLng mLatLng=null;
    public static final int HOUSE_PIC=2001;
    private File tempfile;
    private String houseid="",pic="";
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
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    tv_loc.setText("抱歉，未能找到结果");
                    return;
                }
                mLatLng=geoCodeResult.getLocation();
                tv_loc.setText(geoCodeResult.getAddress());
                tv_latlng.setText("经度："+geoCodeResult.getLocation().longitude+"\n纬度："+geoCodeResult.getLocation().latitude);
                baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(geoCodeResult.getLocation()));
            }
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
                mLatLng=baiduMap.getMapStatus().target;
                tv_latlng.setText("经度："+mLatLng.longitude+"\n纬度："+mLatLng.latitude);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mLatLng));
            }
        });
        ((MainActivity)getActivity()).tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caiJiRequest();
            }
        });
    }
    @Event(value = {R.id.tv_takepic})
    private void takepicClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempfile= CommonUtils.getTempImage();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempfile));
        startActivityForResult(intent,HOUSE_PIC);
    }
    @Event(value = {R.id.tv_searchbyaddress})
    private void checkHouselocClick(View view){
        if (tv_address.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(),"请先查询房屋地址！",Toast.LENGTH_SHORT).show();
            return;
        }
        boolean mSearchResult=mSearch.geocode(new GeoCodeOption().city("苏州").address(tv_address.getText().toString().trim()));
        if (!mSearchResult){
            Toast.makeText(getActivity(),"无法查到该房屋地址！",Toast.LENGTH_SHORT).show();
        }
    }
    @Event(value = {R.id.tv_search})
    private void checkHouseClick(View view){
        DbUtils dbUtils= DeviceUtils.getDbUtils(getActivity());
        HouseAddressOldBean houseAddressOldBean;
        String code =edt_houseid.getText().toString().trim();
        if (code.length()!=6){
            Toast.makeText(getActivity(),"请输入6位数房屋编号！",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            houseAddressOldBean = dbUtils.findFirst(Selector.from(HouseAddressOldBean.class).where("scode", "=", code));
            if (houseAddressOldBean!=null) {
                edt_content.setText(houseAddressOldBean.getAddress());
                tv_address.setText(houseAddressOldBean.getAddress().trim());
                houseid=houseAddressOldBean.getId();
            }else{
                edt_content.setText("查无此房屋");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
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
        mLatLng=baiduMap.getMapStatus().target;
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mLatLng));
    }
    private void caiJiRequest(){
        if (tempfile != null && tempfile.exists() &&hasTakePic) {
            Bitmap bitmap=CommonUtils.compressImage(tempfile.getAbsolutePath());
            byte[] img = CommonUtils.Bitmap2Bytes(bitmap);
            pic= new String(Base64Coder.encodeLines(img));
        }
        int databaseType = SharePrefUtil.getInt(getActivity(),Constants.DATABASE_TYPE,0);
        if (databaseType==0){
            Toast.makeText(getActivity(),"数据库类型未获取，请稍后！",Toast.LENGTH_SHORT).show();
            CommonHttp.getDataBaseType(getActivity());
            return;
        }
        if (TextUtils.isEmpty(edt_houseid.getText().toString().trim())){
            Toast.makeText(getActivity(),"请先输入房屋编号！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edt_content.getText().toString().trim())){
            Toast.makeText(getActivity(),"请先查询房屋地址！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (mLatLng==null){
            Toast.makeText(getActivity(),"请先确定房屋的经纬度坐标！",Toast.LENGTH_SHORT).show();
            return;
        }
        showProcessDialog("数据发送中，请稍等...");
        RequestParams params =new RequestParams(Constants.URL+"HousePosition.aspx");
        if (databaseType==1) {//1外来人口，2实有人口
            params.addBodyParameter("type", "put_wlrk");
            Log.i("type", "put_wlrk");
        }else {
            params.addBodyParameter("type", "put_syrk");
            Log.i("type", "put_syrk");
        }
        params.addBodyParameter("zp",pic);
        params.addBodyParameter("jd",mLatLng.longitude+"");
        params.addBodyParameter("wd",mLatLng.latitude+"");
        params.addBodyParameter("house_id",houseid);
        params.addBodyParameter("cjr", MyInfomationManager.getUserName(getActivity()));
        Log.i("house_id",houseid);
        Log.i("zp",pic);
        Log.i("jd",mLatLng.longitude+"");
        Log.i("wd",mLatLng.latitude+"");
        Log.i("cjr",MyInfomationManager.getUserName(getActivity()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("LOCOPERATION",result);
                if (result.trim().equals("1")){
                    tv_address.setText("");
                    edt_houseid.setText("");
                    edt_content.setText("");
                    hasTakePic=false;
                    houseid="";
                    pic="";
                    iv_house.setScaleType(ImageView.ScaleType.CENTER);
                    iv_house.setImageResource(R.drawable.camera_small);
                    Toast.makeText(getActivity(),"采集成功！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"采集失败，请联系我们！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("LOCOPERATION",ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideProcessDialog();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK) {
            switch (requestCode) {
                case HOUSE_PIC :
                    x.image().bind(iv_house,tempfile.getAbsolutePath(),new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_CROP).setUseMemCache(false).build());
                    hasTakePic=true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }).start();
                    break;
                default:
                    break;
            }
        }
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
        mSearch.destroy();
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
