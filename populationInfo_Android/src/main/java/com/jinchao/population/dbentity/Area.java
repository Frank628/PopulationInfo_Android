package com.jinchao.population.dbentity;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 区   数据库表
 * @author FRANK
 *
 */
@Table(name="area")
public class Area {
	@NoAutoIncrement
	public int id;
	
	@Column(column="area_name")
	public String area_name;

	@Column(column="fk_c")
	public int fk_c;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public int getFk_c() {
		return fk_c;
	}

	public void setFk_c(int fk_c) {
		this.fk_c = fk_c;
	}

	
	
	
	
	

	
	
	
	
}
