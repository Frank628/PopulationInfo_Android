package com.jinchao.population;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;

import com.Common;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.dbentity.JLX2;
import com.jinchao.population.dbentity.JLX3;
import com.jinchao.population.dbentity.JLX4;
import com.jinchao.population.dbentity.JLX5;
import com.jinchao.population.entity.UserBean;
import com.jinchao.population.location.LocationService;
import com.jinchao.population.location.MyLocation;
import com.jinchao.population.main.LoginActivity;
import com.jinchao.population.main.SplashActivity;
import com.jinchao.population.utils.CrashHandler;
import com.jinchao.population.utils.GlobalPref;
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
    public boolean isSureDengji=false;
	public int database_tableNo=0;//当前登录账号使用的哪个数据库，0:未下载的地址库，1：表1,2：表2.。。。
	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        CrashHandler.getInstance().init(getApplicationContext());
		SDKInitializer.initialize(this);
		Common.init(this);//南京读卡初始化
		GlobalPref.init(this);//南京读卡sharedpre初始化
        getUSER();
		myApplication=this;
		locationService = new LocationService(getApplicationContext());
		locationService.registerListener(BDListener);
		locationService.setLocationOption(locationService.getDefaultLocationClientOption());
	}

	/**
	 * 设置当前登录账号使用的哪个数据库
	 * @param No
     */
	public void setDataBaseTableNo(int No){
		database_tableNo=No;
	}
	private void getUSER(){
		RequestParams params=new RequestParams(Constants.URL+"InitLoad.aspx");
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				try {
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
    public void setIsSureDengji(boolean is){
        this.isSureDengji=is;
    }
    public boolean getIsSureDengji(){
       return isSureDengji;
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
     