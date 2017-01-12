package com.jinchao.population.entity;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/8/17.
 */
public class StatisticsJsonBean {
    public List<StatisticOne> data;

    public static class StatisticOne{
        public String action_type="";
        public String acount="";
    }

}
