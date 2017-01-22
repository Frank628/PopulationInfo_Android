package com.jinchao.population.realpopulation;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.DeleteRealPeopleBean;
import com.jinchao.population.entity.SortbyRoomCodeClass;
import com.jinchao.population.entity.SortbyTimeClass;
import com.jinchao.population.entity.RealHouseBean.RealHouseOne;
import com.jinchao.population.entity.RealPeopleinHouseBean;
import com.jinchao.population.entity.RealPeopleinHouseBean.RealPeopleinHouseOne;
import com.jinchao.population.entity.SortbyzaizhuClass;
import com.jinchao.population.mainmenu.HandleIDActivity;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.mainmenu.SearchPeopleDetailActivity;
import com.jinchao.population.mainmenu.UpLoadActivity;
import com.jinchao.population.mainmenu.ZanZhuActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.RealhouseUser;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.RealhouseUser.OnEnsureClickListener;
import com.jinchao.population.widget.ActionSheetDialog;
import com.jinchao.population.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.jinchao.population.widget.ActionSheetDialog.SheetItemColor;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import de.greenrobot.event.EventBus;
@ContentView(R.layout.activity_singlerealpopulation)
public class SingleRealPopulationActivity extends BaseActiviy{
	@ViewInject(R.id.textview)private TextView textview;
	@ViewInject(R.id.tv_title)private TextView tv_title;
	@ViewInject(R.id.tv_zhuzhi)private TextView tv_zhuzhi;
	@ViewInject(R.id.tv_cardno)private TextView tv_cardno;
	@ViewInject(R.id.tv_zhuzhai)private TextView tv_zhuzhai;
	@ViewInject(R.id.btn_xinzeng)private Button btn_xinzeng;
	@ViewInject(R.id.btn_xinzengertong)private Button btn_xinzengertong;
	@ViewInject(R.id.btn_jiaru)private Button btn_jiaru;
	@ViewInject(R.id.btn_banzheng)private Button btn_banzheng;
	@ViewInject(R.id.image)private ImageView image;
	@ViewInject(R.id.lv)private ListView lv;
	@ViewInject(R.id.tv_shou)private CheckBox tv_shou;
	@ViewInject(R.id.cardview)private CardView cardview;
	@ViewInject(R.id.rg)private RadioGroup rg_sort;
	private RealHouseOne realHouseOne;
	private PopupWindow popdelete;
	private RadioGroup rg;
	private RealPeopleinHouseBean realPeopleinHouseBean;
	private People people;
	private DbUtils dbUtils;
	private RelativeLayout layout;
	private DialogLoading dialogLoadingd;
	private CommonAdapter<RealPeopleinHouseOne> adapter;
	private List<RealPeopleinHouseOne> listreal=new ArrayList<RealPeopleinHouseBean.RealPeopleinHouseOne>();
	private DialogLoading dialogLoading;
	private String zhuzhaitype="",renshus="";
	private List<RealPeopleinHouseOne> list;
	private boolean isfirstResume=true;
	private boolean canOperate=false;
	private boolean isfirstadd=false;
	private boolean canJianRu=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("实有人口采集");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		EventBus.getDefault().register(this);
        people = (People) getIntent().getSerializableExtra("people");
		realHouseOne=(RealHouseOne) getIntent().getSerializableExtra("realhouseone");
		RealPeopleinHouseBean(people);
		getPeopleinHouse();
		dbUtils=DeviceUtils.getDbUtils(this);
		tv_shou.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					tv_shou.setText("收起");
					cardview.setVisibility(View.VISIBLE);
				}else{
					tv_shou.setText("展开");
					cardview.setVisibility(View.GONE);
				}	
				
			}
		});
		rg_sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (listreal.size()>0) {
					switch (checkedId) {
						case R.id.rb_time:
							if (adapter!=null) {
								Collections.sort(listreal, new SortbyTimeClass());
								adapter.notifyDataSetChanged();
								lv.setSelection(0);
							}
							break;
						case R.id.rb_shihao:
							if (adapter!=null) {
								Collections.sort(listreal, new SortbyRoomCodeClass());
								adapter.notifyDataSetChanged();
								lv.setSelection(0);
							}
							break;
						case R.id.rb_zaizhu:
							if (adapter!=null) {
								Collections.sort(listreal, new SortbyzaizhuClass());
								adapter.notifyDataSetChanged();
								lv.setSelection(0);
							}
							break;
						default:
							break;
					}
				}
			}
		});
	}
	@Override
	protected void onResume() {
		try {
			
		People people_yidengji=	dbUtils.findFirst(Selector.from(People.class).where("cardno", "=", people.cardno).and("homecode", "=", realHouseOne.scode));
		if (people.cardno.trim().substring(0, 4).equals("3205")||people.address.trim().contains("苏州")||people.address.trim().contains("昆山")||people.address.trim().contains("吴江")||people.address.trim().contains("张家港")||people.address.trim().contains("太仓")||people.address.trim().contains("常熟")) {
			btn_jiaru.setEnabled(true);
			btn_banzheng.setEnabled(false);
		}else{
			if (people_yidengji!=null) {
				btn_jiaru.setEnabled(true);
			}else{
				btn_jiaru.setEnabled(false);
			}
		}
		} catch (DbException e) {
			e.printStackTrace();
		}
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	private void getPeopleinHouse(){
		dialogLoading = new DialogLoading(SingleRealPopulationActivity.this, "数据加载中，请稍等...",true);
		dialogLoading.show();
		RequestParams params=new RequestParams(Constants.URL+"syrkServer.aspx");
		params.addBodyParameter("type", "peopleinfor");
		params.addBodyParameter("idcard", people.cardno);
		params.addBodyParameter("house_id", realHouseOne.houseId);
		params.addBodyParameter("strAddr", people.address);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				processPeoleinHouse(result);
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {dialogLoading.dismiss();}
		});
	}
	private void processPeoleinHouse(String json){
		try {
			realPeopleinHouseBean = GsonTools.changeGsonToBean(json, RealPeopleinHouseBean.class);
			list = new ArrayList<RealPeopleinHouseBean.RealPeopleinHouseOne>();
			for (int i = 0; i < realPeopleinHouseBean.data.peoplelist.size(); i++) {
				if (realPeopleinHouseBean.data.peoplelist.get(i).status==1||realPeopleinHouseBean.data.peoplelist.get(i).status==2) {
					list.add(realPeopleinHouseBean.data.peoplelist.get(i));
				}
			}
//			if (list.size()>0) {
//				btn_xinzengertong.setEnabled(true);
//			}
			tv_title.setText(realPeopleinHouseBean.data.peopleState.get(0).strRet+",当前已登记人数："+list.size());
			tv_zhuzhai.setText(realPeopleinHouseBean.data.personCount.get(0).stype.trim()+"\n"+realPeopleinHouseBean.data.personCount.get(0).person_count+"人");
			zhuzhaitype=realPeopleinHouseBean.data.personCount.get(0).stype.trim();
			renshus=realPeopleinHouseBean.data.personCount.get(0).person_count.trim()+"人";
			
			listreal.clear();
			listreal.addAll(realPeopleinHouseBean.data.peoplelist);
			SortbyTimeClass sortbyTimeClass =new SortbyTimeClass();
			Collections.sort(listreal, sortbyTimeClass);
			adapter = new CommonAdapter<RealPeopleinHouseOne>(SingleRealPopulationActivity.this,listreal,R.layout.item_realhousepopulation) {
				@Override
				public void convert(ViewHolder helper, RealPeopleinHouseOne item, int position) {
					String sQx = "在住";
	                if (item.status == -1){
	                    sQx = "所内他处";
	                }else if (item.status == -2){
	                    sQx = "区内所外";
	                }else if (item.status == -3){
	                    sQx = "市内区外";
	                }else if (item.status == -4){
	                    sQx = "市外";
	                }else if (item.status == -5){
	                    sQx = "其他";
	                }
	                if (item.idcard.trim().equals(people.cardno.trim())) {
						canOperate=true;
						btn_xinzeng.setEnabled(true);
						btn_xinzengertong.setEnabled(true);
					}
	                helper.setText(R.id.tv_top,item.sname.trim()+"     "+item.idcard+"     "+item.sex);
	                String[] split = item.chkudt.split("\\s+");
	                if (item.idcard.trim().substring(0, 4).equals("3205")||item.house_addr.trim().contains("苏州")||item.house_addr.trim().contains("昆山")||item.house_addr.trim().contains("吴江")||item.house_addr.trim().contains("张家港")||item.house_addr.trim().contains("太仓")||item.house_addr.trim().contains("常熟")) {
	                	((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
 	                	helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
						helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
 						helper.setText(R.id.tv_time, item.chkudt);
	                }else{
	                	 if (split.length>1) {
	 		                if (isOneYearLater(split[0])) {
	 		                	((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#ff0000"));
	 		                	helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
	 							helper.setText(R.id.tv_time, Html.fromHtml( "<font color=\'#ff0000\'>"+item.chkudt+"</font>"));
								helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#ff0000\'>室号："+item.roomCode+"</font>"));
	 						}else{
	 							((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
	 							helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
	 							helper.setText(R.id.tv_time, item.chkudt);
								helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
	 						}
	 	                }else{
	 	                	((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
	 	                	helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
	 						helper.setText(R.id.tv_time, item.chkudt);
							 helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
	 	                }
	                }
	               
					
				}
			}; 
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					final RealPeopleinHouseOne realPeopleinHouseOne =(RealPeopleinHouseOne) ((ListView)arg0).getItemAtPosition(arg2);
					final int pos=arg2;
					if (realPeopleinHouseOne.idcard.trim().equals(people.cardno.trim())&&isfirstadd) {
						Toast.makeText(SingleRealPopulationActivity.this, "刚加入用户无法操作！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (canOperate) {
						if (realPeopleinHouseOne.idcard.trim().substring(0, 4).equals("3205")||realPeopleinHouseOne.house_addr.trim().contains("苏州")||realPeopleinHouseOne.house_addr.trim().contains("昆山")||realPeopleinHouseOne.house_addr.trim().contains("吴江")||realPeopleinHouseOne.house_addr.trim().contains("张家港")||realPeopleinHouseOne.house_addr.trim().contains("太仓")||realPeopleinHouseOne.house_addr.trim().contains("常熟")) {
							new ActionSheetDialog(SingleRealPopulationActivity.this)
							.builder()
							.setCancelable(true)
							.setCanceledOnTouchOutside(true)
							.addSheetItem("注销", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											showDelectReason(realPeopleinHouseOne,pos);
										}
									}).show();
						}else{
							new ActionSheetDialog(SingleRealPopulationActivity.this)
							.builder()
							.setCancelable(true)
							.setCanceledOnTouchOutside(true)
							.addSheetItem("注销", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											showDelectReason(realPeopleinHouseOne,pos);
										}
									})
							.addSheetItem("延期", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
												SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												String date =sDateFormat.format(new java.util.Date());
												Intent intent = new Intent(SingleRealPopulationActivity.this,HandleIDActivity.class);
												intent.putExtra("people", new People(realPeopleinHouseOne.sname, realPeopleinHouseOne.idcard, realPeopleinHouseOne.idcard.trim().substring(0,6), "变更", CommonUtils.GenerateGUID(), "1", "1",
														MyInfomationManager.getUserName(SingleRealPopulationActivity.this), "1",realPeopleinHouseOne.house_id,realPeopleinHouseOne.house_addr, realPeopleinHouseOne.roomCode,
														MyInfomationManager.getSQNAME(SingleRealPopulationActivity.this),date,realHouseOne.houseId));
												intent.putExtra("isHandle", false);
												if (realHouseOne!=null) {
													intent.putExtra("house", realHouseOne);
												}
												startActivity(intent);
										}
								}).show();
						}
					}else{
						Toast.makeText(SingleRealPopulationActivity.this, "非本房屋住户身份证无法操作！", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void RealPeopleinHouseBean(People people){
		textview.setText(Html.fromHtml("<font color=\'#2360B9\'>姓名  </font><font color=\'#666666\'>"+people.name+"</font>" ));
		tv_zhuzhi.setText(people.address);
		tv_cardno.setText(Html.fromHtml("<font color=\'#2360B9\'>身份证号码  </font><font color=\'#666666\'>"+people.cardno+"</font>" ));
		image.setImageBitmap(CommonUtils.base64ToBitmap(people.picture));
		
	}
	@Event(value={R.id.tv_zhuzhai})
	private void zhuzhaiClick(View view){
		if (realPeopleinHouseBean!=null) {
			RealhouseUser realhouseUser =new RealhouseUser(SingleRealPopulationActivity.this, new OnEnsureClickListener() {
				
				@Override
				public void OnEnSureClick(String zhuzhai, String renshu) {
					tv_zhuzhai.setText(zhuzhai+"\n"+renshu);
					String ren="0";
					if (renshu.equals("无人居住")) {
						ren="0";
					}else{
						ren=renshu.substring(0, renshu.length()-1);
					}
					savePesoncount(ren,zhuzhai);
				}
			}, zhuzhaitype,renshus);
			realhouseUser.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
		}
	}
	@Event(value={R.id.btn_xinzengertong})
	private void addchildClick(View view){
		Intent intent=new Intent(this, AddChildActivity.class);
		intent.putExtra("peopleinhouse", realPeopleinHouseBean);
		intent.putExtra("house", realHouseOne);
		startActivity(intent);
	}
	@Event(value={R.id.btn_jiaru})
	private void addClick(View view){
		if(canJianRu){
			for (int i = 0; i < listreal.size(); i++) {
				if (listreal.get(i).idcard.trim().equals(people.cardno.trim())) {
					Toast.makeText(SingleRealPopulationActivity.this, "该人员已存在于该房屋中！", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			canJianRu=false;
			btn_jiaru.setEnabled(false);
			save(people);	
		}
		
	}
	@Event(value={R.id.btn_banzheng})
	private void banzhengClick(View view){
		Intent intent=new Intent(this, ZanZhuActivity .class);
		intent.putExtra("people", people);
		intent.putExtra("isAdd", true);//判断是加入还是延期，加入发送数据时，不需要再次调用加入接口
		intent.putExtra("house", realHouseOne);
		startActivity(intent);
	}
	@Event(value={R.id.btn_xinzeng})
	private void xinzengClick(View view){
		Intent intent =new Intent(SingleRealPopulationActivity.this, RegisterActivity.class);
		intent.putExtra("isreal", true);
		intent.putExtra("realhouseone", realHouseOne);
		startActivity(intent);
		SingleRealPopulationActivity.this.finish();
	}
	private void showDelectReason(final RealPeopleinHouseOne realPeopleinHouseOne,final int pos){
		layout = (RelativeLayout) LayoutInflater.from(SingleRealPopulationActivity.this).inflate(R.layout.pop_deletep, null);
		popdelete = new PopupWindow(layout,RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
		popdelete.setBackgroundDrawable(new BitmapDrawable());
		popdelete.showAtLocation(findViewById(R.id.root), Gravity.CENTER, 0, 0);
		popdelete.update();
		rg = (RadioGroup) layout.findViewById(R.id.rg);
		Button btn_ensure=(Button) layout.findViewById(R.id.btn_ensure);
		Button btn_cancle=(Button) layout.findViewById(R.id.btn_cancle);
		btn_ensure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioButton radioButton =(RadioButton)layout.findViewById(rg.getCheckedRadioButtonId());
				String statusstr=radioButton.getText().toString().trim();
				String status="-1";
				if (statusstr.equals("所内他处")){
					status = "-1";
                }else if (statusstr.equals("区内所外")){
                	status = "-2";
                }else if (statusstr.equals("市内区外")){
                	status = "-3";
                }else if (statusstr.equals("市外")){
                	status = "-4";
                }else if (statusstr.equals("其他")){
                	status = "-5";
                }
				popdelete.dismiss();
				deletePeople(realPeopleinHouseOne,status,pos);
			}
		});
		btn_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popdelete.dismiss();
			}
		});
	}
	private void deletePeople(final RealPeopleinHouseOne realPeopleinHouseOne,String status,final int pos){
		dialogLoadingd = new DialogLoading(SingleRealPopulationActivity.this, "删除中,请稍等...", true);
		dialogLoadingd.show();
		RequestParams params=new RequestParams(Constants.URL+"syrkServer.aspx");
		params.addBodyParameter("type", "del");
		if (realPeopleinHouseOne.id.equals("")) {
			Toast.makeText(SingleRealPopulationActivity.this, "新增人员不可删除！", Toast.LENGTH_SHORT).show();
		 	dialogLoading.dismiss();
			dialogLoadingd.dismiss();
			return;
		}
		params.addBodyParameter("id", realPeopleinHouseOne.id);
		params.addBodyParameter("status", status);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				System.out.println(result);
				try {
					DeleteRealPeopleBean deleteRealPeopleBean =GsonTools.changeGsonToBean(result, DeleteRealPeopleBean.class);
					if (deleteRealPeopleBean.data.get(0).success.equals("true")) {
						
						listreal.remove(pos);
						adapter.notifyDataSetChanged();
						if (realPeopleinHouseOne.idcard.trim().substring(0, 4).equals("3205")||realPeopleinHouseOne.house_addr.trim().contains("苏州")||realPeopleinHouseOne.house_addr.trim().contains("昆山")||realPeopleinHouseOne.house_addr.trim().contains("吴江")||realPeopleinHouseOne.house_addr.trim().contains("张家港")||realPeopleinHouseOne.house_addr.trim().contains("太仓")||realPeopleinHouseOne.house_addr.trim().contains("常熟")) {
							Toast.makeText(SingleRealPopulationActivity.this, "注销成功~", Toast.LENGTH_SHORT).show();
						}else{
							SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String date =sDateFormat.format(new java.util.Date()); 
							People people=new People(realPeopleinHouseOne.sname, date, realPeopleinHouseOne.idcard, "注销", CommonUtils.GenerateGUID(), "2", 
									MyInfomationManager.getUserName(SingleRealPopulationActivity.this), realHouseOne.scode,realHouseOne.houseAdress);
							DbUtils dbUtils =DeviceUtils.getDbUtils(SingleRealPopulationActivity.this);
							List<People> list=new ArrayList<People>();
							try {
								list = dbUtils.findAll(Selector.from(People.class).where("cardno", "=", realPeopleinHouseOne.idcard));
								if (list!=null) {
								if (list.size()>0) {
									dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", realPeopleinHouseOne.idcard));
								}
								}
								dbUtils.save(people);
								Toast.makeText(SingleRealPopulationActivity.this, "注销成功~",Toast.LENGTH_SHORT).show();
							} catch (Exception e) {
								Toast.makeText(SingleRealPopulationActivity.this, "注销失败~", Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
						}
					}else{
						Toast.makeText(SingleRealPopulationActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {dialogLoadingd.dismiss();}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {dialogLoadingd.dismiss();}
		});
	}
	private void savePesoncount(final String count ,final String type){
		RequestParams params=new RequestParams(Constants.URL+"syrkServer.aspx");
		params.addBodyParameter("type", "exit");
		params.addBodyParameter("person_count",count);
		params.addBodyParameter("id", realHouseOne.houseId);
		params.addBodyParameter("stype", type);
		params.addBodyParameter("last_checker", MyInfomationManager.getUserName(SingleRealPopulationActivity.this));
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				try {
					DeleteRealPeopleBean deleteRealPeopleBean =GsonTools.changeGsonToBean(result, DeleteRealPeopleBean.class);
					if (deleteRealPeopleBean.data.get(0).success.equals("true")) {
						renshus=count+"人";
						zhuzhaitype=type;
					}else{
						Toast.makeText(SingleRealPopulationActivity.this, "修改人数住所类型失败！请重试~", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {}
		});
	}
	private void save(final People people){
		RequestParams params=new RequestParams(Constants.URL+"superOper.aspx");
		params.addBodyParameter("type", "save_ck_population_parent");
		params.addBodyParameter("proc", "sp_save_ck_population_parent");
		params.addBodyParameter("name", people.name);
		params.addBodyParameter("idcard", people.cardno);
//		params.addBodyParameter("name", "李荣辉");
//		params.addBodyParameter("idcard", "362422197909178413");
		params.addBodyParameter("sex", people.sex);
		params.addBodyParameter("house_id", realHouseOne.houseId);
		params.addBodyParameter("hk_num", "");
		params.addBodyParameter("relation", "不详");
		params.addBodyParameter("status", "1");
		params.addBodyParameter("coll_id", "11");
		params.addBodyParameter("hjdz", people.address);
		params.addBodyParameter("pcs", MyInfomationManager.getSQNAME(SingleRealPopulationActivity.this));
		params.addBodyParameter("roomCode", SharePrefUtil.getString(SingleRealPopulationActivity.this, "realroomcode", ""));
		params.addBodyParameter("parent_idcard","");
		x.http().post(params, new Callback.CommonCallback<String>(){
			@Override                                                                                                             
			public void onSuccess(String result) {
				System.out.println(result);
				if (result.trim().equals("<result>0</result>")) {
					canOperate=true;
					btn_xinzeng.setEnabled(true);
					btn_xinzengertong.setEnabled(true);
					Toast.makeText(SingleRealPopulationActivity.this, "添加成功",Toast.LENGTH_SHORT).show();
					canJianRu=false;
					btn_jiaru.setEnabled(false);
					SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String date =sDateFormat.format(new java.util.Date());
					isfirstadd=true;
					listreal.add(0,new RealPeopleinHouseOne(date, "", "", "", "", people.cardno, "", SharePrefUtil.getString(SingleRealPopulationActivity.this, "realroomcode", ""), "不详", people.sex, people.name, 1));
					adapter.notifyDataSetChanged();
					lv.smoothScrollToPosition(0);
					list.add(new RealPeopleinHouseOne("", "", "", "", "", people.cardno, "", SharePrefUtil.getString(SingleRealPopulationActivity.this, "realroomcode", ""), "不详", people.sex, people.name, 1));
					btn_xinzeng.setEnabled(true);
					tv_title.setText(realPeopleinHouseBean.data.peopleState.get(0).strRet+",当前已登记人数："+list.size());
				}else{
					canJianRu=true;
					btn_jiaru.setEnabled(true);
					Toast.makeText(SingleRealPopulationActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
				}
				
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				canJianRu=true;
				btn_jiaru.setEnabled(true);
			}
			@Override
			public void onCancelled(CancelledException cex) {

			}
			@Override
			public void onFinished() {
			}
		});
	}
	public void onEventMainThread(RealPeopleinHouseOne msg){
		listreal.add(0,msg);
		list.add(msg);
		adapter.notifyDataSetChanged();
		lv.smoothScrollToPosition(0);
		tv_title.setText(realPeopleinHouseBean.data.peopleState.get(0).strRet+",当前已登记人数："+list.size());
	}
	
	@Override
	public void onBackPressed() {
		if (realPeopleinHouseBean==null) {
			SingleRealPopulationActivity.this.finish();
		}
		
		try {
			if (!renshus.equals(list.size()+"人")) {
			
			Dialog.showSelectDialog(SingleRealPopulationActivity.this, "当前人数与该房屋总人数不一致，是否要退出？", new DialogClickListener() {
				@Override
				public void confirm() {
					SingleRealPopulationActivity.this.finish();
				}
				@Override
				public void cancel() {
					return;
				}
			});
			}else{
				SingleRealPopulationActivity.this.finish();
			}
		} catch (Exception e) {
			SingleRealPopulationActivity.this.finish();
			e.printStackTrace();
		}
	}
	public static boolean isOneYearLater(String day) {  
		String[] s=day.split("-");
		String dayf=s[0]+"-"+(s[1].length()==1?("0"+s[1]):s[1])+"-"+(s[2].length()==1?("0"+s[2]):s[2]);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        Date nowDate = null;  
        try {  
            nowDate = df.parse(dayf);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        Date newDate2 = new Date(nowDate.getTime() + (long)365 * 24 * 60 * 60 * 1000);  
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String dateOk = simpleDateFormat.format(newDate2);  
        int falg=dateOk.compareTo(simpleDateFormat.format(new Date()));
        if (falg>=0) {
			return false;
		}else{
			return true; 
		}
         
    }
	
}
