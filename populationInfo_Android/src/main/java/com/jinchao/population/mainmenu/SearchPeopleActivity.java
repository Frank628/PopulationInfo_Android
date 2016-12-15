package com.jinchao.population.mainmenu;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jinchao.population.utils.CommonUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonHttp;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.DialogShowPic;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.LoadMoreListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
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
	@ViewInject(R.id.tv_nopeople) private TextView tv_nopeople;
	@ViewInject(R.id.ll_search) private LinearLayout ll_search;
	@ViewInject(R.id.ll_operation) private LinearLayout ll_operation;
	@ViewInject(R.id.btn_add) private Button btn_add;
	@ViewInject(R.id.btn_delay) private Button btn_delay;
	@ViewInject(R.id.btn_logout) private Button btn_logout;
	@ViewInject(R.id.rg_zai) private RadioGroup rg_zai;
	@ViewInject(R.id.rl_zai) private RelativeLayout rl_zai;
	@ViewInject(R.id.rl_search) private RelativeLayout rl_search;
	@ViewInject(R.id.edt_idcard) private EditText edt_idcard;
	@ViewInject(R.id.ib_search) private ImageButton ib_search;
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
	private ResultBeanAndList<RenYuanXinXiBean> renYuanXinXilist;
	private boolean isZaizhu=true,isSearch=false;
	private People peoplereadcard;//读卡读取的人员信息
	private People peoplefromadd;//加入后返回的人员信息
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
				ll_operation.setVisibility(View.GONE);
				switch (checkedId) {
				case R.id.rb_fangwu:
					ll_search.setVisibility(View.VISIBLE);
					ll_operation.setVisibility(View.GONE);
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
		edt_idcard.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				processData(renyuanInHouseBean);
			}
		});
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
			 BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length);
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length).compress(Bitmap.CompressFormat.JPEG, 60, stream);
			 byte[] b = stream.toByteArray();
			 String picture = new String(Base64Coder.encodeLines(b));
			peoplereadcard= new People(user.getName(), user.getIdNumber(), user.getNation(), user.getSex(), user.getBirthday(), user.getAddress(),picture,user.getIdNumber().trim().substring(0, 6),"1",MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this),"");
		 }
		 isReading = false;

	 }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}



	private void getRequest(String str){
		showProcessDialog("数据加载中...");;
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
				hideProcessDialog();
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
					rl_zai.setVisibility(View.GONE);
					lv.setAdapter(null);
					e.printStackTrace();
				}
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				hideProcessDialog();
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {}
		});
	}
    private void requestYanZheng(String idcard){
        showProcessDialog("数据加载中...");
		if(peoplereadcard!=null){
			if (!peoplereadcard.getCardno().trim().equals(idcard)){
				peoplereadcard=null;
			}
		}
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=get_people&idcard="+idcard);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("quePeople", result);
                tv_content.setText(XmlUtils.parseXML(result));
				if (XmlUtils.parseXMLhasthisPeople(result)) {
//					ll_operation.setVisibility(View.VISIBLE);
//					btn_add.setEnabled(true);
//					btn_delay.setEnabled(false);
//					btn_logout.setEnabled(false);
					if (peoplereadcard==null){//如果不是读卡，是输入身份证搜索
						peoplereadcard=XmlUtils.parseXMLtoPeople(result);
					}else{
						People p=XmlUtils.parseXMLtoPeople(result);
						peoplereadcard.setHomecode(p.getHomecode());
						peoplereadcard.setResidentAddress(p.getResidentAddress());
						peoplereadcard.setPhone(p.getPhone());
					}

				}
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
				hideProcessDialog();
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {
                hideProcessDialog();
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
			if (!isSearch){
				rl_zai.setVisibility(View.VISIBLE);
				ib_search.setVisibility(View.VISIBLE);
			}
			tv_nopeople.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
			List<RenyuanInHouseBean.RenyuanInhouseOne> list=new ArrayList<RenyuanInHouseBean.RenyuanInhouseOne>();
			list.clear();
			if(isSearch&&(!TextUtils.isEmpty(edt_idcard.getText().toString().trim()))){
				for (int i = 0; i <renyuanInHouseBean.data.peoplelist.size(); i++) {
					if (renyuanInHouseBean.data.peoplelist.get(i).idcard.trim().contains(edt_idcard.getText().toString().trim())){
						if(renyuanInHouseBean.data.peoplelist.get(i).resdients_status.equals("在住")){
							list.add(renyuanInHouseBean.data.peoplelist.get(i));
						}

					}
				}
				if (list.size()==0){
					tv_nopeople.setVisibility(View.VISIBLE);
					lv.setVisibility(View.GONE);
					tv_nopeople.setText("该房屋无此人");
				}
			}else {
				rl_search.setVisibility(View.GONE);
				if (isZaizhu) {
					for (int i = 0; i < renyuanInHouseBean.data.peoplelist.size(); i++) {
						if (renyuanInHouseBean.data.peoplelist.get(i).resdients_status.equals("在住")) {
							list.add(renyuanInHouseBean.data.peoplelist.get(i));
						}
					}
					rb_zaizhu.setText("在住(" + list.size() + "人)");
					rb_banli.setText("搬离(" + (renyuanInHouseBean.data.peoplelist.size() - list.size()) + "人)");
				} else {
					for (int i = 0; i < renyuanInHouseBean.data.peoplelist.size(); i++) {
						if (renyuanInHouseBean.data.peoplelist.get(i).resdients_status.equals("搬离")) {
							list.add(renyuanInHouseBean.data.peoplelist.get(i));
						}
					}
					rb_banli.setText("搬离(" + list.size() + "人)");
					rb_zaizhu.setText("在住(" + (renyuanInHouseBean.data.peoplelist.size() - list.size()) + "人)");
				}
				if (rb_fangwu.isChecked()) {
					rl_zai.setVisibility(View.VISIBLE);
					ib_search.setVisibility(View.VISIBLE);
				}
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
	@Event(value={R.id.ib_search})
	private void showseachClick(View view){
		rl_search.setVisibility(View.VISIBLE);
		ll_house.setVisibility(View.VISIBLE);
		ib_search.setVisibility(View.GONE);
		rl_zai.setVisibility(View.GONE);
		isSearch=true;
	}
	@Event(value={R.id.tv_cancel})
	private void seachitClick(View view){
		isSearch=false;
		edt_idcard.setText("");
		rl_search.setVisibility(View.GONE);
		rl_zai.setVisibility(View.VISIBLE);
		ib_search.setVisibility(View.VISIBLE);
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
	@Event(value={R.id.btn_add})
	private void addClick(View view){
		Intent intent = new Intent(SearchPeopleActivity.this,HandleIDActivity.class);
		intent.putExtra("people",peoplereadcard );
		intent.putExtra(Constants.Where_from,1);
		intent.putExtra("isHandle", true);
		startActivityForResult(intent,1);
	}
	@Event(value={R.id.btn_delay})
	private void yanqi(View view){
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date =sDateFormat.format(new java.util.Date());
		People people=new People(peoplefromadd.name, peoplefromadd.cardno, peoplefromadd.cardno.substring(0, 6), "变更", CommonUtils.GenerateGUID(), "1", "1",
				MyInfomationManager.getUserName(SearchPeopleActivity.this), "1", peoplefromadd.homecode, peoplefromadd.ResidentAddress, peoplefromadd.Roomcode,
				MyInfomationManager.getSQNAME(SearchPeopleActivity.this),date);
		DbUtils dbUtils =DeviceUtils.getDbUtils(this);
		List<People> list=new ArrayList<People>();
		try {
			list = dbUtils.findAll(Selector.from(People.class).where("cardno", "=", peoplefromadd.cardno));
			if (list!=null) {
				if (list.size()>0) {
					dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", peoplefromadd.cardno));
				}
			}
			dbUtils.save(people);
			Toast.makeText(this, "延期成功~", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "延期失败~", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	@Event(value={R.id.btn_logout})
	private void zhuxiao(View view){
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date =sDateFormat.format(new java.util.Date());
		People people=new People(peoplefromadd.name, date, peoplefromadd.cardno, "注销", CommonUtils.GenerateGUID(), "2",
				MyInfomationManager.getUserName(SearchPeopleActivity.this), peoplereadcard.homecode,peoplereadcard.ResidentAddress);
		DbUtils dbUtils =DeviceUtils.getDbUtils(this);
		List<People> list=new ArrayList<People>();
		try {
			list = dbUtils.findAll(Selector.from(People.class).where("cardno", "=", peoplereadcard.cardno));
			if (list!=null) {
				if (list.size()>0) {
					dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", peoplereadcard.cardno));
				}
			}
			dbUtils.save(people);
			Toast.makeText(this, "注销成功~", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "注销失败~", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode ==RESULT_OK) {
			switch (requestCode) {
				case 1:
					if (data != null) {
						peoplefromadd=(People) data.getSerializableExtra("people");
						if (peoplefromadd!=null){
							XmlUtils.createXml(peoplefromadd,SearchPeopleActivity.this);
							String str = FileUtils.getStringFromFile(new File(Constants.DB_PATH + peoplefromadd.uuid + ".xml"));
							upload2(str,peoplefromadd);
						}
					}
					break;

				default:
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	private void upload2(String str,final People people){
		showProcessDialog("数据上传中...");
		com.lidroid.xutils.http.RequestParams params =new com.lidroid.xutils.http.RequestParams();
		params.addBodyParameter("ActionType", people.actiontype);
		params.addBodyParameter("CollID", MyInfomationManager.getUserName(SearchPeopleActivity.this));
		params.addBodyParameter("IdCard", people.cardno);
		params.addBodyParameter("XmlFileName", people.uuid);
		params.addBodyParameter("XmlFileNameExt", ".xml");
		params.addBodyParameter("XmlBody", str);
		HttpUtils http=new HttpUtils();
		http.configRequestRetryCount(0);
		http.send(HttpMethod.POST, "http://222.92.144.66:90/IDcollect/Import.aspx",params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.d("upload", arg1+"faile");
				hideProcessDialog();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				hideProcessDialog();
				String result=arg0.result.replaceAll("<(.|\n)*?>", "").trim();
				result=result.replaceAll("Import", "");
				if (result.trim().equals("-1")) {
					Log.d("upload", result);
					Toast.makeText(SearchPeopleActivity.this,"上传失败！",Toast.LENGTH_SHORT).show();
				}else{
					btn_delay.setEnabled(true);
					btn_logout.setEnabled(true);
					btn_add.setEnabled(false);
					FileUtils.deleteFile(Constants.DB_PATH+people.uuid+".xml");
					Toast.makeText(SearchPeopleActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
