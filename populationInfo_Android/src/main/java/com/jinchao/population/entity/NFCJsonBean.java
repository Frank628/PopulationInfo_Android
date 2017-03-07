package com.jinchao.population.entity;

/**
 * Created by OfferJiShu01 on 2017/3/7.
 */

public class NFCJsonBean {
    public String code="";
    public String name="";
    public String idcard="";
    public String phone="";
    public String add="";
    public String udt="";
    public String sq="";

    @Override
    public String toString() {
        return "房屋地址：  " + add + "\n房东姓名：  " + name
                + "\n房东身份证号：  " + idcard + "\n房东电话：  " + phone + "\n采集时间：  "
                + udt ;
    }
}
