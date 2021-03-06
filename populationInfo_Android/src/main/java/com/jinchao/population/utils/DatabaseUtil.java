package com.jinchao.population.utils;

import android.content.Context;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.dbentity.HouseAddress10;
import com.jinchao.population.dbentity.HouseAddress2;
import com.jinchao.population.dbentity.HouseAddress3;
import com.jinchao.population.dbentity.HouseAddress4;
import com.jinchao.population.dbentity.HouseAddress5;
import com.jinchao.population.dbentity.HouseAddress6;
import com.jinchao.population.dbentity.HouseAddress7;
import com.jinchao.population.dbentity.HouseAddress8;
import com.jinchao.population.dbentity.HouseAddress9;
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
import com.jinchao.population.dbentity.HouseJLX;
import com.jinchao.population.dbentity.HouseJLX10;
import com.jinchao.population.dbentity.HouseJLX2;
import com.jinchao.population.dbentity.HouseJLX3;
import com.jinchao.population.dbentity.HouseJLX4;
import com.jinchao.population.dbentity.HouseJLX5;
import com.jinchao.population.dbentity.HouseJLX6;
import com.jinchao.population.dbentity.HouseJLX7;
import com.jinchao.population.dbentity.HouseJLX8;
import com.jinchao.population.dbentity.HouseJLX9;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.dbentity.JLX10;
import com.jinchao.population.dbentity.JLX2;
import com.jinchao.population.dbentity.JLX3;
import com.jinchao.population.dbentity.JLX4;
import com.jinchao.population.dbentity.JLX5;
import com.jinchao.population.dbentity.JLX6;
import com.jinchao.population.dbentity.JLX7;
import com.jinchao.population.dbentity.JLX8;
import com.jinchao.population.dbentity.JLX9;
import com.jinchao.population.dbentity.UserHistory;
import com.jinchao.population.dbentity.UserPKDataBase;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/1/6.
 */

public class DatabaseUtil {
    public static int getNullDB(Context context){
        int no=5;
        try {
            DbUtils dbUtils=DeviceUtils.getDbUtils(context);
            if(dbUtils.tableIsExist(UserPKDataBase.class)){
                for (int i=1;i<11;i++){
                    UserPKDataBase userPKDataBase=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("database_name","=",i).and("is_used","=","0"));
                    if (userPKDataBase==null){
                        return i;
                    }
                }
//               UserPKDataBase userPKDataBase_d=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("is_used","=","1"));
//               if (userPKDataBase_d!=null){
//                   return userPKDataBase_d.getDatabase_name();
//               }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return no;
    }

    public static Class<?> getTable_JLX(int database_tableNo,Context context){
        if (database_tableNo==0){
            database_tableNo  =getNullDB(context);
        }
        switch (database_tableNo){
            case 0:return null;
            case 1:return JLX.class;
            case 2:return JLX2.class;
            case 3:return JLX3.class;
            case 4:return JLX4.class;
            case 5:return JLX5.class;
            case 6:return JLX6.class;
            case 7:return JLX7.class;
            case 8:return JLX8.class;
            case 9:return JLX9.class;
            case 10:return JLX10.class;
        }
        return null;
    }
    public static Class<?> getTable_HouseAddressOldBean(int database_tableNo,Context context){
        if (database_tableNo==0){
            database_tableNo  =getNullDB(context);
        }
        switch (database_tableNo){
            case 0:return null;
            case 1:return HouseAddressOldBean.class;
            case 2:return HouseAddressOldBean2.class;
            case 3:return HouseAddressOldBean3.class;
            case 4:return HouseAddressOldBean4.class;
            case 5:return HouseAddressOldBean5.class;
            case 6:return HouseAddressOldBean6.class;
            case 7:return HouseAddressOldBean7.class;
            case 8:return HouseAddressOldBean8.class;
            case 9:return HouseAddressOldBean9.class;
            case 10:return HouseAddressOldBean10.class;
        }
        return null;
    }
    public static Class<?> getTable_HouseAddress(int database_tableNo,Context context){
        if (database_tableNo==0){
            database_tableNo  =getNullDB(context);
        }
        switch (database_tableNo){
            case 0:return null;
            case 1:return HouseAddress.class;
            case 2:return HouseAddress2.class;
            case 3:return HouseAddress3.class;
            case 4:return HouseAddress4.class;
            case 5:return HouseAddress5.class;
            case 6:return HouseAddress6.class;
            case 7:return HouseAddress7.class;
            case 8:return HouseAddress8.class;
            case 9:return HouseAddress9.class;
            case 10:return HouseAddress10.class;
        }
        return null;
    }
    public static Class<?> getTable_HouseJLX(int database_tableNo,Context context){
        if (database_tableNo==0){
            database_tableNo  =getNullDB(context);
        }
        switch (database_tableNo){
            case 0:return null;
            case 1:return HouseJLX.class;
            case 2:return HouseJLX2.class;
            case 3:return HouseJLX3.class;
            case 4:return HouseJLX4.class;
            case 5:return HouseJLX5.class;
            case 6:return HouseJLX6.class;
            case 7:return HouseJLX7.class;
            case 8:return HouseJLX8.class;
            case 9:return HouseJLX9.class;
            case 10:return HouseJLX10.class;
        }
        return null;
    }
    public static boolean IsSQ_DataBase_Exist(Context context){
        DbUtils dbUtils =DeviceUtils.getDbUtils(context);
        try {
            UserPKDataBase userPKDataBase=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name","=", MyInfomationManager.getSQNAME(context)));
            if (userPKDataBase!=null){
                userPKDataBase.setIs_used("0");//0复用数据库，1代表可删除的数据库关系
                dbUtils.saveOrUpdate(userPKDataBase);
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static int getSQ_DataBase_No(Context context){
        int no=0;
        DbUtils dbUtils =DeviceUtils.getDbUtils(context);
        try {
            UserPKDataBase userPKDataBase=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name","=", MyInfomationManager.getSQNAME(context)));
            if (userPKDataBase!=null){
                if(userPKDataBase.is_used.equals("1")) {
                    UserPKDataBase userPKDataBase2 = dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("database_name", "=", userPKDataBase.database_name).and("sq_name","!=",MyInfomationManager.getSQNAME(context)));
                    if (userPKDataBase2 != null) {
                        dbUtils.delete(userPKDataBase2);
                    }
                    userPKDataBase.setIs_used("0");
                    dbUtils.update(userPKDataBase, "is_used");
                }
                no=userPKDataBase.getDatabase_name();
            }else{
                no=0;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }
}
