package com.jinchao.population.entity;

import java.util.List;

/**
 * Create by FrankFan on 2018/5/17
 * Description:
 */
public class WarnMsg {

    public List<WarnMsgOne> data;
    public static class WarnMsgOne{
        public String sname="";
        public String idcard="";
        public String tel="";
        public String resultCode="";
        public String resultMsg="";
        public String write_time="";
    }
}
