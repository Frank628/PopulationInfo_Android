package com.jinchao.population.dbentity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="jlx9")
public class JLX9 {

		public JLX9() {
		super();
		// TODO Auto-generated constructor stub
	}

		@Id
		public int id;

		@Column(column="jlx_name")
		public String jlx_name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getJlx_name() {
			return jlx_name;
		}

		public void setJlx_name(String jlx_name) {
			this.jlx_name = jlx_name;
		}

		public JLX9(int id, String jlx_name) {
			super();
			this.id = id;
			this.jlx_name = jlx_name;
		}

		public JLX9(String jlx_name) {
			super();
			this.jlx_name = jlx_name;
		}

		


		
}
