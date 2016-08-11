package com.jinchao.population.entity;

import java.util.List;

public class HouseAddressBean {
	
	public List<XiaoQuOne> data;
	public static class XiaoQuOne{
		public List<HouseAddressOne> hourseData;
		public String jlx="";
	}
	public static class HouseAddressOne{
		public String hrsAdress="";
		public String scode="";
	}
}
