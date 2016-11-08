package com.jinchao.population.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jinchao.population.R;

/**
 * Created by OfferJiShu01 on 2016/11/3.
 */

public class BaseHandleIDActivity extends BaseActiviy{

    public View memberView1,memberView2,memberView3,memberView4,relationView1,relationView2,relationView3,relationView4;
    public EditText memberName1,memberName2,memberName3,memberName4,memberGuanxi1,memberGuanxi2,memberGuanxi3,memberGuanxi4,memberSfz1,memberSfz2,memberSfz3,memberSfz4;
    public RadioButton memberSex1,memberSex2,memberSex3,memberSex4;
    public TextView memberBirth1,memberBirth2,memberBirth3,memberBirth4;

    public EditText zinvName1,zinvName2,zinvName3,zinvName4,zinvSfz1,zinvSfz2,zinvSfz3,zinvSfz4;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View getMemberView() {
        //将xml布局文件生成view对象通过LayoutInflater
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //将view对象挂载到那个父元素上，这里没有就为null
        return inflater.inflate(R.layout.item_member, null);
    }
    public View getGuanxirenView() {
        //将xml布局文件生成view对象通过LayoutInflater
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //将view对象挂载到那个父元素上，这里没有就为null
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

}
