package com.jinchao.population.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2018/2/11.
 */

public class RoomBean implements Serializable{
    public List<RoomOne> data;

    public static class RoomOne implements Serializable{
        public String room;
        public int tot;
        public List<BianhaoOne> hourseData;
    }

    public static class BianhaoOne implements Serializable{
        public String scode;
        public String hrsAdress;
        public String hrsPname;
        public String telphone;
        public String idcard;
        public String sfjz;
        public String house_id;
        public int r_status;
    }

}
