package com.jinchao.population.utils;

import com.jinchao.population.config.Constants;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

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
}
