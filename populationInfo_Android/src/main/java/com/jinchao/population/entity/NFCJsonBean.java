package com.jinchao.population.entity;

import java.io.Serializable;

/**
 * Created by OfferJiShu01 on 2017/3/7.
 */

public class NFCJsonBean implements Serializable{
    public String code="";
    public String name="";
    public String idcard="";
    public String phone="";
    public String add="";
    public String udt="";
    public String sq="";
    public String ntime="";
    public String imp="";
    public String room="";

    public NFCJsonBean(String code, String name, String idcard, String add, String phone, String udt) {
        this.code = code;
        this.name = name;
        this.idcard = idcard;
        this.add = add;
        this.phone = phone;
        this.udt = udt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getUdt() {
        return udt;
    }

    public void setUdt(String udt) {
        this.udt = udt;
    }

    public String getSq() {
        return sq;
    }

    public void setSq(String sq) {
        this.sq = sq;
    }

    public String getNtime() {
        return ntime;
    }

    public void setNtime(String ntime) {
        this.ntime = ntime;
    }

    public String getImp() {
        return imp;
    }

    public void setImp(String imp) {
        this.imp = imp;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "房屋地址：  " + add + "\n房东姓名：  " + name
                + "\n房东身份证号：  " + idcard + "\n房东电话：  " + phone + "\n采集时间：  "
                + udt+"\n室号：  " + room+"\n重点人员：  " + imp+"\n标签制作时间：  " + ntime;
    }
}
