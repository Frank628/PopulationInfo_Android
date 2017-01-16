package com.jinchao.population.mainmenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
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
import android.widget.Toast;
import android.widget.ListView;

import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.dbentity.HouseAddress2;
import com.jinchao.population.dbentity.HouseAddress3;
import com.jinchao.population.dbentity.HouseAddress4;
import com.jinchao.population.dbentity.HouseAddress5;
import com.jinchao.population.dbentity.HouseAddressOldBean2;
import com.jinchao.population.dbentity.HouseAddressOldBean3;
import com.jinchao.population.dbentity.HouseAddressOldBean4;
import com.jinchao.population.dbentity.HouseAddressOldBean5;
import com.jinchao.population.dbentity.HouseJLX;
import com.jinchao.population.dbentity.HouseJLX2;
import com.jinchao.population.dbentity.HouseJLX3;
import com.jinchao.population.dbentity.HouseJLX4;
import com.jinchao.population.dbentity.HouseJLX5;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.dbentity.JLX2;
import com.jinchao.population.dbentity.JLX3;
import com.jinchao.population.dbentity.JLX4;
import com.jinchao.population.dbentity.JLX5;
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
	private List<HouseAddress> listzen=new ArrayList<>();
	private List<HouseAddress2> listzen2=new ArrayList<>();
	private List<HouseAddress3> listzen3=new ArrayList<>();
	private List<HouseAddress4> listzen4=new ArrayList<>();
	private List<HouseAddress5> listzen5=new ArrayList<>();
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
		Is_RealPopulation=getIntent().getBooleanExtra(Constants.IS_FROM_REALPOPULATION,false);
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
				try {
					switch (database_tableNo){
						case 1:
							if (listzen.size()==0){
								tv_data.setVisibility(View.INVISIBLE);
							}else{
								tv_data.setVisibility(View.VISIBLE);
							}
							CommonAdapter<HouseAddress> adapter =new CommonAdapter<HouseAddress>(RegistRentalHouseActivity.this,listzen,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddress item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.id+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter);
						case 2:
							if (listzen2.size()==0){
								tv_data.setVisibility(View.INVISIBLE);
							}else{
								tv_data.setVisibility(View.VISIBLE);
							}
							CommonAdapter<HouseAddress2> adapter2 =new CommonAdapter<HouseAddress2>(RegistRentalHouseActivity.this,listzen2,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddress2 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.id+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter2);
							break;
						case 3:
							if (listzen3.size()==0){
								tv_data.setVisibility(View.INVISIBLE);
							}else{
								tv_data.setVisibility(View.VISIBLE);
							}
							CommonAdapter<HouseAddress3> adapter3 =new CommonAdapter<HouseAddress3>(RegistRentalHouseActivity.this,listzen3,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddress3 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.id+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter3);
							break;
						case 4:
							if (listzen4.size()==0){
								tv_data.setVisibility(View.INVISIBLE);
							}else{
								tv_data.setVisibility(View.VISIBLE);
							}
							CommonAdapter<HouseAddress4> adapter4 =new CommonAdapter<HouseAddress4>(RegistRentalHouseActivity.this,listzen4,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddress4 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.id+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter4);
							break;
						case 5:
							if (listzen4.size()==0){
								tv_data.setVisibility(View.INVISIBLE);
							}else{
								tv_data.setVisibility(View.VISIBLE);
							}
							CommonAdapter<HouseAddress5> adapter5 =new CommonAdapter<HouseAddress5>(RegistRentalHouseActivity.this,listzen5,R.layout.item_houseaddress) {
								@Override
								public void convert(ViewHolder helper, HouseAddress5 item, int position) {
									helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.id+"  地址："+item.address+"</font>"));
								}
							};
							lv.setAdapter(adapter5);
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
	private void getAllAddressRequest(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialogLoading.setName("正在加载分段地址数据...");
			}
		});
		RequestParams params;
		if (Is_RealPopulation){
			params=new RequestParams(Constants.URL+"syrkHouse.aspx");
			params.addBodyParameter("type", "getsyrkjlx");
		}else{
			params=new RequestParams(Constants.URL+"HouseAddress.aspx");
			params.addBodyParameter("type", "getjlx");
		}

		params.addBodyParameter("user_id",MyInfomationManager.getUserID(this));
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.d("aa", result);
				processAllAddress(result);
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
					dialogLoading.dismiss();
				 if (ex instanceof HttpException) { // 网络错误
	                    HttpException httpEx = (HttpException) ex;
	                    int responseCode = httpEx.getCode();
						Toast.makeText(RegistRentalHouseActivity.this, "responseCode="+responseCode, Toast.LENGTH_SHORT).show();
	                }else{
	                	getAllAddressRequest();
	                	Toast.makeText(RegistRentalHouseActivity.this, "请求超时，正在为您重新下载...",  Toast.LENGTH_SHORT).show();
	                	dialogLoading.show();
	                }
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished(){}
		});
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					bcount=0;
					ResultBeanAndList<HouseAddressOldBean> housexml = XmlUtils.getBeanByParseXml(result,"Table",HouseAddressOldBean.class, "", HouseAddressOldBean.class);
					Log.d("hosue2", housexml.toString());
					dbUtils.dropTable(DatabaseUtil.getTable_HouseAddressOldBean(database_tableNo,RegistRentalHouseActivity.this));
					if (housexml.list!=null) {
						for (int i = 0; i < housexml.list.size(); i++) {
							try {
								switch (database_tableNo){
									case 1:
										HouseAddressOldBean houseAddressOldBean =new HouseAddressOldBean(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										dbUtils.save(houseAddressOldBean);
										break;
									case 2:
										HouseAddressOldBean2 houseAddressOldBean2 =new HouseAddressOldBean2(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										dbUtils.save(houseAddressOldBean2);
										break;
									case 3:
										HouseAddressOldBean3 houseAddressOldBean3 =new HouseAddressOldBean3(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										dbUtils.save(houseAddressOldBean3);
										break;
									case 4:
										HouseAddressOldBean4 houseAddressOldBean4 =new HouseAddressOldBean4(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										dbUtils.save(houseAddressOldBean4);
										break;
									case 5:
										HouseAddressOldBean5 houseAddressOldBean5 =new HouseAddressOldBean5(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
										dbUtils.save(houseAddressOldBean5);
										break;
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
							bcount++;
							RegistRentalHouseActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									dialogLoading.setName("已导入"+bcount+"条合成地址，"+count+"条分段地址");
								}
							});
						}
					}
					getAllAddressRequest();
				} catch (Exception e) {
					getAllAddressRequest();
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void processAllAddress(final String result){
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
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				count=0;
					try {
						HouseAddressBean houseAddressBean =GsonTools.changeGsonToBean(result, HouseAddressBean.class);
						dbUtils.dropTable(DatabaseUtil.getTable_JLX(database_tableNo,RegistRentalHouseActivity.this));
						dbUtils.dropTable(DatabaseUtil.getTable_HouseJLX(database_tableNo,RegistRentalHouseActivity.this));
						dbUtils.dropTable(DatabaseUtil.getTable_HouseAddress(database_tableNo,RegistRentalHouseActivity.this));
						for (int i = 0; i < houseAddressBean.data.size(); i++) {
							if (!houseAddressBean.data.get(i).jlx.equals("")) {
								switch (database_tableNo){
									case 1:
										dbUtils.save(new JLX(i, houseAddressBean.data.get(i).jlx));
										break;
									case 2:
										dbUtils.save(new JLX2(i, houseAddressBean.data.get(i).jlx));
										break;
									case 3:
										dbUtils.save(new JLX3(i, houseAddressBean.data.get(i).jlx));
										break;
									case 4:
										dbUtils.save(new JLX4(i, houseAddressBean.data.get(i).jlx));
										break;
									case 5:
										dbUtils.save(new JLX5(i, houseAddressBean.data.get(i).jlx));
										break;
								}

							}
							for (int j = 0; j < houseAddressBean.data.get(i).hourseData.size(); j++) {
								count++;
								RegistRentalHouseActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										dialogLoading.setName("已导入"+bcount+"条合成地址，"+count+"条分段地址");
									}
								});
								try {
									switch (database_tableNo){
										case 1:
											listzen.add(new HouseAddress(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
										case 2:
											listzen2.add(new HouseAddress2(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress2(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX2(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
										case 3:
											listzen3.add(new HouseAddress3(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress3(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX3(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
										case 4:
											listzen4.add(new HouseAddress4(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress4(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX4(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
										case 5:
											listzen5.add(new HouseAddress5(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress5(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX5(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
						String date =sDateFormat.format(new Date(System.currentTimeMillis())); 
						SharePrefUtil.saveString(RegistRentalHouseActivity.this, Constants.LOCAL_DB_VERSION, date);
						UserPKDataBase userPKDataBase=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name","=",MyInfomationManager.getSQNAME(RegistRentalHouseActivity.this)));
						if (userPKDataBase!=null){
							userPKDataBase.setIs_used("0");
							userPKDataBase.setDatabase_name(database_tableNo);
							userPKDataBase.setUpdate_time(date);
							dbUtils.update(userPKDataBase,"database_name","is_used","update_time");
						}else{
							dbUtils.save(new UserPKDataBase(MyInfomationManager.getSQNAME(RegistRentalHouseActivity.this), database_tableNo, date, "0",date));
						}
						isQuanKuOk=true;
						Message msg=new Message();
						msg.what=1;
						handler.sendMessage(msg);
					} catch (Exception e) {
						Dialog.showSelectDialog(RegistRentalHouseActivity.this, "下载失败，请重试！", new DialogClickListener() {
							@Override
							public void confirm() {}
							
							@Override
							public void cancel() {}
						});
						dialogLoading.dismiss();
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					bcount=0;
					ResultBeanAndList<HouseAddressOldBean> housexml = XmlUtils.getBeanByParseXml(result,"Table",HouseAddressOldBean.class, "", HouseAddressOldBean.class);
					Log.d("hosue2", housexml.toString());
					if (housexml.list!=null) {
						for (int i = 0; i < housexml.list.size(); i++) {
							switch (database_tableNo){
								case 1:
									HouseAddressOldBean houseAddressOldBean =new HouseAddressOldBean(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
									dbUtils.save(houseAddressOldBean);
									break;
								case 2:
									HouseAddressOldBean2 houseAddressOldBean2 =new HouseAddressOldBean2(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
									dbUtils.save(houseAddressOldBean2);
									break;
								case 3:
									HouseAddressOldBean3 houseAddressOldBean3 =new HouseAddressOldBean3(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
									dbUtils.save(houseAddressOldBean3);
									break;
								case 4:
									HouseAddressOldBean4 houseAddressOldBean4 =new HouseAddressOldBean4(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
									dbUtils.save(houseAddressOldBean4);
									break;
								case 5:
									HouseAddressOldBean5 houseAddressOldBean5 =new HouseAddressOldBean5(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
									dbUtils.save(houseAddressOldBean5);
									break;
							}
							bcount++;
							RegistRentalHouseActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									dialogLoading.setName("已导入"+bcount+"条合成地址，"+count+"条分段地址");
								}
							});
						}
					}
					getZenAddressRequest();
				} catch (Exception e) {
					getZenAddressRequest();
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void getZenAddressRequest(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialogLoading.setName("正在加载分段地址...");
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
			params.addBodyParameter("type", "newsyrkjlx");
		}else{
			params=new RequestParams(Constants.URL+"HouseAddress.aspx");
			params.addBodyParameter("type", "newjlx");
		}

		params.addBodyParameter("user_id",MyInfomationManager.getUserID(this));
		params.addBodyParameter("last_udt",lastudttime);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.d("aa", result);
				processZenAddress(result);
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
					dialogLoading.dismiss();
				 if (ex instanceof HttpException) { // 网络错误
	                    HttpException httpEx = (HttpException) ex;
	                    int responseCode = httpEx.getCode();
						Toast.makeText(RegistRentalHouseActivity.this, "responseCode="+responseCode, Toast.LENGTH_SHORT).show();
	                }else{
	                	getZenAddressRequest();
	                	Toast.makeText(RegistRentalHouseActivity.this, "请求超时，正在为您重新下载...", Toast.LENGTH_SHORT).show();
	                	dialogLoading.show();
	                }
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished(){}
		});
	}
	private void processZenAddress(final String result){
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
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				count=0;
					try {
						HouseAddressBean houseAddressBean =GsonTools.changeGsonToBean(result, HouseAddressBean.class);
//						dbUtils.dropTable(JLX.class);
//						dbUtils.dropTable(HouseJLX.class);
//						dbUtils.dropTable(HouseAddress.class);
						for (int i = 0; i < houseAddressBean.data.size(); i++) {
							if (!houseAddressBean.data.get(i).jlx.equals("")) {
								JLX jlx=dbUtils.findFirst(Selector.from(JLX.class).where("jlx_name", "=", houseAddressBean.data.get(i).jlx));
								if (jlx==null) {
									try {
										switch (database_tableNo){
											case 1:
												dbUtils.save(new JLX(i, houseAddressBean.data.get(i).jlx));
												break;
											case 2:
												dbUtils.save(new JLX2(i, houseAddressBean.data.get(i).jlx));
												break;
											case 3:
												dbUtils.save(new JLX3(i, houseAddressBean.data.get(i).jlx));
												break;
											case 4:
												dbUtils.save(new JLX4(i, houseAddressBean.data.get(i).jlx));
												break;
											case 5:
												dbUtils.save(new JLX5(i, houseAddressBean.data.get(i).jlx));
												break;
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							for (int j = 0; j < houseAddressBean.data.get(i).hourseData.size(); j++) {
								count++;
								RegistRentalHouseActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										dialogLoading.setName("已导入"+bcount+"条合成地址，"+count+"条分段地址");
									}
								});
								try {
									switch (database_tableNo){
										case 1:
											listzen.add(new HouseAddress(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
										case 2:
											listzen2.add(new HouseAddress2(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress2(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX2(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
										case 3:
											listzen3.add(new HouseAddress3(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress3(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX3(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
										case 4:
											listzen4.add(new HouseAddress4(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress4(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX4(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
										case 5:
											listzen5.add(new HouseAddress5(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseAddress5(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
											dbUtils.save(new HouseJLX5(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
											break;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

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
						Message msg=new Message();
						msg.what=1;
						handler.sendMessage(msg);
					} catch (Exception e) {
						dialogLoading.dismiss();
						e.printStackTrace();
					}
			}
		}).start();
	}

//	@Override
//	public void onBackPressed() {
//		if (!isQuanKuOk){
//			AlertDialog.Builder builder = new AlertDialog.Builder(RegistRentalHouseActivity.this);
//			builder.setMessage("确认退出吗?");
//			builder.setTitle("提示");
//			builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					RegistRentalHouseActivity.this.finish();
//				}
//			});
//			builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			builder.create().show();
//		}else{
//			super.onBackPressed();
//      }
//	}

}
