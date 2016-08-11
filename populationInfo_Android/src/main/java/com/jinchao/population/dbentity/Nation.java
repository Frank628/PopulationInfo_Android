package com.jinchao.population.dbentity;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 户籍地址表
 * @author FRANK
 *
 */
@Table(name="nation")
public class Nation {
	@Id
	@NoAutoIncrement
	public String id;
	
	@Column(column="nation_name")
	public String nation_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNation_name() {
		return nation_name;
	}

	public void setNation_name(String nation_name) {
		this.nation_name = nation_name;
	}
	
	
	
	

	
	
	
	
}
