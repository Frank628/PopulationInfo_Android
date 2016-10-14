package com.jinchao.population.entity;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/10/13.
 */
public class RealHouseInfo {
    public List<RealHouseInfoOne> data;
    public static class RealHouseInfoOne{
        public String  house_addr="";
        public String  idcard="";
        public String  name="";
        public String  scode="";
        public String  tel="";

        @Override
        public String toString() {
            return "房屋地址：  " + house_addr + "\n房东姓名：  " + name
                    + "\n房东身份证号：  " + idcard + "\n房东电话：  " + tel ;
        }
    }
}
