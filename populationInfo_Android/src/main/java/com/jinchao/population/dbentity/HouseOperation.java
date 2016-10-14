package com.jinchao.population.dbentity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="houseoperation")
public class HouseOperation {

	public HouseOperation() {
		super();
	}

	@Id
	public String id;

	@Column(column="house_code")
	public String house_code;

	@Column(column="address")
	public String address;

	@Column(column="last_upt")
	public String last_upt;

	@Column(column="operation_type")
	public String operation_type;

	@Column(column="operator")
	public String operator;

	public HouseOperation(String house_code, String address, String last_upt, String operation_type, String operator) {
		this.house_code = house_code;
		this.address = address;
		this.last_upt = last_upt;
		this.operation_type = operation_type;
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

	public String getId() {
		return id;
	}

	public String getHouse_code() {
		return house_code;
	}

	public String getAddress() {
		return address;
	}

	public String getLast_upt() {
		return last_upt;
	}

	public String getOperation_type() {
		return operation_type;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setHouse_code(String house_code) {
		this.house_code = house_code;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLast_upt(String last_upt) {
		this.last_upt = last_upt;
	}

	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}
}
