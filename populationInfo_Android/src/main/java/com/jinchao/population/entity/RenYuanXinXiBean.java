package com.jinchao.population.entity;

import java.io.Serializable;

public class RenYuanXinXiBean implements Serializable{
	public String 居住状态="";
	public String 室号="";
	public String 姓名="";
	public String 身份证号="";
	public String 末次采集时间="";
	public String 房屋编号="";
	public String 房屋地址="";
	public String 性别="";
	public String 名族="";
	public String 户籍地址="";
	@Override
	public String toString() {
		return "RenYuanXinXiBean [居住状态=" + 居住状态 + ", 室号=" + 室号 + ", 姓名=" + 姓名
				+ ", 身份证号=" + 身份证号 + ", 末次采集时间=" + 末次采集时间 + ", 房屋编号=" + 房屋编号
				+ ", 房屋地址=" + 房屋地址 + ", 性别=" + 性别 + ", 名族=" + 名族 + ", 户籍地址="
				+ 户籍地址 + "]";
	}
	

}
