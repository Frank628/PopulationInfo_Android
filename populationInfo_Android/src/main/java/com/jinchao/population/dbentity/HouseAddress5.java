package com.jinchao.population.dbentity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="houseaddress5")
public class HouseAddress5 {
	@Id
	public String id;

	@Column(column="address")
	public String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public HouseAddress5(String id, String address) {
		super();
		this.id = id;
		this.address = address;
	}

	public HouseAddress5() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	
}
