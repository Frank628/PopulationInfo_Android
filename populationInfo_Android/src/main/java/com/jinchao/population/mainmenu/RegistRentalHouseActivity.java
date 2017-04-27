package com.jinchao.population.mainmenu;

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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;

import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.dbentity.HouseAddress10;
import com.jinchao.population.dbentity.HouseAddress2;
import com.jinchao.population.dbentity.HouseAddress3;
import com.jinchao.population.dbentity.HouseAddress4;
import com.jinchao.population.dbentity.HouseAddress5;
import com.jinchao.population.dbentity.HouseAddress6;
import com.jinchao.population.dbentity.HouseAddress7;
import com.jinchao.population.dbentity.HouseAddress8;
import com.jinchao.population.dbentity.HouseAddress9;
import com.jinchao.population.dbentity.HouseAddressOldBean10;
import com.jinchao.population.dbentity.HouseAddressOldBean2;
import com.jinchao.population.dbentity.HouseAddressOldBean3;
import com.jinchao.population.dbentity.HouseAddressOldBean4;
import com.jinchao.population.dbentity.HouseAddressOldBean5;
import com.jinchao.population.dbentity.HouseAddressOldBean6;
import com.jinchao.population.dbentity.HouseAddressOldBean7;
import com.jinchao.population.dbentity.HouseAddressOldBean8;
import com.jinchao.population.dbentity.HouseAddressOldBean9;
import com.jinchao.population.dbentity.HouseJLX;
import com.jinchao.population.dbentity.HouseJLX10;
import com.jinchao.population.dbentity.HouseJLX2;
import com.jinchao.population.dbentity.HouseJLX3;
import com.jinchao.population.dbentity.HouseJLX4;
import com.jinchao.population.dbentity.HouseJLX5;
import com.jinchao.population.dbentity.HouseJLX6;
import com.jinchao.population.dbentity.HouseJLX7;
import com.jinchao.population.dbentity.HouseJLX8;
import com.jinchao.population.dbentity.HouseJLX9;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.dbentity.JLX10;
import com.jinchao.population.dbentity.JLX2;
import com.jinchao.population.dbentity.JLX3;
import com.jinchao.population.dbentity.JLX4;
import com.jinchao.population.dbentity.JLX5;
import com.jinchao.population.dbentity.JLX6;
import com.jinchao.population.dbentity.JLX7;
import com.jinchao.population.dbentity.JLX8;
import com.jinchao.population.dbentity.JLX9;
import com.jinchao.population.dbentity.UserPKDataBase;
import com.jinchao.population.entity.HouseAddressBean;
import com.jinchao.population.dbentity.HouseAddressOldBean;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.NavigationLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

@ContentView(R.layout.activity_registrentalhouse)
public class RegistRentalHouseActivity extends BaseActiviy{

