package com.jinchao.population;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.UserBean;
import com.jinchao.population.location.LocationService;
import com.jinchao.population.location.MyLocation;
import com.jinchao.population.main.LoginActivity;
import com.jinchao.population.main.SplashActivity;
import com.jinchao.population.utils.CrashHandler;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.Dialog.DialogClickListener;


import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class MyApplication extends Application{
	public static MyApplication myApplication=null;
	public  LocationService locationService;
	public MyLocation myLocation=new MyLocation();
	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
//        CrashHandler.getInstance().init(getApplicationContext());
		SDKInitializer.initialize(this);
        getUSER();
		myApplication=this;
		locationService = new LocationService(getApplicationContext());
		locationService.registerListener(BDListener);
		locationService.setLocationOption(locationService.getDefaultLocationClientOption());
	}

	private void getUSER(){
		RequestParams params=new RequestParams(Constants.URL+"InitLoad.aspx");
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				try {
					UserBean userBean =GsonTools.changeGsonToBean(result.trim(), UserBean.class);
					SharePrefUtil.saveString(getApplicationContext(), Constants.USER_DB, result.trim());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {}
		});
	}
	private BDLocationListener BDListener=new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				myLocation.setCity(location.getCity()==null?"":location.getCity());
				myLocation.setAddress(location.getAddrStr()==null?"":location.getAddrStr());
				myLocation.setStreet(location.getStreet()==null?"":location.getStreet());
				myLocation.setProvince(location.getProvince()==null?"":location.getProvince());
				myLocation.setLat(location.getLatitude());
				myLocation.setLog(location.getLongitude());
				Log.i("Location",location.getAddrStr());
				EventBus.getDefault().post(location);
				locationService.stop();
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				} else if (location.getLocType() == BDLocation.TypeServerError) {
              //	 	服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//						网络不同导致定位失败，请检查网络是否通畅
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//						无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
				}
			}
		}
	};
	
}
     