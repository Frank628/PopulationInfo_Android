package com.jinchao.population.dbentity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="Houseaddressold7")
public class HouseAddressOldBean7 {
	@Id
	public String id="";

	@Column(column="shequ_id")
	public String shequ_id="";

	@Column(column="user_id")
	public String user_id="";

	@Column(column="scode")
	public String scode="";

	@Column(column="address")
	public String address="";

	@Column(column="hrs_pname")
	public String hrs_pname="";

	@Column(column="telphone")
	public String telphone="";

	@Column(column="idcard")
	public String idcard="";

	@Column(column="source_id")
	public String source_id="";

	@Column(column="udt")
	public String udt="";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShequ_id() {
		return shequ_id;
	}

	public void setShequ_id(String shequ_id) {
		this.shequ_id = shequ_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHrs_pname() {
		return hrs_pname;
	}

	public void setHrs_pname(String hrs_pname) {
		this.hrs_pname = hrs_pname;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getUdt() {
		return udt;
	}

	public void setUdt(String udt) {
		this.udt = udt;
	}

	public HouseAddressOldBean7(String id, String shequ_id, String user_id,
								String scode, String address, String hrs_pname, String telphone,
								String idcard, String source_id, String udt) {
		super();
		this.id = id;
		this.shequ_id = shequ_id;
		this.user_id = user_id;
		this.scode = scode;
		this.address = address;
		this.hrs_pname = hrs_pname;
		this.telphone = telphone;
		this.idcard = idcard;
		this.source_id = source_id;
		this.udt = udt;
	}

	public HouseAddressOldBean7() {
		super();
	}

	@Override
	public String toString() {
		return "房屋地址：  " + address + "\n房东姓名：  " + hrs_pname
				+ "\n房东身份证号：  " + idcard + "\n房东电话：  " + telphone + "\n采集时间：  "
				+ udt ;
	}
	
	
}