	@ViewInject(R.id.ll_top)private LinearLayout ll_top;
	@ViewInject(R.id.lv)private ListView lv;
	@ViewInject(R.id.tv_data)private TextView tv_data;
	private int count=0,bcount=0;
	private DialogLoading dialogLoading;
	private boolean isQuanKuOk=false,Is_RealPopulation=false;
	private List<HouseAddressOldBean> listzen=new ArrayList<>();
	private List<HouseAddressOldBean2> listzen2=new ArrayList<>();
	private List<HouseAddressOldBean3> listzen3=new ArrayList<>();
	private List<HouseAddressOldBean4> listzen4=new ArrayList<>();
	private List<HouseAddressOldBean5> listzen5=new ArrayList<>();
	private List<HouseAddressOldBean6> listzen6=new ArrayList<>();
	private List<HouseAddressOldBean7> listzen7=new ArrayList<>();
	private List<HouseAddressOldBean8> listzen8=new ArrayList<>();
	private List<HouseAddressOldBean9> listzen9=new ArrayList<>();
	private List<HouseAddressOldBean10> listzen10=new ArrayList<>();
	public int database_tableNo=0;//当前登录账号使用的哪个数据库，0:未下载的地址库，1：表1,2：表2.。。。
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("出租屋登记");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		dbUtils = DeviceUtils.getDbUtils(RegistRentalHouseActivity.this);
		dialogLoading = new DialogLoading(this, "地址下载中...",true);
//		Is_RealPopulation=getIntent().getBooleanExtra(Constants.IS_FROM_REALPOPULATION,false);
		if (((MyApplication)getApplication()).database_tableNo==0){
			database_tableNo= DatabaseUtil.getNullDB(this);
		}else{
			database_tableNo=((MyApplication)getApplication()).database_tableNo;
		}
	}
	private Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Log.e("house",database_tableNo+";"+listzen.size());
				try {
					switch (database_tableNo){
						case 1:
							if (listzen.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean> adapter =new CommonAdapter<HouseAddressOldBean>(RegistRentalHouseActivity.this,listzen,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter);
						case 2:
							if (listzen2.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean2> adapter2 =new CommonAdapter<HouseAddressOldBean2>(RegistRentalHouseActivity.this,listzen2,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean2 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter2);
							break;
						case 3:
							if (listzen3.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean3> adapter3 =new CommonAdapter<HouseAddressOldBean3>(RegistRentalHouseActivity.this,listzen3,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean3 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter3);
							break;
						case 4:
							if (listzen4.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean4> adapter4 =new CommonAdapter<HouseAddressOldBean4>(RegistRentalHouseActivity.this,listzen4,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean4 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter4);
							break;
						case 5:
							if (listzen5.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean5> adapter5 =new CommonAdapter<HouseAddressOldBean5>(RegistRentalHouseActivity.this,listzen5,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean5 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter5);
							break;
						case 6:
							if (listzen6.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean6> adapter6 =new CommonAdapter<HouseAddressOldBean6>(RegistRentalHouseActivity.this,listzen6,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean6 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter6);
							break;
						case 7:
							if (listzen7.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean7> adapter7 =new CommonAdapter<HouseAddressOldBean7>(RegistRentalHouseActivity.this,listzen7,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean7 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter7);
							break;
						case 8:
							if (listzen8.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean8> adapter8 =new CommonAdapter<HouseAddressOldBean8>(RegistRentalHouseActivity.this,listzen8,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean8 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter8);
							break;
						case 9:
							if (listzen9.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean9> adapter9 =new CommonAdapter<HouseAddressOldBean9>(RegistRentalHouseActivity.this,listzen9,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean9 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter9);
							break;
						case 10:
							if (listzen10.size()==0){
								tv_data.setVisibility(View.VISIBLE);
							}else{
								tv_data.setVisibility(View.GONE);
							}
							CommonAdapter<HouseAddressOldBean10> adapter10 =new CommonAdapter<HouseAddressOldBean10>(RegistRentalHouseActivity.this,listzen10,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddressOldBean10 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.scode+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter10);
							break;
					}
					Dialog.showRadioDialog(RegistRentalHouseActivity.this, "地址下载成功!", new DialogClickListener() {
						@Override
						public void confirm() {

						}
						@Override
						public void cancel() {
							((MyApplication)getApplication()).setIsSureDengji(false);
						}
					});
					dialogLoading.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		};
	};
	private DbUtils dbUtils;
	@Event(value={R.id.ib_tian})
	private void tianClick(View view){
		Intent intent=new Intent(this, AddRentalHouseActivity.class);
		intent.putExtra(Constants.IS_FROM_REALPOPULATION,getIntent().getBooleanExtra(Constants.IS_FROM_REALPOPULATION,false));
		startActivity(intent);
	}
	@Event(value={R.id.ib_quan})
	private void quanKuClick(View view){
		Dialog.showSelectDialog(RegistRentalHouseActivity.this, "确定下载全库地址？", new DialogClickListener() {
			@Override
			public void confirm() {
				lv.setAdapter(null);
				tv_data.setVisibility(View.GONE);
				getALL2AddressRequest();
			}
			@Override
			public void cancel() {

			}
		});

	}
	@Event(value={R.id.ib_zen})
	private void zenKuClick(View view){
		tv_data.setVisibility(View.GONE);
		lv.setAdapter(null);//如果当前房屋数据库班版本时间是空的，需重新下载全库
		getZen2AddressRequest();
	}


	private void getALL2AddressRequest(){
		isQuanKuOk=false;
		dialogLoading.show();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialogLoading.setName("正在加载合成地址库...");
			}
		});
		RequestParams params;
		if (Is_RealPopulation){
			params=new RequestParams(Constants.URL+"syrkHouse.aspx");
		}else{
			params=new RequestParams(Constants.URL+"webHouseServer.aspx");
		}
		params.addBodyParameter("type", "get");
		params.addBodyParameter("user_id",MyInfomationManager.getUserID(this));
		params.addBodyParameter("last_udt","2011-01-01 12:00:00");
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
					processAllAddress2(result);
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
					dialogLoading.dismiss();
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished(){}
		});
	}
	private void processAllAddress2(final String result){
		switch (database_tableNo){
			case 1:
				listzen.clear();
				break;
			case 2:
				listzen2.clear();
				break;
			case 3:
				listzen3.clear();
				break;
			case 4:
				listzen4.clear();
				break;
			case 5:
				listzen5.clear();
				break;
			case 6:
				listzen6.clear();
				break;
			case 7:
				listzen7.clear();
				break;
			case 8:
				listzen8.clear();
				break;
			case 9:
				listzen9.clear();
				break;
			case 10:
				listzen10.clear();
				break;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					bcount=0;
					ResultBeanAndList<HouseAddressOldBean> housexml = XmlUtils.getBeanByParseXml(result,"Table",HouseAddressOldBean.class, "", HouseAddressOldBean.class);
					dbUtils.dropTable(DatabaseUtil.getTable_HouseAddressOldBean(database_tableNo,RegistRentalHouseActivity.this));
					if (housexml.list!=null) {
						for (int i = 0; i < housexml.list.size(); i++) {
							try {
								switch (database_tableNo) {
									case 1:
										HouseAddressOldBean houseAddressOldBean = new HouseAddressOldBean(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen.add(houseAddressOldBean);
										dbUtils.save(houseAddressOldBean);
										break;
									case 2:
										HouseAddressOldBean2 houseAddressOldBean2 = new HouseAddressOldBean2(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen2.add(houseAddressOldBean2);
										dbUtils.save(houseAddressOldBean2);
										break;
									case 3:
										HouseAddressOldBean3 houseAddressOldBean3 = new HouseAddressOldBean3(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen3.add(houseAddressOldBean3);
										dbUtils.save(houseAddressOldBean3);
										break;
									case 4:
										HouseAddressOldBean4 houseAddressOldBean4 = new HouseAddressOldBean4(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen4.add(houseAddressOldBean4);
										dbUtils.save(houseAddressOldBean4);
										break;
									case 5:
										HouseAddressOldBean5 houseAddressOldBean5 = new HouseAddressOldBean5(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen5.add(houseAddressOldBean5);
										dbUtils.save(houseAddressOldBean5);
										break;
									case 6:
										HouseAddressOldBean6 houseAddressOldBean6 = new HouseAddressOldBean6(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen6.add(houseAddressOldBean6);
										dbUtils.save(houseAddressOldBean6);
										break;
									case 7:
										HouseAddressOldBean7 houseAddressOldBean7 = new HouseAddressOldBean7(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen7.add(houseAddressOldBean7);
										dbUtils.save(houseAddressOldBean7);
										break;
									case 8:
										HouseAddressOldBean8 houseAddressOldBean8 = new HouseAddressOldBean8(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen8.add(houseAddressOldBean8);
										dbUtils.save(houseAddressOldBean8);
										break;
									case 9:
										HouseAddressOldBean9 houseAddressOldBean9 = new HouseAddressOldBean9(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen9.add(houseAddressOldBean9);
										dbUtils.save(houseAddressOldBean9);
										break;
									case 10:
										HouseAddressOldBean10 houseAddressOldBean10 = new HouseAddressOldBean10(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										listzen10.add(houseAddressOldBean10);
										dbUtils.save(houseAddressOldBean10);
										break;
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
							bcount++;
							RegistRentalHouseActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									dialogLoading.setName("已导入" + bcount + "条合成地址");
								}
							});
						}

						SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String date = sDateFormat.format(new Date(System.currentTimeMillis()));
						UserPKDataBase userPKDataBase = dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name", "=", MyInfomationManager.getSQNAME(RegistRentalHouseActivity.this)));
						if (userPKDataBase != null) {
							userPKDataBase.setIs_used("0");
							userPKDataBase.setDatabase_name(database_tableNo);
							userPKDataBase.setUpdate_time(date);
							dbUtils.update(userPKDataBase, "database_name", "is_used", "update_time");
						} else {
							dbUtils.save(new UserPKDataBase(MyInfomationManager.getSQNAME(RegistRentalHouseActivity.this), database_tableNo, date, "0", date));
						}
						((MyApplication) getApplication()).setDataBaseTableNo(database_tableNo);
						isQuanKuOk = true;
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
					}
//					getAllAddressRequest();
				} catch (Exception e) {
					dialogLoading.dismiss();
//					getAllAddressRequest();
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void getZen2AddressRequest(){
		dialogLoading.show();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialogLoading.setName("正在加载合成地址...");
			}
		});
		String lastudttime="2011-01-01 12:00:00";
		try {
			UserPKDataBase userPKDataBase=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name","=",MyInfomationManager.getSQNAME(RegistRentalHouseActivity.this)));
			if (userPKDataBase!=null){
				lastudttime=userPKDataBase.getUpdate_time();
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		RequestParams params;
		if (Is_RealPopulation){
			params=new RequestParams(Constants.URL+"syrkHouse.aspx");
		}else{
			params=new RequestParams(Constants.URL+"webHouseServer.aspx");
		}
		params.addBodyParameter("type", "get");
		params.addBodyParameter("user_id",MyInfomationManager.getUserID(this));
		params.addBodyParameter("last_udt",lastudttime);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.i("zenliang",result);
				processZenAddress2(result);
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
					dialogLoading.dismiss();
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished(){}
		});
	}
	private void processZenAddress2(final String result){
		switch (database_tableNo){
			case 1:
				listzen.clear();
				break;
			case 2:
				listzen2.clear();
				break;
			case 3:
				listzen3.clear();
				break;
			case 4:
				listzen4.clear();
				break;
			case 5:
				listzen5.clear();
				break;
			case 6:
				listzen6.clear();
				break;
			case 7:
				listzen7.clear();
				break;
			case 8:
				listzen8.clear();
				break;
			case 9:
				listzen9.clear();
				break;
			case 10:
				listzen10.clear();
				break;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					bcount=0;
					ResultBeanAndList<HouseAddressOldBean> housexml = XmlUtils.getBeanByParseXml(result,"Table",HouseAddressOldBean.class, "", HouseAddressOldBean.class);
					if (housexml!=null) {
						if (housexml.list != null) {
							for (int i = 0; i < housexml.list.size(); i++) {
								try {
									switch (database_tableNo) {
										case 1:
											HouseAddressOldBean houseAddressOldBean = new HouseAddressOldBean(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean);
											listzen.add(houseAddressOldBean);
											break;
										case 2:
											HouseAddressOldBean2 houseAddressOldBean2 = new HouseAddressOldBean2(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean2);
											listzen2.add(houseAddressOldBean2);
											break;
										case 3:
											HouseAddressOldBean3 houseAddressOldBean3 = new HouseAddressOldBean3(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean3);
											listzen3.add(houseAddressOldBean3);
											break;
										case 4:
											HouseAddressOldBean4 houseAddressOldBean4 = new HouseAddressOldBean4(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean4);
											listzen4.add(houseAddressOldBean4);
											break;
										case 5:
											HouseAddressOldBean5 houseAddressOldBean5 = new HouseAddressOldBean5(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean5);
											listzen5.add(houseAddressOldBean5);
											break;
										case 6:
											HouseAddressOldBean6 houseAddressOldBean6 = new HouseAddressOldBean6(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean6);
											listzen6.add(houseAddressOldBean6);
											break;
										case 7:
											HouseAddressOldBean7 houseAddressOldBean7 = new HouseAddressOldBean7(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean7);
											listzen7.add(houseAddressOldBean7);
											break;
										case 8:
											HouseAddressOldBean8 houseAddressOldBean8 = new HouseAddressOldBean8(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean8);
											listzen8.add(houseAddressOldBean8);
											break;
										case 9:
											HouseAddressOldBean9 houseAddressOldBean9 = new HouseAddressOldBean9(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean9);
											listzen9.add(houseAddressOldBean9);
											break;
										case 10:
											HouseAddressOldBean10 houseAddressOldBean10 = new HouseAddressOldBean10(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
											dbUtils.saveOrUpdate(houseAddressOldBean10);
											listzen10.add(houseAddressOldBean10);
											break;
									}
									bcount++;
									RegistRentalHouseActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
	//									dialogLoading.setName("已导入"+bcount+"条合成地址，"+count+"条分段地址");
											dialogLoading.setName("已导入" + bcount + "条合成地址");
										}
									});
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
							if(housexml.list.size()!=0){
								SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String date =sDateFormat.format(new Date(System.currentTimeMillis()));
								UserPKDataBase userPKDataBase=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name","=",MyInfomationManager.getSQNAME(RegistRentalHouseActivity.this)));
								if (userPKDataBase!=null){
									userPKDataBase.setIs_used("0");
									userPKDataBase.setUpdate_time(date);
									dbUtils.update(userPKDataBase,"database_name","is_used","update_time");
								}else{
									dbUtils.save(new UserPKDataBase(MyInfomationManager.getSQNAME(RegistRentalHouseActivity.this), database_tableNo, date, "0",date));
								}
							}
							Message msg=new Message();
							msg.what=1;
							handler.sendMessage(msg);
						}
					}

				} catch (Exception e) {
					dialogLoading.dismiss();
					e.printStackTrace();
				}
			}
		}).start();
	}

}
