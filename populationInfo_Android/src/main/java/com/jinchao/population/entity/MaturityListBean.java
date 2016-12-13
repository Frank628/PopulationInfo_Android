package com.jinchao.population.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/8/17.
 */
public class MaturityListBean {
    public MaturityList data;
    public static class MaturityList{
        public List<MatureHouseOne> houselist;
    }
    public static class MatureHouseOne implements Serializable{
        public String house_addr="";
        public String house_code="";
        public List<MaturePeopleOne> peoplelist;
    }
    public static class MaturePeopleOne implements Serializable{
        public String house_addr="";
        public String house_code="";
        public String idcard="";
        public String resdients_status="";
        public String shihao="";
        public String sname="";
        public String write_time="";
    }
}
