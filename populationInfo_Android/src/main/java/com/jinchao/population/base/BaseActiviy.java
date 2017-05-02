package com.jinchao.population.base;

import org.xutils.x;
import com.jinchao.population.SysApplication;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.utils.network.NetWorkManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

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
			try {
				runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActiviy.this, AlertDialog.THEME_HOLO_LIGHT);
                        builder.setMessage("当前网络不给力，请检查网络！");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.create().show();
                    }
                });
			} catch (Exception e) {
				e.printStackTrace();
			}

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
	public void showSoftKeyBoard(View view){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);

	}
	public void hidenSoftKeyBoard(View view){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
