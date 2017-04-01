package com.jinchao.population.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddressOldBean;
import com.jinchao.population.dbentity.HouseAddressOldBean10;
import com.jinchao.population.dbentity.HouseAddressOldBean2;
import com.jinchao.population.dbentity.HouseAddressOldBean3;
import com.jinchao.population.dbentity.HouseAddressOldBean4;
import com.jinchao.population.dbentity.HouseAddressOldBean5;
import com.jinchao.population.dbentity.HouseAddressOldBean6;
import com.jinchao.population.dbentity.HouseAddressOldBean7;
import com.jinchao.population.dbentity.HouseAddressOldBean8;
import com.jinchao.population.dbentity.HouseAddressOldBean9;
import com.jinchao.population.dbentity.UserPKDataBase;
import com.jinchao.population.mainmenu.RegistRentalHouseActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2017/3/30.
 */

public class HouseTableInit {
    private Context context;
    private int database_tableNo=0;
    private Handler handler;
    private DbUtils dbUtils;
    public static HouseTableInit getInstance(Context context,int database_tableNo,Handler handler){
        HouseTableInit houseTableInit=new HouseTableInit(context,database_tableNo,handler);
        return houseTableInit;
    }

    public HouseTableInit(Context context,int database_tableNo,Handler handler) {
        this.context=context;
        this.database_tableNo=database_tableNo;
        this.handler=handler;
        this.dbUtils=DeviceUtils.getDbUtils(context);
    }

    public void addtionalHouse(){
        String lastudttime="2011-01-01 12:00:00";
        try {
            UserPKDataBase userPKDataBase=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name","=", MyInfomationManager.getSQNAME(context)));
            if (userPKDataBase!=null){
                lastudttime=userPKDataBase.getUpdate_time();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(lastudttime))lastudttime="2011-01-01 12:00:00";
        RequestParams params=new RequestParams(Constants.URL+"webHouseServer.aspx");
        params.addBodyParameter("type", "get");
        params.addBodyParameter("user_id",MyInfomationManager.getUserID(context));
        params.addBodyParameter("last_udt",lastudttime);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("zenliang",result);
                processAddtionalHouse(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished(){}
        });
    }
    private void processAddtionalHouse(final String result){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultBeanAndList<HouseAddressOldBean> housexml = XmlUtils.getBeanByParseXml(result,"Table",HouseAddressOldBean.class, "", HouseAddressOldBean.class);
                    if (housexml!=null) {
                        if (housexml.list != null) {
                            for (int i = 0; i < housexml.list.size(); i++) {
                                try {
                                    switch (database_tableNo) {
                                        case 1:
                                            HouseAddressOldBean houseAddressOldBean = new HouseAddressOldBean(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean);
                                            break;
                                        case 2:
                                            HouseAddressOldBean2 houseAddressOldBean2 = new HouseAddressOldBean2(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean2);
                                            break;
                                        case 3:
                                            HouseAddressOldBean3 houseAddressOldBean3 = new HouseAddressOldBean3(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean3);
                                            break;
                                        case 4:
                                            HouseAddressOldBean4 houseAddressOldBean4 = new HouseAddressOldBean4(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean4);
                                            break;
                                        case 5:
                                            HouseAddressOldBean5 houseAddressOldBean5 = new HouseAddressOldBean5(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean5);
                                            break;
                                        case 6:
                                            HouseAddressOldBean6 houseAddressOldBean6 = new HouseAddressOldBean6(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean6);
                                            break;
                                        case 7:
                                            HouseAddressOldBean7 houseAddressOldBean7 = new HouseAddressOldBean7(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean7);
                                            break;
                                        case 8:
                                            HouseAddressOldBean8 houseAddressOldBean8 = new HouseAddressOldBean8(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean8);
                                            break;
                                        case 9:
                                            HouseAddressOldBean9 houseAddressOldBean9 = new HouseAddressOldBean9(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean9);
                                            break;
                                        case 10:
                                            HouseAddressOldBean10 houseAddressOldBean10 = new HouseAddressOldBean10(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
                                            dbUtils.saveOrUpdate(houseAddressOldBean10);
                                            break;
                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date =sDateFormat.format(new Date(System.currentTimeMillis()));
                    UserPKDataBase userPKDataBase=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name","=",MyInfomationManager.getSQNAME(context)));
                    if (userPKDataBase!=null){
                        userPKDataBase.setIs_used("0");
                        userPKDataBase.setUpdate_time(date);
                        dbUtils.update(userPKDataBase,"database_name","is_used","update_time");
                    }else{
                        dbUtils.save(new UserPKDataBase(MyInfomationManager.getSQNAME(context), database_tableNo, date, "0",date));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
