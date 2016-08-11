package com.jinchao.population.entity;

public class YanZhengBean {
	public String result="";
	public String user_id="";
	public String HasJzz="";
	public String name="";
	public String bzsq="";
	public String house_addr="";
	public String house_code="";
	public String udt="";
	public String telphone="";
	public String fwcs="";
	@Override
	public String toString() {
		return  (result.equals("1")?" 此人已登记":"")+"\n 姓名：" + name
				+ "\n 是否办理过居住证：" + (HasJzz .equals("1")?"是":"否")+ "\n 初次办证社区：" + bzsq + "\n 居住地址：" + house_addr+ "\n 房屋编号：" + house_code
				+ "\n 信息来源：" + user_id + "\n 工作单位：" + fwcs + "\n 联系电话："
				+ telphone + "\n 末次采集时间：" + udt ;
	}

}
