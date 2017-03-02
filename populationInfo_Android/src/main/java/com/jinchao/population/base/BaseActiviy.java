package com.jinchao.population.base;

import org.xutils.x;
import com.jinchao.population.SysApplication;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.utils.network.NetWorkManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

public class BaseActiviy extends FragmentActivity implements NetWorkManager.NetConnectChangeListener{
	public ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		NetWorkManager.getInstance().regist(this);
		SysApplication.getInstance().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void change(NetWorkManager.NetWorkInfo netWorkInfo) {
		if (!netWorkInfo.isNetAvailable){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(BaseActiviy.this, AlertDialog.THEME_HOLO_LIGHT);
					builder.setMessage("当前网络不给力，请检查网络！");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.create().show();
				}
			});

		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		NetWorkManager.getInstance().unregist(this);
	}
	public void showProgressDialog(String title,String toast){
		progressDialog = ProgressDialog.show(this,title,toast,true,false);
		progressDialog.show();
	}
	public void dismissProgressDialog(){
		if (progressDialog!=null){
			progressDialog.dismiss();
		}
	}

}
