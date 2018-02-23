package com.jinchao.population.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.Version;
import com.jinchao.population.entity.DeviceBean;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.TelephonyInfo;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class SplashActivity extends BaseActiviy{
	private final int BUFFER_SIZE = 10000;
	private boolean isServiceOk=false,isDeviceOk=false,IsServiceOver=false,IsDeviceOver=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		TelephonyInfo ti = TelephonyInfo.getInstance();
		ti.initConfig(this);
		Log.i("Phone",ti.getImeiSIM1());
		if (isNeedDB()) {//判断是否需要导入新的数据库
			Dialog.showSelectDialog(SplashActivity.this, "户籍地址库更新，请先上传所有待发送数据！", new DialogClickListener() {
				@Override
				public void confirm() {
					new Thread(new Runnable() {
						@Override
						public void run() {
							importDB();
						}
					}).start();
				}

				@Override
				public void cancel() {
					initalData();
				}
			},"已上传全部数据","尚未上传全部数据");

		}else{
			initalData();
		}
	}
	private boolean isNeedDB(){
		File file =new File(Constants.DB_PATH+Constants.DB_NAME);
		if (file.exists()) {
            Version version=null;
            try {
                DbUtils dbUtils=DeviceUtils.getDbUtils(this);
                version=dbUtils.findFirst(Version.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (version==null){
                return true;
            }
			if (!version.getVersion_current().trim().equals("5")){//20180122日户籍地址库编号
				return true;
			}
			return false;
		}else{
			return true;
		}
	}
	private void initalData(){
		RequestParams params=new RequestParams(Constants.URL+"ServiceConn.aspx");
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (result.equals("0")) {
					checkDevice();
				}else{
					checkit("无法连接到服务器~");
				}

			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				initalData();
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {}
		});
	}

	private void checkDevice(){
		RequestParams params=new RequestParams(Constants.URL+"Lisence.aspx?type=get_lisence&ch="+ CommonUtils.getIMEI(SplashActivity.this)+"&mobile=0");
//		RequestParams params=new RequestParams("http://222.92.144.66:91/population/Lisence.aspx?type=get_lisence&ch=20000&mobile=13865433212");
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.i("isHEFASHEBEI",result);
//				try {
//					DeviceBean deviceBean = GsonTools.changeGsonToBean(result,DeviceBean.class);
//					if (deviceBean.data.Result.equals("1")){
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				if (result.equals("1")) {
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}else if (result.trim().equals("0")){
					checkit("非法设备，无法使用此应用~");
				}else{
					checkDevice();
				}
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				initalData();
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {}
		});
	}

	private void checkit(String str) {
			Dialog.showSelectDialog(SplashActivity.this, str, new DialogClickListener() {
				@Override
				public void confirm() {
					SplashActivity.this.finish();
				}

				@Override
				public void cancel() {
					SplashActivity.this.finish();
				}
			});
	}
	/**
	 * 导入数据库
	 */
	public void importDB(){
	      if (DeviceUtils.hasSDCard()) {
	    	  Log.d("AAA", "SD READY");
	        try {
	                InputStream is =getResources().getAssets().open(Constants.DB_NAME); //欲导入的数据库
	                FileOutputStream fos = new FileOutputStream(Constants.DB_PATH+Constants.DB_NAME);
	                byte[] buffer = new byte[BUFFER_SIZE];
	                int count = 0;
	                while ((count = is.read(buffer)) > 0) {
	                    fos.write(buffer, 0, count);
	                }              
	                fos.close();//关闭输出流
	                is.close();//关闭输入流
	                initalData();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	      }else{
	    	  Dialog.showSelectDialog(SplashActivity.this, "无法检测到SD，请插入SD卡", new DialogClickListener() {
				@Override
				public void confirm() {
					SplashActivity.this.finish();
				}
				@Override
				public void cancel() {
					SplashActivity.this.finish();
				}
			});
	      }
	}
}
