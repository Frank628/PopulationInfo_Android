package com.jinchao.population.entity;

import java.io.Serializable;
import java.util.List;

public class RealPeopleinHouseBean implements Serializable {
	public realPeppleHouseinfo data;
	
	public static class realPeppleHouseinfo implements Serializable{
		public List<RealPeopleinHouseOne> peoplelist;
		public List<RealPeopleStatusOne> peopleState;
		public List<RealHouseStatusOne> personCount;
	}
	public static class RealPeopleinHouseOne implements Serializable{
		public String chkudt="";
		public String hjdz="";
		public String house_addr="";
		public String house_id="";
		public String id="";
		public String idcard="";
		public String pcs="";
		public String roomCode="";
		public String sRelation="";
		public String sex="";
		public String sname="";
		public int status=1;
		public RealPeopleinHouseOne(String chkudt, String hjdz,
				String house_addr, String house_id, String id, String idcard,
				String pcs, String roomCode, String sRelation, String sex,
				String sname, int status) {
			super();
			this.chkudt = chkudt;
			this.hjdz = hjdz;
			this.house_addr = house_addr;
			this.house_id = house_id;
			this.id = id;
			this.idcard = idcard;
			this.pcs = pcs;
			this.roomCode = roomCode;
			this.sRelation = sRelation;
			this.sex = sex;
			this.sname = sname;
			this.status = status;
		}
		
	}
	public static class RealPeopleStatusOne implements Serializable{
		public String pcsInstance="";
		public String strRet="";
		
	}
	public static class RealHouseStatusOne implements Serializable{
		public String person_count="";
		public String stype="";
	}
}
