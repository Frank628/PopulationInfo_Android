package com.jinchao.population.mainmenu;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;

import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.activity.PersonInHouseActivity;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.NearbyHouseBean;
import com.jinchao.population.entity.RenYuanXinXiBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.CommonHttp;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.DialogShowPic;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.LoadMoreListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.greenrobot.event.EventBus;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

@ContentView(R.layout.activity_searchpeople)
public class SearchPeopleActivity extends BaseReaderActiviy implements  IDReader.IDReaderListener{
	@ViewInject(R.id.rg_search) private RadioGroup rg_search;
	@ViewInject(R.id.edt_content) private EditText edt_content;
//	@ViewInject(R.id.btn_readcard) private Button btn_readcard;
	@ViewInject(R.id.tv_content) private TextView tv_content;
	@ViewInject(R.id.ll_search) private LinearLayout ll_search;
	@ViewInject(R.id.rg_zai) private RadioGroup rg_zai;
	@ViewInject(R.id.rb_banli) private RadioButton rb_banli;
	@ViewInject(R.id.rb_zaizhu) private RadioButton rb_zaizhu;
	@ViewInject(R.id.rb_fangwu) private RadioButton rb_fangwu;
	@ViewInject(R.id.lv) private ListView lv;
	@ViewInject(R.id.btn_seach) private TextView btn_seach;
	@ViewInject(R.id.loadmorelv) private LoadMoreListView loadmorelv;
	@ViewInject(R.id.ll_house) private LinearLayout ll_house;
	@ViewInject(R.id.rotate_header_list_view_frame) private PtrClassicFrameLayout mPtrFrame;
	@ViewInject(R.id.sv_content) private ScrollView sv_content;
	public static final String TAG="IDCARD_DEVICE";
	private RenyuanInHouseBean renyuanInHouseBean;
	private boolean IsHouseID=true;
	private DialogLoading dialogLoading;
	private ResultBeanAndList<RenYuanXinXiBean> renYuanXinXilist;
	private boolean isZaizhu=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("人员信息查询");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		EventBus.getDefault().register(this);
		idReader.setListener(this);
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				getNearbyList();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
			}
		});
		//the following are default settings
		mPtrFrame.setResistance(1.7f);
		mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
		mPtrFrame.setDurationToClose(200);
		mPtrFrame.setDurationToCloseHeader(1000);
		// default is false
		mPtrFrame.setPullToRefresh(false);
		// default is true
		mPtrFrame.setKeepHeaderWhenRefresh(true);

		loadmorelv.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
			@Override
			public void onLoadMore() {

			}
		});
		rg_search.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_fangwu:
					ll_search.setVisibility(View.VISIBLE);
					edt_content.setHint("请输入房屋编号");
//					btn_readcard.setEnabled(false);
					IsHouseID=true;
					edt_content.setText("");
					ll_house.setVisibility(View.GONE);
					sv_content.setVisibility(View.GONE);
					tv_content.setText("");
					mPtrFrame.setVisibility(View.GONE);
					break;
				case R.id.rb_renyuan:
					ll_search.setVisibility(View.VISIBLE);
					edt_content.setHint("请输入身份证号或读卡");
//					btn_readcard.setEnabled(true);
					IsHouseID=false;
					edt_content.setText("");
					tv_content.setText("");
					ll_house.setVisibility(View.GONE);
					sv_content.setVisibility(View.VISIBLE);
					mPtrFrame.setVisibility(View.GONE);
					break;
					case R.id.rb_nearby:
						MyApplication.myApplication.locationService.start();
						ll_house.setVisibility(View.GONE);
						ll_search.setVisibility(View.GONE);
						mPtrFrame.setVisibility(View.VISIBLE);
						mPtrFrame.postDelayed(new Runnable() {
							@Override
							public void run() {
								mPtrFrame.autoRefresh();
							}
						}, 100);

						break;
				default:
					break;
				}
			}
		});
		rg_zai.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_zaizhu:
					isZaizhu=true;
					processData(renyuanInHouseBean);
					break;
				case R.id.rb_banli:
					isZaizhu=false;
					processData(renyuanInHouseBean);
					break;
				default:
					break;
				}
			}
		});
		MyApplication.myApplication.locationService.start();
