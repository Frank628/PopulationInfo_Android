package com.jinchao.population.dbentity;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 省 表
 * @author FRANK
 *
 */
@Table(name="province")
public class Province {
	@NoAutoIncrement
	public int id;
	
	@Column(column="province_name")
	public String province_name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	
	
	
	

	
	
	
	
}
