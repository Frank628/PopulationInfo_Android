package com.jinchao.population.dbentity;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 城市表
 * @author FRANK
 *
 */
@Table(name="city")
public class City {
	@NoAutoIncrement
	public int id;
	
	@Column(column="city_name")
	public String city_name;

	@Column(column="fk_p")
	public int fk_p;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public int getFk_p() {
		return fk_p;
	}

	public void setFk_p(int fk_p) {
		this.fk_p = fk_p;
	}
	
	
	
	
	

	
	
	
	
}
