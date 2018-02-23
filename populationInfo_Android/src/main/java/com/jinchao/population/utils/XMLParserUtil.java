package com.jinchao.population.utils;

import android.util.Log;

import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.HouseInfoBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.entity.TrackingBean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by OfferJiShu01 on 2017/3/13.
 */

public class XMLParserUtil {
    public interface OnXmlParserListener{
        void success(String result);
        void fail(String error);
    }
    public interface OnXmlParserToTrackListener{
        void success(TrackingBean result);
        void fail(String error);
    }
    public static void parseXMLforReportLoss(String xml,OnXmlParserListener onXmlParserListener){
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
                                    }
                                }
                            }
                        }
                    }else if(n2.getNodeName().equals("AppType")){
                        NodeList nl2 = n2.getChildNodes();
                        boolean isSuccess=true;
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                if(n3.getNodeName().equals("ReturnID")){
                                    if (n3.getTextContent().trim().equals("0")){
                                        onXmlParserListener.success("挂失成功！");
                                    }else{
                                        isSuccess=false;
                                    }
                                }
                                if(n3.getNodeName().equals("ReturnMessage")){
                                    if (!isSuccess) {
                                        onXmlParserListener.fail(n3.getTextContent());
                                    }
                                }
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
    }
    public static List<String> parseXMLtoRooms(String xml){
        List<String> list=new ArrayList<>();
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
                                        if("租住室号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            list.add(n4.getTextContent() );
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
        return list;
    }

    public static String parseXMLtoRenYuanInHouse(String xml,String houseCode,String houseAddress){
        JSONObject data=new JSONObject();
        JSONObject jsonObject=new JSONObject();
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
                        jsonObject.put("house_exist","1");
                        jsonObject.put("people_exist","1");
                        JSONArray jsonArray=new JSONArray();
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                NodeList nl3 = n3.getChildNodes();
                                JSONObject jsonObjectPeople=new JSONObject();
                                for (int k = 0; k < nl3.getLength(); k++) {//row
                                    Node n4 = nl3.item(k);
                                    if (n4.getNodeType() == Node.ELEMENT_NODE) {
                                        if("人员身份证号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            jsonObjectPeople.put("idcard",n4.getTextContent());
                                        }
                                        if("人员姓名".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            jsonObjectPeople.put("sname",n4.getTextContent());

                                        }
                                        if("日期".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            jsonObjectPeople.put("write_time",n4.getTextContent());
                                        }
                                        if("操作类型".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            jsonObjectPeople.put("resdients_status",n4.getTextContent());
                                        }
                                        jsonObjectPeople.put("house_addr",houseAddress);
                                        jsonObjectPeople.put("house_code",houseCode);
                                        jsonObjectPeople.put("shihao","");
                                    }
                                }
                                jsonArray.put(jsonObjectPeople);
                            }

                        }
                        jsonObject.put("peoplelist",jsonArray);
                        data.put("data",jsonObject);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }
    public static String parseXMLtoHistory(String xml,String houseCode,String houseAddress){
        JSONObject data=new JSONObject();
        JSONObject jsonObject=new JSONObject();
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
                        jsonObject.put("house_exist","1");
                        jsonObject.put("people_exist","1");
                        JSONArray jsonArray=new JSONArray();
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                NodeList nl3 = n3.getChildNodes();
                                JSONObject jsonObjectPeople=new JSONObject();
                                for (int k = 0; k < nl3.getLength(); k++) {//row
                                    Node n4 = nl3.item(k);
                                    if (n4.getNodeType() == Node.ELEMENT_NODE) {
                                        if("居民证号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            jsonObjectPeople.put("idcard",n4.getTextContent());
                                        }
                                        if("人员姓名".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            jsonObjectPeople.put("sname",n4.getTextContent());

                                        }
                                        if("日期".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            jsonObjectPeople.put("write_time",n4.getTextContent());
                                        }
                                        if("操作类型".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            jsonObjectPeople.put("resdients_status",n4.getTextContent());
                                        }
                                        jsonObjectPeople.put("house_addr",houseAddress);
                                        jsonObjectPeople.put("house_code",houseCode);
                                        jsonObjectPeople.put("shihao","");
                                    }
                                }
                                jsonArray.put(jsonObjectPeople);
                            }

                        }
                        jsonObject.put("peoplelist",jsonArray);
                        data.put("data",jsonObject);
                    }else if(n2.getNodeName().equals("AppType")){
                        NodeList nl2 = n2.getChildNodes();
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
//                                str=str+n3.getTextContent();
                            }
                        }
                    }else if(n2.getNodeName().equals("msg")){
                        String msgg=n2.getTextContent();
                        if(msgg.equals("没有查询到数据")){
                            jsonObject.put("house_exist","0");
                            jsonObject.put("people_exist","0");
                            data.put("data",jsonObject);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public static HouseInfoBean parseHouseInfor(String xml){
        HouseInfoBean houseinfo=new HouseInfoBean();
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
                                for (int k = 0; k < nl3.getLength(); k++) {//row
                                    Node n4 = nl3.item(k);
                                    if (n4.getNodeType() == Node.ELEMENT_NODE) {
                                        if("出租人姓名".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setFzxm(n4.getTextContent());
                                        }
                                        if("出租人居民证号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setFzsfz(n4.getTextContent());

                                        }
                                        if("联系电话".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setFzdh(n4.getTextContent());
                                        }
                                        if("出租面积".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setJzmj(n4.getTextContent());
                                        }
                                        if("房屋结构".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setFwjg(n4.getTextContent());
                                        }
                                        if("房屋类别".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setFwlx(n4.getTextContent());
                                        }
                                        if("租住户数".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setJzjs(n4.getTextContent());
                                        }
                                        if("房屋用途".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setFwyt(n4.getTextContent());
                                        }
                                        if("租住类型".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setZzlx(n4.getTextContent());
                                        }
                                        if("转租人姓名".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setZzrxm(n4.getTextContent());
                                        }
                                        if("转租人身份证号".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setZzrsfz(n4.getTextContent());
                                        }
                                        if("租住间数".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setJzhs(n4.getTextContent());
                                        }
                                        if("承租途径".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setCztj(n4.getTextContent());
                                        }
                                        if("出租类型".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            houseinfo.setJzlx(n4.getTextContent());
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
                        houseinfo.setMsg(n2.getTextContent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return houseinfo;
    }

    public static void parseXMLtoTRACK(String xml,OnXmlParserToTrackListener onXmlParserListener){
        String str="";
        TrackingBean track=new TrackingBean();
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
                                        if("是否审批".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            track.shenpi=n4.getTextContent().trim();
                                        }
                                        if("是否制证".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            track.zhizheng=n4.getTextContent().trim();
                                        }
                                        if("是否下发".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            track.xiafa=n4.getTextContent().trim();
                                        }
                                        if("是否领证".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            track.lingzheng=n4.getTextContent().trim();
                                        }
                                        if("卡状态".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            track.kastatus=n4.getTextContent().trim();
                                        }
                                        if("操作类型".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            track.caozuo=n4.getTextContent().trim();
                                        }
                                        if("操作时间".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            track.caozuoshijian=n4.getTextContent().trim();
                                        }
                                        if("领证日期".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            track.lingzhengriqi=n4.getTextContent().trim();
                                        }
                                    }
                                }
                            }
                        }
                        onXmlParserListener.success(track);
                    }else if(n2.getNodeName().equals("AppType")){
                        NodeList nl2 = n2.getChildNodes();
                        boolean isSuccess=true;
                        for (int j = 0; j < nl2.getLength(); j++) {
                            Node n3 = nl2.item(j);
                            if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                if(n3.getNodeName().equals("ReturnID")){
                                    if (n3.getTextContent().trim().equals("0")){
//                                        onXmlParserListener.success("挂失成功！");
                                    }else{
                                        isSuccess=false;
                                    }
                                }
                                if(n3.getNodeName().equals("ReturnMessage")){
                                    if (!isSuccess) {
                                        onXmlParserListener.fail(n3.getTextContent());
                                    }
                                }
                            }
                        }
                    }else if(n2.getNodeName().equals("msg")){
                        String msgg=n2.getTextContent().trim();
                        if(msgg.equals("没有查询到数据")){
                            onXmlParserListener.fail("没有查询到数据");
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
    }
}
