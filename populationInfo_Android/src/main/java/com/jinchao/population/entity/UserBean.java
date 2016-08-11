package com.jinchao.population.entity;

import java.io.Serializable;
import java.util.List;

public class UserBean {
	public List<PCS> data;
	public static class PCS{
		public String pcsId="";
		public String pcsName="";
		public List<AccountOne> account;
	}
	public static class AccountOne implements Serializable{
		private static final long serialVersionUID = 5085314785752636207L;
		public AccountOne(String userId, String userName) {
			this.userId = userId;
			this.userName = userName;
		}
		public String sqId="";
		public String sqName="";
		public String userId="";
		public String userName="";
		
	}
}
