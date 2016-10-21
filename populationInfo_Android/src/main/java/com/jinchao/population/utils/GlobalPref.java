package com.jinchao.population.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GlobalPref {
	private static final String ADDRESS_PREF_KEY = "IP_PREF_KEY";
	private static final String PORT_PREF_KEY = "PORT_PREF_KEY";
    private static final String USE_AUTO_PREF_KEY="USE_AUTO_PREF_KEY";
    private static Context context=null;
    private static SharedPreferences pref;
    public static void init(Context context){
    	GlobalPref.context=context;
    	pref= PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public static void saveAddress(String address){
    	pref.edit().putString(ADDRESS_PREF_KEY,address).commit();    	
    }
    
    public static String getAddress(){
    	return pref.getString(ADDRESS_PREF_KEY,"");
    }
    
    public static void savePort(int port){
    	pref.edit().putInt(PORT_PREF_KEY, port).commit();
    }
    
	public static int getPort(){
		return pref.getInt(PORT_PREF_KEY,0);
	}
	
	public static void saveUseAuto(boolean value){
		pref.edit().putBoolean(USE_AUTO_PREF_KEY, value).commit();
	}
	
	public static boolean getUseAuto(){
		return pref.getBoolean(USE_AUTO_PREF_KEY,true);
	}
	
}
