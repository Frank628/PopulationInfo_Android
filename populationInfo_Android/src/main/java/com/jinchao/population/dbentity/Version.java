package com.jinchao.population.dbentity;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 城市表
 * @author FRANK
 *
 */
@Table(name="version")
public class Version {
	@NoAutoIncrement
	public int id;
	
	@Column(column="version_current")
	public String version_current;

    public String getVersion_current() {
        return version_current;
    }

    public void setVersion_current(String version_current) {
        this.version_current = version_current;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
