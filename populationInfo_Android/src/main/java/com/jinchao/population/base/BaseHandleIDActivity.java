package com.jinchao.population.base;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.R;
import com.jinchao.population.dbentity.HouseJLX;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.mainmenu.HandleIDActivity;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by OfferJiShu01 on 2016/11/3.
 */

public class BaseHandleIDActivity extends BaseActiviy{
    public DbUtils dbUtils;
    public View memberView1,memberView2,memberView3,memberView4,relationView1,relationView2,relationView3,relationView4;
    public EditText memberName1,memberName2,memberName3,memberName4;
    public RadioButton memberSex1,memberSex2,memberSex3,memberSex4;
    public TextView memberBirth1,memberBirth2,memberBirth3,memberBirth4,memberGuanxi1,memberGuanxi2,memberGuanxi3,memberGuanxi4;
    public ValidateEidtText zinvSfz1,zinvSfz2,zinvSfz3,zinvSfz4,memberSfz1,memberSfz2,memberSfz3,memberSfz4;
    public EditText zinvName1,zinvName2,zinvName3,zinvName4;
    public RadioButton zinvSex1,zinvSex2,zinvSex3,zinvSex4,zinvJiezhong1,zinvJiezhong2,zinvJiezhong3,zinvJiezhong4;
    public TextView zinvBirth1,zinvBirth2,zinvBirth3,zinvBirth4;
    public String cbqk="",MSN="",Email="",czwxz="",lsrq="",djrq="",fzxm="",fzdh="",fzsfz="",dwlxdh="",zymc="",ldhtqj="",sbbh="",
            jyrq="",jkzbh="",dwfzr="",sfjy="",hyzmzl="",hyzmbh="",jqjzym="",hyqfrq="",jhrq="",yfjzzh="",fwkh="",bycsrq="",czqx1="",czqx2="",
            zjdq="",beizhu2 ="",xdr1="",xdxm1="",xdxb1="",xdrq1="",xdsfz1="",
            xdr2="",xdxm2="",xdxb2="",xdrq2="",xdsfz2="",
            xdr3="",xdxm3="",xdxb3="",xdrq3="",xdsfz3="",
            xdr4="",xdxm4="",xdxb4="",xdrq4="",xdsfz4="",
            gxr1="",gxxm1="",gxxb1="",gxrq1="",gxsfz1="",gxrjzk1="",
            gxr2="",gxxm2="",gxxb2="",gxrq2="",gxsfz2="",gxrjzk2="",
            gxr3="",gxxm3="",gxxb3="",gxrq3="",gxsfz3="",gxrjzk3="",
            gxr4="",gxxm4="",gxxb4="",gxrq4="",gxsfz4="",gxrjzk4="";
    public static final int IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int RESULT_REQUEST_CODE = 2;
    public static final int REQUEST_CODE_CAMERA=3;
    public static final String IMAGE_FILE_NAME = "faceImage.jpg";

    public String[] PSCName,USERName,UserId;
    public Map<String, String[]> UserNameMap=new HashMap<String, String[]>();
    public Map<String, String[]> UserIdMap=new HashMap<String, String[]>();
    public boolean isHouseAddressInital=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbUtils= DeviceUtils.getDbUtils(this);

    }

    public View getMemberView() {
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_member, null);
    }
    public View getGuanxirenView() {
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_relation, null);
    }

    public void hideSoftKeyBord(){
        if (getCurrentFocus()!=null) {
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public boolean BiggerorEqualCurrent(String currentTime,String compareTime){
        try {
            int current=Integer.parseInt(currentTime);
            int compare=Integer.parseInt(compareTime);
            if (compare>=current)
                return true;
            else
                return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean BiggerCurrent(String currentTime,String compareTime){
        try {
            int current=Integer.parseInt(currentTime);
            int compare=Integer.parseInt(compareTime);
            if (compare>current)
                return true;
            else
                return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getGuanXiRenCode(String str){
        switch (str){
            case "配偶":
                return "0";
            case "子女":
                return "1";
            case "父母":
                return "2";
            case "兄弟姐妹":
                return "3";
            case "其他":
                return "4";
            default:
                return "4";
        }
    }
    public void validateIdCard(EditText v,String idcard){
        if (CommonIdcard.validateCard(idcard)) {
            if (idcard.length() == 15) {
                idcard = CommonIdcard.conver15CardTo18(idcard);
                v.setText(idcard);
                Toast.makeText(BaseHandleIDActivity.this, "15位转18位证件号成功",Toast.LENGTH_SHORT).show();
            } else if (idcard.length() == 17) {
                idcard = CommonIdcard.conver17CardTo18(idcard);
                v.setText(idcard);
                Toast.makeText(BaseHandleIDActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(BaseHandleIDActivity.this, "请先输入合法的身份证号", Toast.LENGTH_SHORT).show();
            return;
        }
    }


}
