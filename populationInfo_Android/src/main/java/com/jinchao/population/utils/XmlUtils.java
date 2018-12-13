package com.jinchao.population.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.SQBean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlUtils {
	/**
	 * 解析一个bean和一个list的xml文件结构的方法
	 * @param listRoot 内层ListBean需要实例化对象的一个标识
	 * @param listClazz ListBean.class
	 * @param beanRoot 外层Bean需要实例化对象的一个标识
	 * @param beanClazz Bean.class
	 * @return 一个bean和一个list的结果
	 * @throws Exception
	 */
	public static <T,T1> ResultBeanAndList<T> getBeanByParseXml(String xml , String listRoot , Class<T> listClazz ,String beanRoot , Class<T1> beanClazz) throws Exception{
		//最后结果
		ResultBeanAndList<T> result = null;
		//list  存放一堆item
		ArrayList<T> list = null;
		//内层ListBean
		T t = null;
		//外层Bean
		T1 bean = null;
		//一个计数器
		int count = 0 ;
		try {
			ByteArrayInputStream tInputStringStream = null;  
			if (xml != null && !xml.trim().equals("")) {  
				tInputStringStream = new ByteArrayInputStream(xml.getBytes());  
			}  
			XmlPullParser parser = Xml.newPullParser(); 
			parser.setInput(tInputStringStream, "UTF-8");  
			//获得当前标签类型
			int eventType = parser.getEventType();
			//如果不是xml文件结束标签，则一个一个向下解析
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch (eventType) {
				//如果是xml文件开始标签，则初始化一些数据
				case XmlPullParser.START_DOCUMENT:
					//最后的结果
					result = new ResultBeanAndList<T>();
					//list
					list = new ArrayList<T>();
					//将list加入到result中，当前list是空的，等后面加入了数据后，就不是空了
					result.setList(list);
					break;
				//开始标签
				case XmlPullParser.START_TAG:
					//获得标签的名字
					String tagName = parser.getName();
					//如果内层的ListBean已经实例化出来的话
					if (t != null) {
						try {
							//判断当前标签在没在ListBean的属性中
							Field field = listClazz.getField(tagName);
							//如果ListBean中有当前标签
							if (field != null) {
								//计数器+1
								count++;
								//将取出来的值赋给ListBean中对应的属性
								field.set(t, parser.nextText());
							}
						} catch (Exception e) {
							//如果ListBean中没有当前标签，则会直接跳到这里，什么都不执行，然后再继续往下走
							
						}
					//如果外层的Bean已经实例化出来的话
					}else if (bean != null) {
						try {
							//判断当前标签在没在Bean的属性中
							Field field = beanClazz.getField(tagName);
							//如果Bean中有当前标签
							if (field != null) {
								//计数器+1
								count++;
								//将取出来的值赋给Bean中对应的属性
								field.set(bean, parser.nextText());
							}
						} catch (Exception e) {
							//如果Bean中没有当前标签，则会直接跳到这里，什么都不执行，然后再继续往下走
						}
					}
					//如果当前标签为我们传入的内层根标签，说明ListBean需要实例化出来了
					if (tagName.equals(listRoot)) {
						//将ListBean实例化出来
						t = listClazz.newInstance();
					}
					//如果当前标签为我们传入的内层根标签，说明Bean需要实例化出来了
					if (tagName.equals(beanRoot)) {
						//将Bean实例化出来
						bean = beanClazz.newInstance();
					}
					break;
				//结束标签
				case XmlPullParser.END_TAG:
					//如果当前标签为</item>
					if (listRoot.equalsIgnoreCase(parser.getName())) {
						//如果ListBean不为空
						if (t != null) {
							//保存到list中，同时也保存到了result中，因为list已经是保存在result中了，
							//只不过刚才没有值，现在有值了
							list.add(t);
							//并且把ListBean置空，因为后续还有好多个item
							t = null;
						}
					//如果当前标签为</root>
					}else if (beanRoot.equalsIgnoreCase(parser.getName())) {
						//将Bean保存到result中
						result.setBean(bean);
					}
					break;
				}
				//移动到下一个标签
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果计数器为0说明没有解析到任何数据
		if (count == 0) {
			//将result置空就可以了
			result = null;
		}
		//将result返回
		return result;
		
	}
	public static <T,T1> ResultBeanAndList<T> getBeanByParseXml(XmlPullParser parser , String listRoot , Class<T> listClazz ,String beanRoot , Class<T1> beanClazz) throws Exception{
		//最后结果
		ResultBeanAndList<T> result = null;
		//list  存放一堆item
		ArrayList<T> list = null;
		//内层ListBean
		T t = null;
		//外层Bean
		T1 bean = null;
		//一个计数器
		int count = 0 ;
		try {
			//获得当前标签类型
			int eventType = parser.getEventType();
			//如果不是xml文件结束标签，则一个一个向下解析
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch (eventType) {
				//如果是xml文件开始标签，则初始化一些数据
				case XmlPullParser.START_DOCUMENT:
					//最后的结果
					result = new ResultBeanAndList<T>();
					//list
					list = new ArrayList<T>();
					//将list加入到result中，当前list是空的，等后面加入了数据后，就不是空了
					result.setList(list);
					break;
				//开始标签
				case XmlPullParser.START_TAG:
					//获得标签的名字
					String tagName = parser.getName();
					//如果内层的ListBean已经实例化出来的话
					if (t != null) {
						try {
							//判断当前标签在没在ListBean的属性中
							Field field = listClazz.getField(tagName);
							//如果ListBean中有当前标签
							if (field != null) {
								//计数器+1
								count++;
								//将取出来的值赋给ListBean中对应的属性
								field.set(t, parser.nextText());
							}
						} catch (Exception e) {
							//如果ListBean中没有当前标签，则会直接跳到这里，什么都不执行，然后再继续往下走
							
						}
					//如果外层的Bean已经实例化出来的话
					}else if (bean != null) {
						try {
							//判断当前标签在没在Bean的属性中
							Field field = beanClazz.getField(tagName);
							//如果Bean中有当前标签
							if (field != null) {
								//计数器+1
								count++;
								//将取出来的值赋给Bean中对应的属性
								field.set(bean, parser.nextText());
							}
						} catch (Exception e) {
							//如果Bean中没有当前标签，则会直接跳到这里，什么都不执行，然后再继续往下走
						}
					}
					//如果当前标签为我们传入的内层根标签，说明ListBean需要实例化出来了
					if (tagName.equals(listRoot)) {
						//将ListBean实例化出来
						t = listClazz.newInstance();
					}
					//如果当前标签为我们传入的内层根标签，说明Bean需要实例化出来了
					if (tagName.equals(beanRoot)) {
						//将Bean实例化出来
						bean = beanClazz.newInstance();
					}
					break;
				//结束标签
				case XmlPullParser.END_TAG:
					//如果当前标签为</item>
					if (listRoot.equalsIgnoreCase(parser.getName())) {
						//如果ListBean不为空
						if (t != null) {
							//保存到list中，同时也保存到了result中，因为list已经是保存在result中了，
							//只不过刚才没有值，现在有值了
							list.add(t);
							//并且把ListBean置空，因为后续还有好多个item
							t = null;
						}
					//如果当前标签为</root>
					}else if (beanRoot.equalsIgnoreCase(parser.getName())) {
						//将Bean保存到result中
						result.setBean(bean);
					}
					break;
				}
				//移动到下一个标签
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果计数器为0说明没有解析到任何数据
		if (count == 0) {
			//将result置空就可以了
			result = null;
		}
		//将result返回
		return result;
		
	}
	public static boolean parseXMLhasthisPeople(String xml){
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if(n2.getNodeName().equals("AppType")){
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								if(n3.getNodeName().equals("ReturnID")){
									if (!n3.getTextContent().trim().equals("0"))
										return false;
									else
										return true;
								}
							}
						}
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static String parseXMLtoShequcode(String xml){
		String code="";
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {

										if("社区代码".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											code =n4.getTextContent();
										}
									}
								}
							}
						}

					}else if(n2.getNodeName().equals("AppType")){
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
							}
						}
					}else if(n2.getNodeName().equals("msg")){
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}
	public static People parseXMLtoPeople(String xml){
		People people=new People();
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						String huji1="",huji2="",zanzhu1="",zanzhu2="",bianhao="",sex="",danweidizhi="",birth="",idcard="",name="",shengao="",phone="",minzu="";
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {
										if("出租屋编号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											bianhao=n4.getTextContent() ;
										}
										if("身高".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											shengao= n4.getTextContent();
										}
										if("户籍地址名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											huji1=n4.getTextContent();
										}
										if("姓名".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											name =n4.getTextContent();
										}
										if("户籍地址详址".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											huji2 =  n4.getTextContent();
										}
										if("名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											zanzhu1 = n4.getTextContent();
										}
										if("门牌号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											zanzhu2 = n4.getTextContent();
										}
										if("居民证号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											idcard = n4.getTextContent();
										}
										if("出生日期".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											birth =n4.getTextContent();
										}
										if("服务处所".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											danweidizhi =n4.getTextContent();
										}
										if("性别".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											sex =n4.getTextContent().equals("1")?"男":"女";
										}
										if("个人联系电话".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											phone =n4.getTextContent();
										}
										if("民族".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											minzu =n4.getTextContent().trim();

										}
									}
								}
							}
						}
						people.setResidentAddress(zanzhu1+zanzhu2);
						people.setHomecode(bianhao);
						people.setAddress(huji1+huji2);
						people.setName(name);
						people.setCardno(idcard);
						people.setBirthday(birth);
						people.setPhone(phone);
						people.setSeviceAddress(danweidizhi);
						people.setSex(sex);
						people.setHeight(shengao);
						for (int m = 0; m<Constants.MINZU_CODE.length; m++) {

							if(minzu.equals(Constants.MINZU_CODE[m])){
								minzu=Constants.MINZU[m];
								Log.d("minzu3",minzu);
							}
						}
						people.setPeople(minzu);
					}else if(n2.getNodeName().equals("AppType")){
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
							}
						}
					}else if(n2.getNodeName().equals("msg")){
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return people;
	}
	public static String parseXMLtohuji2(String xml){
		String str="";
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {

										if("户籍地址详址".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											str =  n4.getTextContent();
										}

									}
								}
							}
						}
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static String parseXMLtohuji1(String xml){
		String str="";
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {

										if("户籍地址名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											str =  n4.getTextContent();
										}

									}
								}
							}
						}
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static  String parseXML(String xml){
		String str="";
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						String huji1="",huji2="",zanzhu1="",zanzhu2="",name="",dianhua="",danweidizhi="",caijiren="",mocicaijishijian="",shifoulingzheng="",shequ="",currentstatus="";
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {
										if("姓名".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											name= "姓名:"+n4.getTextContent() + "\n";
										}
										if("个人联系电话".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											dianhua= "联系电话:"+n4.getTextContent() + "\n";
										}
										if("户籍地址名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											huji1= "户籍地址:"+n4.getTextContent();
										}
										if("居住证申请时间".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											shifoulingzheng =  "是否办证:"+(n4.getTextContent().equals("")?"否":"是") + "\n";
										}
										if("户籍地址详址".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											huji2 =  n4.getTextContent() + "\n";
										}
										if("名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											zanzhu1 = "暂住地址:"+ n4.getTextContent();
										}
										if("门牌号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											zanzhu2 = n4.getTextContent() + "\n";
										}
										if("更新时间".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											mocicaijishijian =  "末次操作时间:"+n4.getTextContent() + "\n";
										}
										if("设备识别号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											caijiren ="采集人:"+n4.getTextContent() + "\n";
										}
										if("服务处所".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											danweidizhi ="工作单位:"+n4.getTextContent() + "\n";
										}
										if("社区名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//											shequ ="初次办证社区:"+n4.getTextContent() + "\n";
											shequ ="初次办证社区:" + "\n";
										}
										if("注销日期".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											currentstatus ="当前状态:"+(n4.getTextContent().trim().equals("")?"在住":"搬离")+ "\n";
										}
//										str=str+n4.getAttributes().getNamedItem("name").getNodeValue()+":"+n4.getTextContent()+"\n";
									}
								}
							}
						}
						str=shifoulingzheng+name+huji1+huji2+zanzhu1+zanzhu2+dianhua+danweidizhi+mocicaijishijian+shequ+currentstatus;
					}else if(n2.getNodeName().equals("AppType")){
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
							}
						}
					}else if(n2.getNodeName().equals("msg")){
						str=str+n2.getTextContent();
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static SQBean parseXMLshequCode(String xml){
		SQBean str=new SQBean();
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {
										if("社区代码".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											str.setSqcode(n4.getTextContent().trim());
										}
										if("社区名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											str.setSqname(n4.getTextContent().trim());
										}
									}
								}
							}
						}
					}else if(n2.getNodeName().equals("AppType")){
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
							}
						}
					}else if(n2.getNodeName().equals("msg")){
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static  boolean parseXMLIsDengji(String xml){
		boolean str=false;
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {

										if("居住证申请时间".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											str=n4.getTextContent().equals("")?false:true;
										}

									}
								}
							}
						}
					}else if(n2.getNodeName().equals("AppType")){
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
							}
						}
					}else if(n2.getNodeName().equals("msg")){
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static  boolean parseXMLIsBanzheng(String xml){
		boolean str=false;
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {

										if("姓名".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											str=n4.getTextContent().equals("")?false:true;
										}

									}
								}
							}
						}
					}else if(n2.getNodeName().equals("AppType")){
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
							}
						}
					}else if(n2.getNodeName().equals("msg")){
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static  String isPhoto(String xml){
		String str="";
		xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
			Node n = d.getChildNodes().item(0);
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n2 = nl.item(i);
				if (n2.getNodeType() == Node.ELEMENT_NODE) {
					if (n2.getNodeName().equals("ResultSet")) {
						String isphoto="";
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
								NodeList nl3 = n3.getChildNodes();
								for (int k = 0; k < nl3.getLength(); k++) {
									Node n4 = nl3.item(k);
									if (n4.getNodeType() == Node.ELEMENT_NODE) {
//
										if("ISPHOTO".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											isphoto =n4.getTextContent();

										}

									}
								}
							}
						}
						str=isphoto;
					}else if(n2.getNodeName().equals("AppType")){
						NodeList nl2 = n2.getChildNodes();
						for (int j = 0; j < nl2.getLength(); j++) {
							Node n3 = nl2.item(j);
							if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
							}
						}
					}else if(n2.getNodeName().equals("msg")){
						str=str+n2.getTextContent();
					}
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
    public static  String parseBanZhengSheQuXML(String xml){
        String str="";
        xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
            Node n = d.getChildNodes().item(0);
            NodeList nl = n.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n2 = nl.item(i);
                if (n2.getNodeType() == Node.ELEMENT_NODE) {
                    if (n2.getNodeName().equals("ResultSet")) {
                        String huji1="",huji2="",zanzhu1="",zanzhu2="",name="",dianhua="",danweidizhi="",caijiren="",mocicaijishijian="",shifoulingzheng="",shequ="",currentstatus="";
                        NodeList nl2 = n2.getChildNodes();
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                NodeList nl3 = n3.getChildNodes();
                                for (int k = 0; k < nl3.getLength(); k++) {
                                    Node n4 = nl3.item(k);
                                    if (n4.getNodeType() == Node.ELEMENT_NODE) {
//                                        if("姓名".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            name= "姓名:"+n4.getTextContent() + "\n";
//                                        }
//                                        if("个人联系电话".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            dianhua= "联系电话:"+n4.getTextContent() + "\n";
//                                        }
//                                        if("户籍地址名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            huji1= "户籍地址:"+n4.getTextContent();
//                                        }
//                                        if("居住证申请时间".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            shifoulingzheng =  "是否办证:"+(n4.getTextContent().equals("")?"否":"是") + "\n";
//                                        }
//                                        if("户籍地址详址".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            huji2 =  n4.getTextContent() + "\n";
//                                        }
//                                        if("名称".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            zanzhu1 = "暂住地址:"+ n4.getTextContent();
//                                        }
//                                        if("门牌号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            zanzhu2 = n4.getTextContent() + "\n";
//                                        }
//                                        if("更新时间".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            mocicaijishijian =  "末次采集时间:"+n4.getTextContent() + "\n";
//                                        }
//                                        if("设备识别号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            caijiren ="采集人:"+n4.getTextContent() + "\n";
//                                        }
//                                        if("服务处所".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            danweidizhi ="工作单位:"+n4.getTextContent() + "\n";
//                                        }
                                        if("社区".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
											shequ ="初次办证社区:"+n4.getTextContent();
//                                            shequ ="初次办证社区:" + "\n";
                                        }
//                                        if("注销日期".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
//                                            currentstatus ="当前状态:"+(n4.getTextContent().trim().equals("")?"在住":"搬离")+ "\n";
//                                        }
//										str=str+n4.getAttributes().getNamedItem("name").getNodeValue()+":"+n4.getTextContent()+"\n";
                                    }
                                }
                            }
                        }
                        str=shequ;
                    }else if(n2.getNodeName().equals("AppType")){
                        NodeList nl2 = n2.getChildNodes();
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
                            }
                        }
                    }else if(n2.getNodeName().equals("msg")){
                        str=str+n2.getTextContent();
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static  String parseChangKouXML(String xml){
        String str="";
        xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(CommonUtils.writeTxtToFile(xml, Constants.DB_PATH,"xml.xml"));
            Node n = d.getChildNodes().item(0);
            NodeList nl = n.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n2 = nl.item(i);
                if (n2.getNodeType() == Node.ELEMENT_NODE) {
                    if (n2.getNodeName().equals("ResultSet")) {
                        String huji="";
                        NodeList nl2 = n2.getChildNodes();
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                NodeList nl3 = n3.getChildNodes();
                                for (int k = 0; k < nl3.getLength(); k++) {
                                    Node n4 = nl3.item(k);
                                    if (n4.getNodeType() == Node.ELEMENT_NODE) {
//
                                        if("SSQX".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            huji =n4.getTextContent();
                                        }
//
                                    }
                                }
                            }
                        }
                        str=huji;
                    }else if(n2.getNodeName().equals("AppType")){
                        NodeList nl2 = n2.getChildNodes();
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
                            }
                        }
                    }else if(n2.getNodeName().equals("msg")){
                        str=str+n2.getTextContent();
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
	public static void createXml(People people,Activity context) {
		XmlSerializer serializer = Xml.newSerializer();// xml文件生成器
		File file = new File(Constants.DB_PATH,people.uuid+".xml");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			serializer.setOutput(fos, "utf-8");// 为xml生成器设置输出流和字符编码
//        serializer.startDocument("utf-8", true);// 开始文档，参数分别为字符编码和是否保持独立
			serializer.startTag(null, "xml_reco"); // 开始标签,参数分别为：命名空间和标签名
			if (people.module.equals("注销")) {
				serializer.startTag(null, "module");// 开始标签
				serializer.text(people.module);// 文本内容
				serializer.endTag(null, "module");// 结束标签

				serializer.startTag(null, "user_id");
				serializer.text(people.user_id);
				serializer.endTag(null, "user_id");

				serializer.startTag(null, "name");
				serializer.text(people.name);
				serializer.endTag(null, "name");

				serializer.startTag(null, "cardno");
				serializer.text(people.cardno);
				serializer.endTag(null, "cardno");

				serializer.startTag(null, "Roomcode");
				serializer.text(people.Roomcode);
				serializer.endTag(null, "Roomcode");

				serializer.startTag(null, "Residentcode");
				serializer.text(people.homecode);
				serializer.endTag(null, "Residentcode");

				serializer.startTag(null, "ResidentAddress");
				serializer.text(people.ResidentAddress);
				serializer.endTag(null, "ResidentAddress");

				serializer.startTag(null, "collect_datetime");
				serializer.text(people.collect_datetime);
				serializer.endTag(null, "collect_datetime");

				serializer.startTag(null, "sbsbh");
				serializer.text( CommonUtils.getIMEI(context));
				serializer.endTag(null, "sbsbh");
			}else if(people.module.equals("补")){
				serializer.startTag(null, "module");// 开始标签
				serializer.text(people.module);// 文本内容
				serializer.endTag(null, "module");// 结束标签

				serializer.startTag(null, "user_id");
				serializer.text(people.user_id);
				serializer.endTag(null, "user_id");

				serializer.startTag(null, "cardno");
				serializer.text(people.cardno);
				serializer.endTag(null, "cardno");

				serializer.startTag(null, "collect_datetime");
				serializer.text(people.collect_datetime);
				serializer.endTag(null, "collect_datetime");

				serializer.startTag(null, "picture");
				serializer.text(people.picture);
				serializer.endTag(null, "picture");

				serializer.startTag(null, "phototype");
				serializer.text("1");
				serializer.endTag(null, "phototype");
			}else{
				serializer.startTag(null, "module");// 开始标签
				serializer.text(people.module);// 文本内容
				serializer.endTag(null, "module");// 结束标签

				serializer.startTag(null, "card_type");
				serializer.text(people.card_type);
				serializer.endTag(null, "card_type");

				serializer.startTag(null, "user_id");
				serializer.text(people.user_id);
				serializer.endTag(null, "user_id");

				serializer.startTag(null, "name");
				serializer.text(people.name);
				serializer.endTag(null, "name");

				serializer.startTag(null, "formername");
				serializer.text(people.formername);
				serializer.endTag(null, "formername");

				serializer.startTag(null, "cbqk");
				serializer.text(people.cbqk);
				serializer.endTag(null, "cbqk");

				serializer.startTag(null, "sex");
				serializer.text(people.sex);
				serializer.endTag(null, "sex");

				serializer.startTag(null, "people");
				serializer.text(people.people);
				serializer.endTag(null, "people");

				serializer.startTag(null, "cardno");
				serializer.text(people.cardno);
				serializer.endTag(null, "cardno");

				serializer.startTag(null, "hjdxz");
				serializer.text(people.hjdxz);
				serializer.endTag(null, "hjdxz");

				serializer.startTag(null, "address");
				serializer.text(people.address);
				serializer.endTag(null, "address");

				serializer.startTag(null, "birthday");
				serializer.text(people.birthday);
				serializer.endTag(null, "birthday");

				serializer.startTag(null, "picture");
				serializer.text(people.picture);
				serializer.endTag(null, "picture");

				serializer.startTag(null, "is_jzz");
				serializer.text(people.is_jzz);
				serializer.endTag(null, "is_jzz");

				serializer.startTag(null, "height");
				serializer.text(people.height);
				serializer.endTag(null, "height");

				serializer.startTag(null, "education");
				serializer.text(people.education);
				serializer.endTag(null, "education");

				serializer.startTag(null, "marriage");
				serializer.text(people.marriage);
				serializer.endTag(null, "marriage");

				serializer.startTag(null, "landscape");
				serializer.text(people.landscape);
				serializer.endTag(null, "landscape");

				serializer.startTag(null, "homecode");
				serializer.text(people.homecode);
				serializer.endTag(null, "homecode");

				serializer.startTag(null, "ResidentAddress");
				serializer.text(people.ResidentAddress);
				serializer.endTag(null, "ResidentAddress");

				serializer.startTag(null, "Roomcode");
				serializer.text(people.Roomcode);
				serializer.endTag(null, "Roomcode");

				serializer.startTag(null, "RoomType");
				serializer.text(people.RoomType);
				serializer.endTag(null, "RoomType");

				serializer.startTag(null, "ResidentReason");
				serializer.text(people.ResidentReason);
				serializer.endTag(null, "ResidentReason");

				serializer.startTag(null, "SeviceAddress");
				serializer.text(people.SeviceAddress);
				serializer.endTag(null, "SeviceAddress");

				serializer.startTag(null, "Note");
				serializer.text(people.Note);
				serializer.endTag(null, "Note");

				serializer.startTag(null, "PositionName");
				serializer.text(people.PositionName);
				serializer.endTag(null, "PositionName");

				serializer.startTag(null, "IsInsured");
				serializer.text(people.IsInsured);
				serializer.endTag(null, "IsInsured");

				serializer.startTag(null, "ChanBaoShiJian");
				serializer.text(people.ChanBaoShiJian );
				serializer.endTag(null, "ChanBaoShiJian");

				serializer.startTag(null, "Phone");
				serializer.text(people.Phone);
				serializer.endTag(null, "Phone");

				serializer.startTag(null, "FuBingYi");
				serializer.text(people.FuBingYi);
				serializer.endTag(null, "FuBingYi");

				serializer.startTag(null, "JuZhuLeiBie");
				serializer.text(people.JuZhuLeiBie);
				serializer.endTag(null, "JuZhuLeiBie");

				serializer.startTag(null, "JuZhuFangShi");
				serializer.text(people.JuZhuFangShi);
				serializer.endTag(null, "JuZhuFangShi");

				serializer.startTag(null, "FangDongGuanXi");
				serializer.text(people.FangDongGuanXi);
				serializer.endTag(null, "FangDongGuanXi");

				serializer.startTag(null, "FuQiTongXing");
				serializer.text(people.FuQiTongXing);
				serializer.endTag(null, "FuQiTongXing");

				serializer.startTag(null, "ShengYuQingKuang");
				serializer.text(people.ShengYuQingKuang);
				serializer.endTag(null, "ShengYuQingKuang");

				serializer.startTag(null, "childNum");
				serializer.text(people.childNum);
				serializer.endTag(null, "childNum");

				serializer.startTag(null, "gravidity");
				serializer.text(people.gravidity);
				serializer.endTag(null, "gravidity");

				serializer.startTag(null, "BirthCtl");
				serializer.text(people.BirthCtl);
				serializer.endTag(null, "BirthCtl");

				serializer.startTag(null, "ShiFouLingZheng");
				serializer.text(people.ShiFouLingZheng);
				serializer.endTag(null, "ShiFouLingZheng");

				serializer.startTag(null, "carType");
				serializer.text(people.carType);
				serializer.endTag(null, "carType");

				serializer.startTag(null, "carNo");
				serializer.text(people.carNo );
				serializer.endTag(null, "carNo");

				serializer.startTag(null, "Rent");
				serializer.text(people.Rent);
				serializer.endTag(null, "Rent");

				serializer.startTag(null, "QQ");
				serializer.text(people.QQ);
				serializer.endTag(null, "QQ");

				serializer.startTag(null, "MAC_id");
				serializer.text(people.MAC_id);
				serializer.endTag(null, "MAC_id");


				serializer.startTag(null, "JieYuCuoShi");
				serializer.text(people.JieYuCuoShi);
				serializer.endTag(null, "JieYuCuoShi");

				serializer.startTag(null, "sq_name");
				serializer.text(people.sq_name);
				serializer.endTag(null, "sq_name");

				serializer.startTag(null, "LingQuFangShi");
				serializer.text(people.LingQuFangShi);
				serializer.endTag(null, "LingQuFangShi");

				serializer.startTag(null, "JuZhuShiJian");
				serializer.text(people.JuZhuShiJian);
				serializer.endTag(null, "JuZhuShiJian");

				serializer.startTag(null, "ShenQingLeiBie");
				serializer.text(people.ShenQingLeiBie);
				serializer.endTag(null, "ShenQingLeiBie");

				serializer.startTag(null, "ShouJiXingHao");
				serializer.text(people.ShouJiXingHao);
				serializer.endTag(null, "ShouJiXingHao");

				serializer.startTag(null, "ShouJiChuanHao");
				serializer.text(people.ShouJiChuanHao);
				serializer.endTag(null, "ShouJiChuanHao");

				serializer.startTag(null, "BeiYong");
				serializer.text(people.BeiYong);
				serializer.endTag(null, "BeiYong");

				serializer.startTag(null, "phototype");
				serializer.text("1");
				serializer.endTag(null, "phototype");
				//新增xml部分
				serializer.startTag(null, "MSN");
				serializer.text(people.MSN);
				serializer.endTag(null, "MSN");

				serializer.startTag(null, "Email");
				serializer.text(people.Email);
				serializer.endTag(null, "Email");

				serializer.startTag(null, "czwxz");
				serializer.text(people.czwxz);
				serializer.endTag(null, "czwxz");

				serializer.startTag(null, "lsrq");
				serializer.text(people.lsrq);
				serializer.endTag(null, "lsrq");

				serializer.startTag(null, "djrq");
				serializer.text(people.djrq);
				serializer.endTag(null, "djrq");

				serializer.startTag(null, "fzxm");
				serializer.text(people.fzxm);
				serializer.endTag(null, "fzxm");

				serializer.startTag(null, "fzdh");
				serializer.text(people.fzdh);
				serializer.endTag(null, "fzdh");

				serializer.startTag(null, "fzsfz");
				serializer.text(people.fzsfz);
				serializer.endTag(null, "fzsfz");

				serializer.startTag(null, "dwlxdh");
				serializer.text(people.dwlxdh);
				serializer.endTag(null, "dwlxdh");

				serializer.startTag(null, "zymc");
				serializer.text(people.zymc);
				serializer.endTag(null, "zymc");

				serializer.startTag(null, "ldhtqj");
				serializer.text(people.ldhtqj);
				serializer.endTag(null, "ldhtqj");

				serializer.startTag(null, "sbbh");
				serializer.text(people.sbbh);
				serializer.endTag(null, "sbbh");

				serializer.startTag(null, "jyrq");
				serializer.text(people.jyrq);
				serializer.endTag(null, "jyrq");

				serializer.startTag(null, "jkzbh");
				serializer.text(people.jkzbh);
				serializer.endTag(null, "jkzbh");

				serializer.startTag(null, "jkzbh");
				serializer.text(people.jkzbh);
				serializer.endTag(null, "jkzbh");

				serializer.startTag(null, "dwfzr");
				serializer.text(people.dwfzr);
				serializer.endTag(null, "dwfzr");

				serializer.startTag(null, "sfjy");
				serializer.text("1");
				serializer.endTag(null, "sfjy");

				serializer.startTag(null, "hyzmzl");
				serializer.text(people.hyzmzl);
				serializer.endTag(null, "hyzmzl");

				serializer.startTag(null, "hyzmbh");
				serializer.text(people.hyzmbh);
				serializer.endTag(null, "hyzmbh");

				serializer.startTag(null, "hyqfrq");
				serializer.text(people.hyqfrq);
				serializer.endTag(null, "hyqfrq");

				serializer.startTag(null, "jqjzym");
				serializer.text("0");
				serializer.endTag(null, "jqjzym");

				serializer.startTag(null, "jhrq");
				serializer.text(people.jhrq);
				serializer.endTag(null, "jhrq");

				serializer.startTag(null, "yfjzzh");
				serializer.text(people.yfjzzh);
				serializer.endTag(null, "yfjzzh");

				serializer.startTag(null, "dsznz");
				serializer.text(people.dsznz);
				serializer.endTag(null, "dsznz");

				serializer.startTag(null, "fwkh");
				serializer.text(people.fwkh);
				serializer.endTag(null, "fwkh");

				serializer.startTag(null, "bycsrq");
				serializer.text(people.bycsrq);
				serializer.endTag(null, "bycsrq");

				serializer.startTag(null, "czqx1");
				serializer.text(people.czqx1);
				serializer.endTag(null, "czqx1");

				serializer.startTag(null, "czqx2");
				serializer.text(people.czqx2);
				serializer.endTag(null, "czqx2");

				serializer.startTag(null, "zjdq");
				serializer.text("20161101");
				serializer.endTag(null, "zjdq");

				serializer.startTag(null, "beizhu2");
				serializer.text(people.beizhu2 );
				serializer.endTag(null, "beizhu2");

				serializer.startTag(null, "sbsbh");
				serializer.text( CommonUtils.getIMEI(context));
				serializer.endTag(null, "sbsbh");

				serializer.startTag(null, "xdr1");
				serializer.text(people.xdr1);
				serializer.endTag(null, "xdr1");

				serializer.startTag(null, "xdxm1");
				serializer.text(people.xdxm1);
				serializer.endTag(null, "xdxm1");

				serializer.startTag(null, "xdxb1");
				serializer.text(people.xdxb1);
				serializer.endTag(null, "xdxb1");

				serializer.startTag(null, "xdrq1");
				serializer.text(people.xdrq1);
				serializer.endTag(null, "xdrq1");

				serializer.startTag(null, "xdsfz1");
				serializer.text(people.xdsfz1);
				serializer.endTag(null, "xdsfz1");

				serializer.startTag(null, "xdr2");
				serializer.text(people.xdr2);
				serializer.endTag(null, "xdr2");

				serializer.startTag(null, "xdxm2");
				serializer.text(people.xdxm2);
				serializer.endTag(null, "xdxm2");

				serializer.startTag(null, "xdxb2");
				serializer.text(people.xdxb2);
				serializer.endTag(null, "xdxb2");

				serializer.startTag(null, "xdrq2");
				serializer.text(people.xdrq2);
				serializer.endTag(null, "xdrq2");

				serializer.startTag(null, "xdsfz2");
				serializer.text(people.xdsfz2);
				serializer.endTag(null, "xdsfz2");

				serializer.startTag(null, "xdr3");
				serializer.text(people.xdr3);
				serializer.endTag(null, "xdr3");

				serializer.startTag(null, "xdxm3");
				serializer.text(people.xdxm3);
				serializer.endTag(null, "xdxm3");

				serializer.startTag(null, "xdxb3");
				serializer.text(people.xdxb3);
				serializer.endTag(null, "xdxb3");

				serializer.startTag(null, "xdrq3");
				serializer.text(people.xdrq3);
				serializer.endTag(null, "xdrq3");

				serializer.startTag(null, "xdsfz3");
				serializer.text(people.xdsfz3);
				serializer.endTag(null, "xdsfz3");

				serializer.startTag(null, "xdr4");
				serializer.text(people.xdr4);
				serializer.endTag(null, "xdr4");

				serializer.startTag(null, "xdxm4");
				serializer.text(people.xdxm4);
				serializer.endTag(null, "xdxm4");

				serializer.startTag(null, "xdxb4");
				serializer.text(people.xdxb4);
				serializer.endTag(null, "xdxb4");

				serializer.startTag(null, "xdrq4");
				serializer.text(people.xdrq4);
				serializer.endTag(null, "xdrq4");

				serializer.startTag(null, "xdsfz4");
				serializer.text(people.xdsfz4);
				serializer.endTag(null, "xdsfz4");

				serializer.startTag(null, "gxr1");
				serializer.text(people.gxr1);
				serializer.endTag(null, "gxr1");

				serializer.startTag(null, "gxxm1");
				serializer.text(people.gxxm1);
				serializer.endTag(null, "gxxm1");

				serializer.startTag(null, "gxxb1");
				serializer.text(people.gxxb1);
				serializer.endTag(null, "gxxb1");

				serializer.startTag(null, "gxrq1");
				serializer.text(people.gxrq1);
				serializer.endTag(null, "gxrq1");

				serializer.startTag(null, "gxsfz1");
				serializer.text(people.gxsfz1);
				serializer.endTag(null, "gxsfz1");

				serializer.startTag(null, "gxrjzk1");
				serializer.text(people.gxrjzk1);
				serializer.endTag(null, "gxrjzk1");

				serializer.startTag(null, "gxr2");
				serializer.text(people.gxr2);
				serializer.endTag(null, "gxr2");

				serializer.startTag(null, "gxxm2");
				serializer.text(people.gxxm2);
				serializer.endTag(null, "gxxm2");

				serializer.startTag(null, "gxxb2");
				serializer.text(people.gxxb2);
				serializer.endTag(null, "gxxb2");

				serializer.startTag(null, "gxrq2");
				serializer.text(people.gxrq2);
				serializer.endTag(null, "gxrq2");

				serializer.startTag(null, "gxsfz2");
				serializer.text(people.gxsfz2);
				serializer.endTag(null, "gxsfz2");

				serializer.startTag(null, "gxrjzk2");
				serializer.text(people.gxrjzk2);
				serializer.endTag(null, "gxrjzk2");

				serializer.startTag(null, "gxr3");
				serializer.text(people.gxr3);
				serializer.endTag(null, "gxr3");

				serializer.startTag(null, "gxxm3");
				serializer.text(people.gxxm3);
				serializer.endTag(null, "gxxm3");

				serializer.startTag(null, "gxxb3");
				serializer.text(people.gxxb3);
				serializer.endTag(null, "gxxb3");

				serializer.startTag(null, "gxrq3");
				serializer.text(people.gxrq3);
				serializer.endTag(null, "gxrq3");

				serializer.startTag(null, "gxsfz3");
				serializer.text(people.gxsfz3);
				serializer.endTag(null, "gxsfz3");

				serializer.startTag(null, "gxrjzk3");
				serializer.text(people.gxrjzk3);
				serializer.endTag(null, "gxrjzk3");

				serializer.startTag(null, "gxr4");
				serializer.text(people.gxr4);
				serializer.endTag(null, "gxr4");

				serializer.startTag(null, "gxxm4");
				serializer.text(people.gxxm4);
				serializer.endTag(null, "gxxm4");

				serializer.startTag(null, "gxxb4");
				serializer.text(people.gxxb4);
				serializer.endTag(null, "gxxb4");

				serializer.startTag(null, "gxrq4");
				serializer.text(people.gxrq4);
				serializer.endTag(null, "gxrq4");

				serializer.startTag(null, "gxsfz4");
				serializer.text(people.gxsfz4);
				serializer.endTag(null, "gxsfz4");

				serializer.startTag(null, "gxrjzk4");
				serializer.text(people.gxrjzk4);
				serializer.endTag(null, "gxrjzk4");
			}

			serializer.endTag(null, "xml_reco");// 结束标签
			serializer.endDocument();// 结束xml文档
		} catch (Exception e) {
			Toast.makeText(context, "XML生成失败！", Toast.LENGTH_SHORT);
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}