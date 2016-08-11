package com.jinchao.population.entity;

import java.io.Serializable;
import java.util.List;

public class RealHouseBean {
	public List<RealHouseOne> data;

	public static class RealHouseOne implements Serializable{
		public String houseAdress="";
		public String houseId="";
		public String oldScode="";
		public String scode="";
		
	}

	
}