//		btn_readcard.setEnabled(false);
		dialogLoading=new DialogLoading(this, "数据加载中...",true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RenyuanInHouseBean.RenyuanInhouseOne renyuanInHouseone=(RenyuanInHouseBean.RenyuanInhouseOne) ((ListView)parent).getItemAtPosition(position);
				Intent intent =new Intent(SearchPeopleActivity.this, SearchPeopleDetailActivity.class);
				intent.putExtra("renyuan", renyuanInHouseone);
				startActivity(intent);
			}
		});
		if (getIntent().getStringExtra("housecode")!=null){
			edt_content.setText(getIntent().getStringExtra("housecode"));
			btn_seach.performClick();
		}
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (link != null)
			return;
		Tag nfcTag = null;
		try {
			nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		} catch (Exception e) {

		}
		if (nfcTag != null) {
			link = LinkFactory.createExNFCLink(nfcTag);
			try {
				link.connect();
			} catch (Exception e) {
				showError(e.getMessage());
				try {
					link.disconnect();
				} catch (Exception ex) {
				} finally {
					link = null;
				}
				return;
			}
			idReader.setLink(link);
			showIDCardInfo(true, null,null);
			showProcessDialog("正在读卡中，请稍后");

			idReader.startReadCard();
		}
	}
	 private void showIDCardInfo(boolean isClear,PersonInfo user,String msg)  {//显示身份证数据到界面
		 hideProcessDialog();
		 if (isClear){
			 return;
		 }
		 if (user==null&&msg!=null){
			 showError(msg);
		 }else{
			 edt_content.setText(user.getIdNumber().trim());
		 }
		 isReading = false;

	 }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}



	private void getRequest(String str){
		dialogLoading=new DialogLoading(SearchPeopleActivity.this, "数据加载中...", true);
		dialogLoading.show();
		RequestParams params=new RequestParams(Constants.URL+"quePeople.aspx");
		if (IsHouseID) {
			isZaizhu=true;
			rb_zaizhu.setChecked(true);
			params=new RequestParams(Constants.URL+"HousePosition.aspx?type=get_houseinfor&user_id="+MyInfomationManager.getUserName(this)+"&house_code="+str);
//			params.addBodyParameter("type", "get_houseinfo");
//			params.addBodyParameter("user_id",MyInfomationManager.getUserName(this));
//			params.addBodyParameter("house_code", str);
		}else{
			params.addBodyParameter("type", "get_people");
			params.addBodyParameter("idcard", str);
		}
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				dialogLoading.dismiss();
				try {
					if (IsHouseID) {
						try {
							 renyuanInHouseBean= GsonTools.changeGsonToBean(result,RenyuanInHouseBean.class);
							processData(renyuanInHouseBean);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						ResultBeanAndList<YanZhengBean> yanzhengxml = XmlUtils.getBeanByParseXml(result,"",YanZhengBean.class, "xml_people", YanZhengBean.class);
						YanZhengBean yanZhengBean =(YanZhengBean) yanzhengxml.bean;
						if (yanZhengBean.result.equals("1")) {
							tv_content.setText(yanZhengBean.toString());
						}else if (yanZhengBean.result.equals("0")) {
							tv_content.setText("查无此人");
						}
						Log.d("quePeople", yanzhengxml.toString());
					}
					
				} catch (Exception e) {
					rg_zai.setVisibility(View.GONE);
					lv.setAdapter(null);
					e.printStackTrace();
				}
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				dialogLoading.dismiss();
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {}
		});
	}
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
	@Override
	public void onResume() {
		super.onResume();
		if (renyuanInHouseBean!=null) {
			processData(renyuanInHouseBean);
		}
	}
	private void processData(RenyuanInHouseBean renyuanInHouseBean){
		if (!rb_fangwu.isChecked()){
			return;
		}
		try {
			if (renyuanInHouseBean.data.house_exist.equals("0")){
				sv_content.setVisibility(View.VISIBLE);
				ll_house.setVisibility(View.GONE);
				tv_content.setText("此房屋编号不存在！");
				return;
			}
			if (renyuanInHouseBean.data.people_exist.equals("0")){
				sv_content.setVisibility(View.VISIBLE);
				ll_house.setVisibility(View.GONE);
				tv_content.setText("此房屋没有采集过人！");
				return;
			}
			ll_house.setVisibility(View.VISIBLE);
			sv_content.setVisibility(View.GONE);
			rg_zai.setVisibility(View.VISIBLE);
			List<RenyuanInHouseBean.RenyuanInhouseOne> list=new ArrayList<RenyuanInHouseBean.RenyuanInhouseOne>();
			list.clear();
			if (isZaizhu) {
				for (int i = 0; i <renyuanInHouseBean.data.peoplelist.size(); i++) {
					if (renyuanInHouseBean.data.peoplelist.get(i).resdients_status.equals("在住")) {
						list.add(renyuanInHouseBean.data.peoplelist.get(i));
					}
				}
				rb_zaizhu.setText("在住("+list.size()+"人)");
				rb_banli.setText("搬离("+(renyuanInHouseBean.data.peoplelist.size()-list.size())+"人)");
			}else{
				for (int i = 0; i <renyuanInHouseBean.data.peoplelist.size(); i++) {
					if (renyuanInHouseBean.data.peoplelist.get(i).resdients_status.equals("搬离")) {
						list.add(renyuanInHouseBean.data.peoplelist.get(i));
					}
				}
				rb_banli.setText("搬离("+list.size()+"人)");
				rb_zaizhu.setText("在住("+(renyuanInHouseBean.data.peoplelist.size()-list.size())+"人)");
			}
			if(rb_fangwu.isChecked()){
				rg_zai.setVisibility(View.VISIBLE);
			}
			CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne> adapter =new CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne>(SearchPeopleActivity.this,list,R.layout.item_renyuan) {
				@Override
				public void convert(ViewHolder helper, RenyuanInHouseBean.RenyuanInhouseOne item, int position) {
					helper.setText(R.id.tv_name,"姓名: "+ item.sname);
					DbUtils dbUtils =DeviceUtils.getDbUtils(SearchPeopleActivity.this);
					People people = null;
					try {
						people=dbUtils.findFirst(Selector.from(People.class).where("cardno", "=", item.idcard.trim()));
					} catch (DbException e) {
						e.printStackTrace();
					}
					if (people!=null){
						if (people.module.equals("变更")) {
							helper.setText(R.id.tv_status, "【延期】");
						}else if (people.module.equals("注销")) {
							helper.setText(R.id.tv_status, "【注销】");
						}else{

							String[] split = item.write_time.split("\\s+");
							if (split.length>1) {
								if (isBigerthanElevenandSmallthanTwelve(split[0])) {
									helper.setText(R.id.tv_status, "【"+item.resdients_status+"?】");
								}else{
									helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
								}
							}else{
								helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
							}
						}
					}else{
						String[] split = item.write_time.split("\\s+");
						if (split.length>1) {
							if (isBigerthanElevenandSmallthanTwelve(split[0])) {
								helper.setText(R.id.tv_status, "【"+item.resdients_status+"?】");
							}else{
								helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
							}
						}else{
							helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
						}
					}
					helper.setText(R.id.tv_shihao, "室号: "+item.shihao);
					helper.setText(R.id.tv_time, item.write_time);
				}
			};
			lv.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onEventMainThread(BDLocation location) {

	}

	private void getNearbyList(){
		if (MyApplication.myApplication.myLocation.getLat()==0){
			Toast.makeText(this,"位置信息获取失败，正在为您重新获取！",Toast.LENGTH_SHORT).show();
			MyApplication.myApplication.locationService.start();
			return;
		}
		int databaseType = SharePrefUtil.getInt(this,Constants.DATABASE_TYPE,0);
		if (databaseType==0){
			Toast.makeText(this,"数据库类型未获取，请稍后！",Toast.LENGTH_SHORT).show();
			CommonHttp.getDataBaseType(this);
			return;
		}
		RequestParams params =new RequestParams(Constants.URL+"HousePosition.aspx");
		if (databaseType==1) {//1外来人口，2实有人口
			params.addBodyParameter("type", "get_wlrklist");
		}else {
			params.addBodyParameter("type", "get_syrklist");
		}
		params.addBodyParameter("jd",MyApplication.myApplication.myLocation.getLog()+"");
		params.addBodyParameter("wd",MyApplication.myApplication.myLocation.getLat()+"");
		params.addBodyParameter("userid",MyInfomationManager.getUserID(SearchPeopleActivity.this));
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.i("houselistnearby",result);
				processNearbyHouse(result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Log.i("houselistnearby",ex.getMessage());
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {
				mPtrFrame.refreshComplete();
			}
		});
	}
	private void processNearbyHouse(String result){
		try {
			NearbyHouseBean nearbyHouseBean =GsonTools.changeGsonToBean(result,NearbyHouseBean.class);
			loadmorelv.setTotalNum(nearbyHouseBean.data.size());
			if (nearbyHouseBean.data.size()==0){
				loadmorelv.setNodata("附近暂无房屋",false,null);
			}else{
				CommonAdapter<NearbyHouseBean.NearbyHouseOne> adapter =new CommonAdapter<NearbyHouseBean.NearbyHouseOne>(SearchPeopleActivity.this,nearbyHouseBean.data,R.layout.item_nearby) {
					@Override
					public void convert(ViewHolder helper, final NearbyHouseBean.NearbyHouseOne item, int position) {
						helper.setText(R.id.tv_housecode,"房屋编号："+item.scode);
						helper.setText(R.id.tv_houseaddress,item.address);
						helper.setText(R.id.tv_distance,item.distance);
						helper.getView(R.id.tv_showpic).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								DialogShowPic dialogShowPic =new DialogShowPic(SearchPeopleActivity.this,item.house_pic);
								dialogShowPic.show();
							}
						});
						if (item.house_pic.trim().equals("")){
							helper.getView(R.id.tv_showpic).setVisibility(View.GONE);
						}else{
							helper.getView(R.id.tv_showpic).setVisibility(View.VISIBLE);
						}
					}
				};
				loadmorelv.setAdapter(adapter);
				loadmorelv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
						NearbyHouseBean.NearbyHouseOne  nearbyHouseOne=(NearbyHouseBean.NearbyHouseOne)((ListView)adapterView).getItemAtPosition(i);
						Intent intent=new Intent(SearchPeopleActivity.this, PersonInHouseActivity.class);
						intent.putExtra("id",nearbyHouseOne.scode);
						startActivity(intent);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Event(value={R.id.btn_seach})
	private void seachClick(View view){
		String cardno=edt_content.getText().toString().trim();
		if (IsHouseID) {
			if (cardno.equals("")) {
				Toast.makeText(this, "请输入房屋编号~", Toast.LENGTH_SHORT).show();
				return;
			}
			getRequest(cardno);
		}else{
			if (cardno.equals("")) {
				Toast.makeText(this, "请录入身份证号~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (CommonIdcard.validateCard(cardno)) {
				if (cardno.length() == 15) {
					cardno = CommonIdcard.conver15CardTo18(cardno);
					edt_content.setText(cardno);
	                Toast.makeText(SearchPeopleActivity.this, "15位转18位证件号成功", Toast.LENGTH_SHORT).show();
	            } else if (cardno.length() == 17) {
	            	cardno = CommonIdcard.conver17CardTo18(cardno);
	            	edt_content.setText(cardno);
	                Toast.makeText(SearchPeopleActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
	            }
			}else{
				 Toast.makeText(SearchPeopleActivity.this, "请输入合法身份证！", Toast.LENGTH_SHORT).show();
				 return;
			}

			requestYanZheng(cardno);
		}

	}
	
	public static boolean isBigerthanElevenandSmallthanTwelve(String day) {  
		String[] s=day.split("-");
		String dayf=s[0]+"-"+(s[1].length()==1?("0"+s[1]):s[1])+"-"+(s[2].length()==1?("0"+s[2]):s[2]);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        Date nowDate = null;  
        try {  
            nowDate = df.parse(dayf);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        Date newDate12 = new Date(nowDate.getTime() + (long)365 * 24 * 60 * 60 * 1000);  
        Date newDate11 = new Date(nowDate.getTime() + (long)334 * 24 * 60 * 60 * 1000); 
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String date12 = simpleDateFormat.format(newDate12);  
        String date11 = simpleDateFormat.format(newDate11);
        int falg12=date12.compareTo(simpleDateFormat.format(new Date()));
        int falg11=date11.compareTo(simpleDateFormat.format(new Date()));
        if (falg11<0) {
			return true;
		}else{
			return false; 
		}
         
    }

	@Override
	public void onReadCardSuccess(final PersonInfo personInfo) {
		try {
			link.disconnect();
		} catch (Exception e) {
			Log.e("readcard", e.getMessage());
		} finally {
			link = null;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hideProcessDialog();
				showIDCardInfo(false,personInfo,null);
			}
		});

	}

	@Override
	public void onReadCardFailed(final String s) {
		try {
			link.disconnect();
		} catch (Exception e) {
			Log.e("readcard", e.getMessage());
		} finally {
			link = null;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hideProcessDialog();
				showError(s);
			}
		});

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
