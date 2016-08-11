package com.jinchao.population;

import com.jinchao.population.main.SplashActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent2=new Intent(context,SplashActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        context.startActivity(intent2);
    }
}
