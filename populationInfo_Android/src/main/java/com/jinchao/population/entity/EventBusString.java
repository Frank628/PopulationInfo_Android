package com.jinchao.population.entity;

import java.io.Serializable;

/**
 * Created by user on 2018/2/11.
 */

public class EventBusString implements Serializable{

    public int type ;//0代表街路巷，1代表楼幢号
    public String address;

    public EventBusString(int type, String address) {
        this.type = type;
        this.address = address;
    }
}
