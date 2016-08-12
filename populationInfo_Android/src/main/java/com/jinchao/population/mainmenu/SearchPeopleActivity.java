package com.jinchao.population.mainmenu;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.caihua.cloud.common.User;
import com.caihua.cloud.common.enumerate.ConnectType;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RenYuanXinXiBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.NavigationLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
@ContentView(R.layout.activity_searchpeople)
public class SearchPeopleActivity extends BaseReaderActiviy{
	@ViewInject(R.id.rg_search) private RadioGroup rg_search;
	@ViewInject(R.id.edt_content) private EditText edt_content;
	@ViewInject(R.id.btn_readcard) private Button btn_readcard;
	@ViewInject(R.id.tv_content) private TextView tv_content;
	@ViewInject(R.id.rg_zai) private RadioGroup rg_zai;
	@ViewInject(R.id.rb_banli) private RadioButton rb_banli;
	@ViewInject(R.id.rb_zaizhu) private RadioButton rb_zaizhu;
	@ViewInject(R.id.rb_fangwu) private RadioButton rb_fangwu;
	@ViewInject(R.id.lv) private ListView lv;
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
		idReader = new IDReader(this, mHandler);
		rg_search.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_fangwu:
					edt_content.setHint("请输入房屋编号");
					btn_readcard.setEnabled(false);
					IsHouseID=true;
					edt_content.setText("");
					tv_content.setVisibility(View.GONE);
					tv_content.setText("");
					rg_zai.setVisibility(View.VISIBLE);
					break;
				case R.id.rb_renyuan:
					edt_content.setHint("请输入身份证号或读卡");
					btn_readcard.setEnabled(true);
					IsHouseID=false;
					tv_content.setVisibility(View.VISIBLE);
					rg_zai.setVisibility(View.GONE);
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

		btn_readcard.setEnabled(false);
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
	}

	public void onNewIntent(Intent intent){
		Parcelable parcelable = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (parcelable == null) {
			return;
		}
		// 正在处理上一次读取的数据，不再读取数据
		if (isReading) {
			return;
		}
		isReading = true;
		showProcessDialog("正在读卡中，请稍后");
		idReader.connect(ConnectType.NFC, parcelable);
	}
	 private void showIDCardInfo(boolean isClear,User user)  {//显示身份证数据到界面
		 hideProcessDialog();
		 if (isClear){
			 return;
		 }
		 if (user==null){
			 showError();
		 }else{
			 edt_content.setText(user.id.trim());
		 }
		 isReading = false;

	 }

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case IDReader.CONNECT_SUCCESS:
					break;
				case IDReader.CONNECT_FAILED:
					hideProcessDialog();
					if (idReader.strErrorMsg != null) {
//                        Toast.makeText(getActivity(),"连接失败：" + idReader.strErrorMsg,Toast.LENGTH_SHORT).show();
						String str="未响应，请将身份证紧贴手机背部重试!";
						if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
							str="未响应，请将身份证紧贴读卡器重试!";
						}else if(idReader.getConnectType() == ConnectType.NFC){
							str="未响应，请将身份证紧贴手机背部重试!";
						}else{
							str="读卡失败，"+idReader.strErrorMsg;
						}
						AlertDialog.Builder builder = new AlertDialog.Builder(SearchPeopleActivity.this);
						builder.setMessage(str);
						builder.setTitle("提示");
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.create().show();
					}
					isReading = false;
					if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
						SharePrefUtil.saveString(SearchPeopleActivity.this,"mac",null);
					}
					break;
				case IDReader.READ_CARD_SUCCESS:
//                    BeepManager.playsuccess(getActivity());

					showIDCardInfo(false, (User) msg.obj);

					break;

				case IDReader.READ_CARD_FAILED:
