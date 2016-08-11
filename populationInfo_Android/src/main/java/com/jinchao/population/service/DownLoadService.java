package com.jinchao.population.service;

import java.io.File;

import com.jinchao.population.config.Constants;
import com.jinchao.population.utils.SharePrefUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

public class DownLoadService extends Service{
	private String url="";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onStart(Intent intent, int startId) {
		if (intent!=null) {
			url=intent.getStringExtra("url");
		}else{
			url=SharePrefUtil.getString(this, "url", "");
		}
		download(url);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags=START_REDELIVER_INTENT;
		return super.onStartCommand(intent, flags, startId);
	}
	private void download(String url){
		HttpUtils http =new HttpUtils();
		http.download(url.trim(), Constants.DB_PATH+"PopulationInfo_Android.apk", false, true, new RequestCallBack<File>() {
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW);  
		        intent.setDataAndType(Uri.fromFile(new File(Environment  
		                .getExternalStorageDirectory(), "PopulationInfo_Android.apk")),  
		                "application/vnd.android.package-archive");  
		        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        DownLoadService.this.startActivity(intent);
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
	}
}
