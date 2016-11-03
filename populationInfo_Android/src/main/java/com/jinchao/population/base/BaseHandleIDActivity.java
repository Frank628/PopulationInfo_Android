package com.jinchao.population.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jinchao.population.R;

/**
 * Created by OfferJiShu01 on 2016/11/3.
 */

public class BaseHandleIDActivity extends BaseActiviy{

    public View memberView1,memberView2,memberView3,memberView4,relationView1,relationView2,relationView3,relationView4;
    public TextView memberTitle1,memberTitle2,memberTitle3,memberTitle4;
    public String MSN="",Email="",czwxz="",lsrq="",djrq="",fzxm="",fzdh="",fzsfz="",dwlxdh="",zymc="",ldhtqj="",sbbh="",
            jyrq="",jkzbh="",dwfzr="",sfjy="",hyzmzl="",hyzmbh="",jqjzym="",jhrq="",yfjzzh="",fwkh="",bycsrq="",czqx1="",czqx2="",
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
}
