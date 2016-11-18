package com.jinchao.population.mainmenu;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RealHouseBean.RealHouseOne;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.NavigationLayout;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@ContentView(R.layout.activity_zanzhuinfo)
public class ZanZhuActivity extends BaseActiviy{
	@ViewInject(R.id.tv_content)
	private TextView tv_content;
	private People people;
	private DialogLoading dialogLoading;
	private RealHouseOne realHouseOne;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("暂住信息验证");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		people=(People) getIntent().getSerializableExtra("people");
		realHouseOne=(RealHouseOne) getIntent().getSerializableExtra("house");
		dialogLoading=new DialogLoading(this, "信息验证中...",true);
	}
	
//	private void requestYanZheng(String idcard){
//		dialogLoading.show();
//		RequestParams params=new RequestParams(Constants.URL+"quePeople.aspx");
//		params.addBodyParameter("type", "get_people");
//		params.addBodyParameter("idcard", idcard);
//		x.http().post(params, new Callback.CommonCallback<String>() {
//			@Override
//			public void onSuccess(String result) {
//				Log.d("quePeople", result);
//				try {
//					ResultBeanAndList<YanZhengBean> yanzhengxml = XmlUtils.getBeanByParseXml(result,"",YanZhengBean.class, "xml_people", YanZhengBean.class);
//					YanZhengBean yanZhengBean =(YanZhengBean) yanzhengxml.bean;
//					dialogLoading.dismiss();
//					if (yanZhengBean.result.equals("1")) {
//						tv_content.setText(yanZhengBean.toString());
//					}else if (yanZhengBean.result.equals("0")) {
//						handleID(true);
//					}
//					Log.d("quePeople", yanzhengxml.toString());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onError(Throwable ex, boolean isOnCallback) {
//				dialogLoading.dismiss();
//			}
//			@Override
//			public void onCancelled(CancelledException cex) {}
//			@Override
//			public void onFinished() {}
//		});
//	}
private void requestYanZheng(String idcard){
    dialogLoading.show();
    RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=get_people&idcard="+idcard);
    x.http().post(params, new Callback.CommonCallback<String>() {
        @Override
        public void onSuccess(String result) {
            Log.d("quePeople", result);
           tv_content.setText(parseXML(result));

        }
        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            dialogLoading.dismiss();
        }
        @Override
        public void onCancelled(CancelledException cex) {}
        @Override
        public void onFinished() {
            dialogLoading.dismiss();
        }
    });
}
	private void handleID(boolean isHandle) {
		Intent intent = new Intent(ZanZhuActivity.this,HandleIDActivity.class);
		intent.putExtra("people", people);
		intent.putExtra("isHandle", isHandle);
		if (realHouseOne!=null) {
			intent.putExtra("isAdd", getIntent().getBooleanExtra("isAdd",false));
			intent.putExtra("house", realHouseOne);
		}
		startActivity(intent);
		finish();
	}
	@Event(value={R.id.ib_yanzheng})
	private void yaNZheng(View view){
		requestYanZheng(people.cardno);
	}
	@Event(value={R.id.ib_banzheng})
	private void handleId(View view){
		handleID(true);
	}
	@Event(value={R.id.ib_biangeng})
	private void changeId(View view){
		handleID(false);
	}
	@Event(value={R.id.ib_tiaozhuan})
	private void passit(View view){
		handleID(false);
	}

    public  String parseXML(String xml){
        String str="";
        xml=xml.replace("encoding=\"GB2312\"","encoding=\"UTF-8\"");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(CommonUtils.writeTxtToFile(xml,Constants.DB_PATH,"xml.xml"));
            Node n = d.getChildNodes().item(0);
            NodeList nl = n.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n2 = nl.item(i);
                if (n2.getNodeType() == Node.ELEMENT_NODE) {
                    if (n2.getNodeName().equals("ResultSet")) {
                        String huji1="",huji2="",zanzhu1="",zanzhu2="",name="",dianhua="",danweidizhi="",caijiren="",mocicaijishijian="",shifoulingzheng="";
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
                                        if("是否领证".equals( n4.getAttributes().getNamedItem("name").getNodeValue())) {
                                            shifoulingzheng =  "是否领证:"+(n4.getTextContent().equals("1")?"是":"否") + "\n";
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
                                    }
                                }
                            }
                        }
                        str=shifoulingzheng+name+huji1+huji2+zanzhu1+zanzhu2+dianhua+danweidizhi+mocicaijishijian;
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
