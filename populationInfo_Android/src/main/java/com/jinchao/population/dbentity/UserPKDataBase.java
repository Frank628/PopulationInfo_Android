package com.jinchao.population.dbentity;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 登录用户社区与数据库关系
 * @author FRANK
 *
 */
@Table(name="userpkdatabase")
public class UserPKDataBase {
	@Id
	public int id;

	@Column(column="sq_name")
	public String sq_name;

	@Column(column="database_name")
	public int database_name;

	@Column(column="create_time")
	public String create_time;

	@Column(column="update_time")
	public String update_time;

	@Column(column="is_used")
	public String is_used;

	public UserPKDataBase() {}

	public UserPKDataBase(String sq_name, int database_name, String time, String is_used) {
		this.sq_name = sq_name;
		this.database_name = database_name;
		this.create_time = create_time;
		this.is_used = is_used;
	}
	public UserPKDataBase(String sq_name, int database_name, String time) {
		this.sq_name = sq_name;
		this.database_name = database_name;
		this.update_time = update_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSq_name() {
		return sq_name;
	}

	public void setSq_name(String sq_name) {
		this.sq_name = sq_name;
	}

	public int getDatabase_name() {
		return database_name;
	}

	public void setDatabase_name(int database_name) {
		this.database_name = database_name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getIs_used() {
		return is_used;
	}

	public void setIs_used(String is_used) {
		this.is_used = is_used;
	}
}
