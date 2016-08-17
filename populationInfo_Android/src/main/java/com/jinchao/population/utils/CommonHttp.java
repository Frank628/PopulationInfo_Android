package com.jinchao.population.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.HouseDataBaseType;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/8/16.
 */
public class CommonHttp {
    public static int databasetype=0;
    public static int getDataBaseType(final Context context){
        databasetype=0;
        RequestParams params=new RequestParams(Constants.URL+"HousePosition.aspx?type=get_syrk&pcs_id="+ MyInfomationManager.getPCSId(context));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    HouseDataBaseType houseDataBaseType=GsonTools.changeGsonToBean(result,HouseDataBaseType.class);
                    Log.i("databsetyoe",houseDataBaseType.data.get(0).syrkFlag);
                    if (houseDataBaseType.data.get(0).syrkFlag.trim().equals("当前为外来人口房屋")){
                        SharePrefUtil.saveInt(context,Constants.DATABASE_TYPE,1);
                        databasetype= 1;
                    }else{
                        SharePrefUtil.saveInt(context,Constants.DATABASE_TYPE,2);
                        databasetype=2;
                    }
                } catch (Exception e) {
                    SharePrefUtil.saveInt(context,Constants.DATABASE_TYPE,0);
                    databasetype=0;
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {}
        });
        return databasetype;
    }
}
