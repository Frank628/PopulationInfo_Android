package com.jinchao.population.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefRecord;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class CommonUtils {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	public static File getCompareTempImage() {
		return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "compare.jpg");
	}
	public static byte[] getByte(File file) {
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		return Bitmap2Bytes(bm, Bitmap.CompressFormat.JPEG, 50);
	}
	public static byte[] Bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(format, quality, baos);
		return baos.toByteArray();
	}
	public static NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
		byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
		byte[] textBytes = text.getBytes(utfEncoding);

		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);

		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
	}
	public static Bitmap base64ToBitmap(String base64Data) {  
	    byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
	} 
	public static boolean isEmpty(String str){
		if (str==null) {
			return true;
		}
		if (str.equals(""))
			return true;
		else
			return false;
	}
	public static final String GenerateGUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString(); 
	}
	public static boolean isAddress(String str){
		Pattern p = Pattern.compile("^([\\u4e00-\\u9fa5]|[a-zA-Z0-9]|[-]|[\\(]|[\\)])+$");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	public static boolean isFangwuBianHao(String str){
			Pattern p = Pattern.compile("[A-Za-z0-9]{6}");
			Matcher m = p.matcher(str);
			return m.matches();
	}

	public static boolean checkChar(char oneChar){
		if((oneChar >= '\u4e00' && oneChar <= '\u9fa5')||(oneChar >= '\uf900' && oneChar <='\ufa2d'))
			return true;
		else
			return false;
	}
	public static boolean isChinese(String str){
		for(int i = 0 ;i<str.length();i++) {
			char ch = str.charAt(i);
			if (!checkChar(ch)) {
				return false;
			}
		}
		return true;
	}
	public static boolean isSBBH(String str){//社保卡编号
		int length=0;
		for(int i = 0 ;i<str.length();i++) {
			char ch = str.charAt(i);
			if (checkChar(ch)) {
				length=length+2;
			}else{
				length=length+1;
			}
		}
		if (length>18)
			return false;
		else
			return true;
	}
	public static boolean isJKZBH(String str){//健康证编号
		int length=0;
		for(int i = 0 ;i<str.length();i++) {
			char ch = str.charAt(i);
			if (checkChar(ch)) {
				length=length+2;
			}else{
				length=length+1;
			}
		}
		if (length>10)
			return false;
		else
			return true;
	}

    public static boolean isEmail(String mail) {
            Pattern p = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
            Matcher m = p.matcher(mail);
            return m.matches();
    }
	public static boolean isMobile(String mobiles) {
		if (mobiles.length()==11) {
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,0-9])|(17[0,0-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			return m.matches();
		}else{
			return false;
		}
	}

	public static boolean isTEL(String mobiles) {
		if (mobiles.length()==11) {

			return true;
		}else{
			Pattern p = Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
			Matcher m = p.matcher(mobiles);
			return m.matches();
		}
	}
    public static boolean isGuangdaTel(String mobiles) {
        if (mobiles.length()==8||mobiles.length()==11||mobiles.length()==12) {
            if (mobiles.equals("00000000")||mobiles.equals("00000000000")||mobiles.equals("000000000000"))
                return false;
            else
                return true;
        }else{
            return false;
        }
    }
	public static boolean isTrueHigh(String high) {
		if (TextUtils.isEmpty(high)){
			return false;
		}else{
			if (isNumeric(high)) {
				int highInt=Integer.parseInt(high);
				if (highInt>250||highInt<30) {
					return false;
				}else{
					return true;
				}
			}else{
				return false;
			}
		}
	}
	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	//版本名
	public static String getVersionName(Context context) {
	    return getPackageInfo(context).versionName;
	}
	 
	//版本号
	public static int getVersionCode(Context context) {
	    return getPackageInfo(context).versionCode;
	}
	 
	private static PackageInfo getPackageInfo(Context context) {
	    PackageInfo pi = null;
	    try {
	        PackageManager pm = context.getPackageManager();
	        pi = pm.getPackageInfo(context.getPackageName(),
	                PackageManager.GET_CONFIGURATIONS);
	        return pi;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return pi;
	}
	 public static boolean isServiceRunning(Context mContext,String className) {
         boolean isRunning = false;
			ActivityManager activityManager = (ActivityManager)
			mContext.getSystemService(Context.ACTIVITY_SERVICE); 
         List<ActivityManager.RunningServiceInfo> serviceList 
         = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0)) {
             return false;
         }
         for (int i=0; i<serviceList.size(); i++) {
             if (serviceList.get(i).service.getClassName().equals(className) == true) {
                 isRunning = true;
                 break;
             }
         }
         return isRunning;
     }
	 public static boolean isCarNo(String str){
		 String reg="^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$|^[a-zA-Z]{2}\\d{7}$";
		 if (str.matches(reg)) {
			 return true;
		 } else{
			 String regdian="[0-9a-zA-Z]{4,9}";
			 if (str.matches(regdian))
				 return true;
			 else
				 return false;
		 }
	 }
    public static boolean isCarAndMotoNo(String str){
        String reg="^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$|^[a-zA-Z]{2}\\d{7}$";
        if (str.matches(reg)) {
            return true;
        } else{
            return false;
        }
    }
	public static String getIMEI(Context context) {
			if(context != null){
				TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(
						Context.TELEPHONY_SERVICE);
				return tm.getDeviceId();// 获取机器码
			}else{
				return "";
			}
	}
	public static String getPhoneNumber(Context context){
		if(context != null){
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			Log.i("phonenumber","aaaaaaaaaaaaaaaaaaa");
			Log.i("phonenumber",tm.getLine1Number()+"bbb");
			return tm.getLine1Number();
		}else{
			return "";
		}
	}
	@SuppressLint("SimpleDateFormat")
	public static File getTempImage() {
		return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"temp.jpg");
	}

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	public static Bitmap compressImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    /**
     * 用来判断是否开启通知权限
     * */
    public static boolean isNotificationEnabled(Context context){

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try {

            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int)opPostNotificationValue.get(Integer.class);
            return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

	private void initData() {
		String filePath = "/sdcard/Test/";
		String fileName = "log.txt";

		writeTxtToFile("txt content", filePath, fileName);
	}

	// 将字符串写入到文本文件中
	public static File writeTxtToFile(String strcontent, String filePath, String fileName) {
		//生成文件夹之后，再生成文件，不然会出错
		makeFilePath(filePath, fileName);

		String strFilePath = filePath+fileName;
		// 每次写入时，都换行写
		String strContent = strcontent + "\r\n";
		File file=null;
		try {
			file = new File(strFilePath);
			if (!file.exists()) {
				Log.d("TestFile", "Create the file:" + strFilePath);
				file.getParentFile().mkdirs();
				file.createNewFile();
			}else{
				file.delete();
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			raf.seek(file.length());
			raf.write(strContent.getBytes());
			raf.close();
		} catch (Exception e) {
			Log.e("TestFile", "Error on write File:" + e);
		}
		return file;
	}
	public static File makeFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			Log.i("error:", e + "");
		}
	}
}
