package com.jinchao.population.entity;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/8/17.
 */
public class NearbyHouseBean {
    public List<NearbyHouseOne> data;

    public static class NearbyHouseOne{
        public String house_id="";
        public String scode="";
        public String address="";
        public String house_pic="";
        public String distance="";
    }
}
