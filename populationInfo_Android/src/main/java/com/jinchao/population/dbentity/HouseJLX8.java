package com.jinchao.population.dbentity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="housejlx8")
public class HouseJLX8 {

	public HouseJLX8() {
		super();
	}

	@Id
	public String id;

	@Column(column="hosejlx_name")
	public String hosejlx_name;

	@Column(column="fk_p")
	public int fk_p;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHosejlx_name() {
		return hosejlx_name;
	}

	public void setHosejlx_name(String hosejlx_name) {
		this.hosejlx_name = hosejlx_name;
	}

	public int getFk_p() {
		return fk_p;
	}

	public void setFk_p(int fk_p) {
		this.fk_p = fk_p;
	}

	public HouseJLX8(String id, String hosejlx_name, int fk_p) {
		super();
		this.id = id;
		this.hosejlx_name = hosejlx_name;
		this.fk_p = fk_p;
	}
	
	
}