//                    BeepManager.playfail(getActivity());
					showIDCardInfo(false, null);
					break;
				default:
					break;
			}
		}
	};

	private void getRequest(String str){
		dialogLoading=new DialogLoading(SearchPeopleActivity.this, "数据加载中...", true);
		dialogLoading.show();
		RequestParams params=new RequestParams(Constants.URL+"quePeople.aspx");
		if (IsHouseID) {
			isZaizhu=true;
			rb_zaizhu.setChecked(true);
			params=new RequestParams(Constants.URL+"quePeople.aspx?type=get_houseinfor&user_id="+MyInfomationManager.getUserName(this)+"&house_code="+str);
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
//						Log.d("aaaaaaaaa", result);
//						renYuanXinXilist = XmlUtils.getBeanByParseXml(result, "Table", RenYuanXinXiBean.class, "", RenYuanXinXiBean.class);
//						processData(renYuanXinXilist);
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

	@Override
	public void onResume() {
		super.onResume();
		if (renYuanXinXilist!=null) {
			processData(renyuanInHouseBean);
		}
		if (renYuanXinXilist!=null) {
			processData(renyuanInHouseBean);
		}
	}
	private void processData(RenyuanInHouseBean renyuanInHouseBean){
		try {
			if (renyuanInHouseBean.data.house_exist.equals("0")){
				tv_content.setVisibility(View.VISIBLE);
				tv_content.setText("此房屋编号不存在！");
				return;
			}
			if (renyuanInHouseBean.data.people_exist.equals("0")){
				tv_content.setVisibility(View.VISIBLE);
				tv_content.setText("此房屋没有采集过人！");
				return;
			}
			tv_content.setVisibility(View.GONE);
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
//	private void processData(ResultBeanAndList<RenYuanXinXiBean> result){
//		List<RenYuanXinXiBean> list=new ArrayList<RenYuanXinXiBean>();
//		list.clear();
//		if(result==null)return;
//		if (isZaizhu) {
//			for (int i = 0; i <result.list.size(); i++) {
//				if (result.list.get(i).居住状态.equals("在住")) {
//					list.add(result.list.get(i));
//				}
//			}
//			rb_zaizhu.setText("在住("+list.size()+"人)");
//			rb_banli.setText("搬离("+(result.list.size()-list.size())+"人)");
//		}else{
//			for (int i = 0; i <result.list.size(); i++) {
//				if (result.list.get(i).居住状态.equals("搬离")) {
//					list.add(result.list.get(i));
//				}
//			}
//			rb_banli.setText("搬离("+list.size()+"人)");
//			rb_zaizhu.setText("在住("+(result.list.size()-list.size())+"人)");
//		}
//		if(rb_fangwu.isChecked()){
//			rg_zai.setVisibility(View.VISIBLE);
//		}
//		CommonAdapter<RenYuanXinXiBean> adapter =new CommonAdapter<RenYuanXinXiBean>(this,list,R.layout.item_renyuan) {
//			@Override
//			public void convert(ViewHolder helper, RenYuanXinXiBean item, int position) {
//				helper.setText(R.id.tv_name,"姓名: "+ item.姓名);
//				DbUtils dbUtils =DeviceUtils.getDbUtils(SearchPeopleActivity.this);
//				People people = null;
//				try {
//					people=dbUtils.findFirst(Selector.from(People.class).where("cardno", "=", item.身份证号.trim()));
//				} catch (DbException e) {
//					e.printStackTrace();
//				}
//				if (people!=null){
//					if (people.module.equals("变更")) {
//						helper.setText(R.id.tv_status, "【延期】");
//					}else if (people.module.equals("注销")) {
//						helper.setText(R.id.tv_status, "【注销】");
//					}else{
//
//						String[] split = item.末次采集时间.split("\\s+");
//						if (split.length>1) {
//							if (isBigerthanElevenandSmallthanTwelve(split[0])) {
//								helper.setText(R.id.tv_status, "【"+item.居住状态+"?】");
//							}else{
//								helper.setText(R.id.tv_status, "【"+item.居住状态+"】");
//							}
//						}else{
//							helper.setText(R.id.tv_status, "【"+item.居住状态+"】");
//						}
//					}
//				}else{
//					String[] split = item.末次采集时间.split("\\s+");
//					if (split.length>1) {
//						if (isBigerthanElevenandSmallthanTwelve(split[0])) {
//							helper.setText(R.id.tv_status, "【"+item.居住状态+"?】");
//						}else{
//							helper.setText(R.id.tv_status, "【"+item.居住状态+"】");
//						}
//					}else{
//						helper.setText(R.id.tv_status, "【"+item.居住状态+"】");
//					}
//				}
//				helper.setText(R.id.tv_shihao, "室号: "+item.室号);
//				helper.setText(R.id.tv_time, item.末次采集时间);
//			}
//		};
//		lv.setAdapter(adapter);
//	}
	@Event(value={R.id.btn_readcard})
	private void readcardClick(View view){
		String net = SharePrefUtil.getString(this,Constants.DEVICE_WAY,"自动");
		switch (net) {

			case "自动":
				int a = HasOTGDeviceConnected();
				if (a == 0) {
					showProcessDialog("正在读卡中，请稍后");
					idReader.connect(ConnectType.OTG);
				} else if (a == 1) {
					showProcessDialog("正在读卡中，请稍后");
					idReader.connect(ConnectType.OTGAccessory);
				} else {
					String mac=SharePrefUtil.getString(SearchPeopleActivity.this,"mac",null);
					if (mac == null) {
						deviceListDialogFragment.show(getSupportFragmentManager(), "");
					} else {
						showProcessDialog("正在读卡中，请稍后");
						int delayMillis = SharePrefUtil.getInt(SearchPeopleActivity.this, Constants.BluetoothSetting_long_time,15);
						idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
					}
				}
				break;
			case "蓝牙":
				String mac=SharePrefUtil.getString(SearchPeopleActivity.this,"mac",null);
				if (mac == null) {
					deviceListDialogFragment.show(getSupportFragmentManager(), "");
				} else {
					showProcessDialog("正在读卡中，请稍后");
					int delayMillis = SharePrefUtil.getInt(SearchPeopleActivity.this, Constants.BluetoothSetting_long_time,15);
					idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
				}
				break;
			case "OTG":
				int a2 = HasOTGDeviceConnected();
				if (a2 == 0) {
					showProcessDialog("正在读卡中，请稍后");
					idReader.connect(ConnectType.OTG);
				} else if (a2 == 1) {
					showProcessDialog("正在读卡中，请稍后");
					idReader.connect(ConnectType.OTGAccessory);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(SearchPeopleActivity.this, AlertDialog.THEME_HOLO_LIGHT);
					builder.setMessage("找不到OTG设备");
					builder.setPositiveButton("确定", null);
					builder.show();
				}
				break;

			case "NFC":
				AlertDialog.Builder builder = new AlertDialog.Builder(SearchPeopleActivity.this, AlertDialog.THEME_HOLO_LIGHT);
				builder.setMessage("当前是NFC模式，请将身份证贴在手机背面");
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
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
		}
		getRequest(cardno);
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
        if (falg12>=0&&falg11<0) {
			return true;
		}else{
			return false; 
		}
         
    } 
}
