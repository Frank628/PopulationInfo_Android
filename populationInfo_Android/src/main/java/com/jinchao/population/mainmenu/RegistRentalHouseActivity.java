package com.jinchao.population.mainmenu;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.dbentity.HouseJLX;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.entity.HouseAddressBean;
import com.jinchao.population.entity.HouseAddressOldBean;
import com.jinchao.population.entity.YanZhengBean;
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
	private int count=0,bcount=0;
	private DialogLoading dialogLoading;
	private boolean isQuanKuOk=false;
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
	}
	private Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					CommonAdapter<HouseAddress> adapter =new CommonAdapter<HouseAddress>(RegistRentalHouseActivity.this,dbUtils.findAll(HouseAddress.class),R.layout.item_houseaddress) {
						@Override
						public void convert(ViewHolder helper, HouseAddress item, int position) {
							helper.setText(R.id.tv_content,Html.fromHtml("<font  color=\'#cfcfcf\'>"+(position+1)+". </font><font color=\'#666666\'>"+"编号："+item.id+"  地址："+item.address+"</font>"));
						}
					};
					lv.setAdapter(adapter);
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
		startActivity(intent);
	}
	@Event(value={R.id.ib_quan})
	private void quanKuClick(View view){
		lv.setAdapter(null);
		getALL2AddressRequest();
	}
	@Event(value={R.id.ib_zen})
	private void zenKuClick(View view){
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
		RequestParams params=new RequestParams(Constants.URL+"HouseAddress.aspx");
		params.addBodyParameter("type", "getjlx");
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
						Toast.makeText(RegistRentalHouseActivity.this, "responseCode="+responseCode, 0).show();
	                }else{
	                	getAllAddressRequest();
	                	Toast.makeText(RegistRentalHouseActivity.this, "请求超时，正在为您重新下载...", 0).show();
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
		RequestParams params=new RequestParams(Constants.URL+"webHouseServer.aspx");
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
					dbUtils.dropTable(HouseAddressOldBean.class);
					if (housexml.list!=null) {
						for (int i = 0; i < housexml.list.size(); i++) {
							HouseAddressOldBean houseAddressOldBean =new HouseAddressOldBean(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
							try {
								dbUtils.save(houseAddressOldBean);
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				count=0;
					try {
						HouseAddressBean houseAddressBean =GsonTools.changeGsonToBean(result, HouseAddressBean.class);
						dbUtils.dropTable(JLX.class);
						dbUtils.dropTable(HouseJLX.class);
						dbUtils.dropTable(HouseAddress.class);
						for (int i = 0; i < houseAddressBean.data.size(); i++) {
							if (!houseAddressBean.data.get(i).jlx.equals("")) {
								dbUtils.save(new JLX(i, houseAddressBean.data.get(i).jlx));
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
									dbUtils.save(new HouseAddress(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
									dbUtils.save(new HouseJLX(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
						String date =sDateFormat.format(new Date(System.currentTimeMillis())); 
						SharePrefUtil.saveString(RegistRentalHouseActivity.this, Constants.LOCAL_DB_VERSION, date);
						isQuanKuOk=true;
						Message msg=new Message();
						msg.what=1;
						handler.sendMessage(msg);
						
					} catch (Exception e) {
						Dialog.showSelectDialog(RegistRentalHouseActivity.this, "下载失败，请重试！", new DialogClickListener() {
							@Override
							public void confirm() {
							}
							
							@Override
							public void cancel() {
								
							}
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
				dialogLoading.setName("正在合成地址...");
			}
		});
		RequestParams params=new RequestParams(Constants.URL+"webHouseServer.aspx");
		params.addBodyParameter("type", "get");
		params.addBodyParameter("user_id",MyInfomationManager.getUserID(this));
		Toast.makeText(RegistRentalHouseActivity.this, SharePrefUtil.getString(RegistRentalHouseActivity.this, Constants.LOCAL_DB_VERSION, "2011-01-01 12:00:00"), 0).show();
		params.addBodyParameter("last_udt",SharePrefUtil.getString(RegistRentalHouseActivity.this, Constants.LOCAL_DB_VERSION, "2011-01-01 12:00:00"));
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
							HouseAddressOldBean houseAddressOldBean =new HouseAddressOldBean(housexml.list.get(i).id, housexml.list.get(i).shequ_id, housexml.list.get(i).user_id, housexml.list.get(i).scode, housexml.list.get(i).address, housexml.list.get(i).hrs_pname, housexml.list.get(i).telphone, housexml.list.get(i).idcard, housexml.list.get(i).source_id, housexml.list.get(i).udt);
							dbUtils.save(houseAddressOldBean);
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
		Log.d("last_udt", SharePrefUtil.getString(RegistRentalHouseActivity.this, Constants.LOCAL_DB_VERSION, "2011-01-01 12:00:00"));
		RequestParams params=new RequestParams(Constants.URL+"HouseAddress.aspx");
		params.addBodyParameter("type", "newjlx");
		params.addBodyParameter("user_id",MyInfomationManager.getUserID(this));
		params.addBodyParameter("last_udt",SharePrefUtil.getString(RegistRentalHouseActivity.this, Constants.LOCAL_DB_VERSION, "2011-01-01 12:00:00"));
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
						Toast.makeText(RegistRentalHouseActivity.this, "responseCode="+responseCode, 0).show();
	                }else{
	                	getZenAddressRequest();
	                	Toast.makeText(RegistRentalHouseActivity.this, "请求超时，正在为您重新下载...", 0).show();
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
										dbUtils.save(new JLX(i, houseAddressBean.data.get(i).jlx));
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
									dbUtils.save(new HouseAddress(houseAddressBean.data.get(i).hourseData.get(j).scode,houseAddressBean.data.get(i).jlx+houseAddressBean.data.get(i).hourseData.get(j).hrsAdress));
									dbUtils.save(new HouseJLX(houseAddressBean.data.get(i).hourseData.get(j).scode, houseAddressBean.data.get(i).hourseData.get(j).hrsAdress, i));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
						String date =sDateFormat.format(new Date(System.currentTimeMillis())); 
						SharePrefUtil.saveString(RegistRentalHouseActivity.this, Constants.LOCAL_DB_VERSION, date);
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
//				});
//			builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			builder.create().show();
//		}else{
//			super.onBackPressed();
//        }
//	}

}
