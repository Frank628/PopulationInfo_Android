package com.jinchao.population.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.jinchao.population.config.Constants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlUtils {
	/**
	 * 解析一个bean和一个list的xml文件结构的方法
	 * @param parser 解析者
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
											mocicaijishijian =  "末次采集时间:"+n4.getTextContent() + "\n";
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
}