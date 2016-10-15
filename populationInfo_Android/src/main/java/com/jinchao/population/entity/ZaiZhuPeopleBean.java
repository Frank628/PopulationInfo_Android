package com.jinchao.population.entity;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/10/15.
 */
public class ZaiZhuPeopleBean {
    public ZaiZhuPeopleBeanData data;
    public static class ZaiZhuPeopleBeanData{
        public String house_exist="";
        public String people_exist="";
        public List<ZaiZhuPeopleOne> peoplelist;

    }
    public static class ZaiZhuPeopleOne{
        public String house_addr="";
        public String house_code="";
        public String idcard="";
        public String resdients_status="";
        public String shihao="";
        public String sname="";
        public String write_time="";
    }
}
