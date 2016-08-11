package com.jinchao.population;

import java.util.LinkedList;
import java.util.List;


import android.app.Activity;
import android.app.Application;

public class SysApplication extends Application{
	
	
	private List<Activity> mList = new LinkedList<Activity>(); 
    private List<Activity> mList_ex_index = new LinkedList<Activity>(); 
    private List<Activity> login = new LinkedList<Activity>(); 
    private static SysApplication instance;
    private SysApplication() {   
    } 
   
    public synchronized static SysApplication getInstance() { 
        if (null == instance) { 
            instance = new SysApplication(); 
        } 
        return instance; 
    } 
 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
    public void addExIndexActivity(Activity activity) { 
    	mList_ex_index.add(activity); 
    }
    public void addLogin(Activity activity) { 
    	login.add(activity); 
    }
    public void killlogin() { 
        try { 
            for (Activity activity : login) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    } 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
    public void killExActivities() { 
        try { 
            for (Activity activity : mList_ex_index) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    } 
    @Override 
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    } 
}
