package com.jinchao.population;

import com.jinchao.population.config.Constants;
import com.jinchao.population.utils.SharePrefUtil;

import android.content.Context;

public class MyInfomationManager {
	public static void setUserName(Context context,String value){
		SharePrefUtil.saveString(context, Constants.USERNAME, value);
	}
	public static String getUserName(Context context){
		return SharePrefUtil.getString(context, Constants.USERNAME, "");
	}
	public static void setPassWord(Context context,String value){
		SharePrefUtil.saveString(context, Constants.PASSWORD, value);
	}
	public static String getPassWord(Context context){
		return SharePrefUtil.getString(context, Constants.PASSWORD, "");
	}
	public static void setUserID(Context context,String value){
		SharePrefUtil.saveString(context, Constants.USERID, value);
	}
	public static String getUserID(Context context){
		return SharePrefUtil.getString(context, Constants.USERID, "");
	}
	
	public static void setSQID(Context context,String value){
		SharePrefUtil.saveString(context, Constants.SQID, value);
	}
	public static String getSQID(Context context){
		return SharePrefUtil.getString(context, Constants.SQID, "");
	}
	public static void setSQNAME(Context context,String value){
		SharePrefUtil.saveString(context, Constants.SQNAME, value);
	}
	public static String getSQNAME(Context context){
		return SharePrefUtil.getString(context, Constants.SQNAME, "");
	}
}
