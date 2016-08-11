package com.jinchao.population.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jinchao.population.MyApplication;



import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler instance;
	private Context mContext;
	private Map<String, String> infos = new HashMap<String, String>();
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private CrashHandler() {
	}
	public static CrashHandler getInstance() {
		if (instance == null)
			instance = new CrashHandler();
		return instance;
	}
	/**
	 * 初始化
	 */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			//退出程序    
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		collectDeviceInfo(mContext);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
//				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		//保存日志文件     
		saveCatchInfo2File(ex);
		return true;
	}
	/**
	 * 收集设备参数信息
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}
	private String getFilePath() {
		String file_dir = "";
		// SD卡是否存在
		boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		// Environment.getExternalStorageDirectory()相当于File file=new File("/sdcard")
		boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();
		if (isSDCardExist && isRootDirExist) {
			file_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crashlog/";
		} else {
			// MyApplication.getInstance().getFilesDir()返回的路劲为/data/data/PACKAGE_NAME/files，其中的包就是我们建立的主Activity所在的包
			file_dir = MyApplication.myApplication.getFilesDir().getAbsolutePath() + "/crashlog/";
		}
		return file_dir;
	}
	private String saveCatchInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			String file_dir = getFilePath();
			//			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File dir = new File(file_dir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(file_dir + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			//发送给开发人员
			sendCrashLog2PM(file_dir + fileName);
			fos.close();
			//			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
	private void sendCrashLog2PM(String fileName) {
		//		if (!new File(fileName).exists()) {
		//			Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
		//			return;
		//		}
		//		FileInputStream fis = null;
		//		BufferedReader reader = null;
		//		String s = null;
		//		try {
		//			fis = new FileInputStream(fileName);
		//			reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
		//			while (true) {
		//				s = reader.readLine();
		//				if (s == null)
		//					break;
		//				Log.i("info", s.toString());
		//			}
		//		} catch (FileNotFoundException e) {
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		} finally { // 关闭流
		//			try {
		//				reader.close();
		//				fis.close();
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			}
		//		}
	}
}