package com.jinchao.population.dbentity;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 用户登录历史记录
 * @author FRANK
 *
 */
@Table(name="userhistory")
public class UserHistory {
	@Id
	public int id;
	
	@Column(column="user_name")
	public String user_name;

	@Column(column="user_id")
	public String user_id;

	@Column(column="sq_name")
	public String sq_name;

	@Column(column="sq_id")
	public String sq_id;

	@Column(column="pcs_name")
	public String pcs_name;

	@Column(column="pcs_id")
	public String pcs_id;

	@Column(column="time")
	public String time;
	public String getPcs_id() {
		return pcs_id;
	}

	public void setPcs_id(String pcs_id) {
		this.pcs_id = pcs_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSq_name() {
		return sq_name;
	}

	public void setSq_name(String sq_name) {
		this.sq_name = sq_name;
	}

	public String getSq_id() {
		return sq_id;
	}

	public void setSq_id(String sq_id) {
		this.sq_id = sq_id;
	}

	public String getPcs_name() {
		return pcs_name;
	}

	public void setPcs_name(String pcs_name) {
		this.pcs_name = pcs_name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public UserHistory() {}

	public UserHistory(String user_name, String user_id, String sq_name, String sq_id, String pcs_name, String pcs_id,String time) {
		this.user_name = user_name;
		this.user_id = user_id;
		this.sq_name = sq_name;
		this.sq_id = sq_id;
		this.pcs_name = pcs_name;
		this.pcs_id = pcs_id;
		this.time=time;
	}
}
