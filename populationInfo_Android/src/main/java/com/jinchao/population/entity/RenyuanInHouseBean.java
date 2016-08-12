package com.jinchao.population.entity;

import java.io.Serializable;
import java.util.List;

public class RenyuanInHouseBean {
	public RenyuanInHouseBeanOne data;
	public static class RenyuanInHouseBeanOne{
		public String house_exist="";
		public String people_exist="";
		public List<RenyuanInhouseOne> peoplelist;
	}

	public static class RenyuanInhouseOne implements Serializable{
		public String house_addr="";
		public String house_code="";
		public String idcard="";
		public String resdients_status="";
		public String shihao="";
		public String sname="";
		public String write_time="";
	}
}
