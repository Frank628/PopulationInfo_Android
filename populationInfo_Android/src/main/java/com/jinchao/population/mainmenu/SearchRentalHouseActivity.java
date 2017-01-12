package com.jinchao.population.mainmenu;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddressOldBean;
import com.jinchao.population.entity.RealHouseInfo;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.view.NavigationLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

@ContentView(R.layout.activity_searchrentalhouse)
public class SearchRentalHouseActivity extends BaseActiviy{
	@ViewInject(R.id.edt_content)EditText edt_content;
	@ViewInject(R.id.tv_content)TextView tv_content;
	@ViewInject(R.id.btn_unregist)Button btn_unregist;
	HouseAddressOldBean houseAddressOldBean;
	private DbUtils dbUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("出租屋查询");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		dbUtils=DeviceUtils.getDbUtils(this);
	}
//	@Event(value={R.id.btn_unregist})
//	private void unregisthouseClick(View view){
//		unregistHouse(houseAddressOldBean.getScode(),houseAddressOldBean.getAddress());
//	}
	@Event(value={R.id.btn_seach})
	private void searchClick(View view){
		String code=edt_content.getText().toString().trim();
		btn_unregist.setVisibility(View.GONE);
		if (edt_content.equals("")) {
			Toast.makeText(this, "请输入房屋编号~", Toast.LENGTH_SHORT).show();
			return;
		}
		if (getIntent().getBooleanExtra(Constants.IS_FROM_REALPOPULATION,false)){
			searchHouseRequest(code);
			return;
		}

		try {
			houseAddressOldBean = dbUtils.findFirst(Selector.from(HouseAddressOldBean.class).where("scode", "=", code));
			if (houseAddressOldBean!=null) {
				tv_content.setText(houseAddressOldBean.toString());
				btn_unregist.setVisibility(View.VISIBLE);
//
			}else{
				tv_content.setText("查无此房屋");
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	private void searchHouseRequest(String content){
		RequestParams params=new RequestParams(Constants.URL+"syrkHouse.aspx");
		params.addBodyParameter("type", "getsyrkhouse");
		params.addBodyParameter("scode", content);
		params.addBodyParameter("user_id",  MyInfomationManager.getUserID(SearchRentalHouseActivity.this));
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				System.out.println(result);
				try {
					RealHouseInfo realHouseInfo= GsonTools.changeGsonToBean(result,RealHouseInfo.class);
					if (realHouseInfo.data.size()>0){
						tv_content.setText(realHouseInfo.data.get(0).toString());
					}else{
						tv_content.setText("查无此房屋");
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

//	private void  unregistHouse(String houseid,String address){
//		getzaizhuPeopleInhouse(houseid,address);
//
//	}
//	private void getzaizhuPeopleInhouse(final String house_code,final String address){
//		showProgressDialog("","正在为您注销请稍等...");
//		final RequestParams params=new RequestParams(Constants.URL+"HouseSave.aspx?");
//		params.addBodyParameter("type", "get_houseinfor");
//		params.addBodyParameter("house_code", house_code);
//		params.addBodyParameter("user_id",  MyInfomationManager.getUserID(SearchRentalHouseActivity.this));
//		x.http().post(params, new Callback.CommonCallback<String>() {
//			@Override
//			public void onSuccess(String result) {
//				System.out.println(result);
//				try {
//					ZaiZhuPeopleBean realHouseInfo= GsonTools.changeGsonToBean(result,ZaiZhuPeopleBean.class);
//					if (realHouseInfo.data.peoplelist==null){
//						try {
//							dbUtils.delete(HouseOperation.class, WhereBuilder.b("house_code", "=", house_code));
//							SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//							String date =sDateFormat.format(new java.util.Date());
//							dbUtils.save(new HouseOperation(house_code,address,date,"注销",MyInfomationManager.getUserName(SearchRentalHouseActivity.this)));
//							Toast.makeText(SearchRentalHouseActivity.this,"注销成功!",Toast.LENGTH_SHORT).show();
//						} catch (DbException e) {
//							e.printStackTrace();
//						}
//						return;
//					}
//					for (int i=0;i<realHouseInfo.data.peoplelist.size();i++){
//						if (realHouseInfo.data.peoplelist.get(i).resdients_status.trim().equals("在住")){
//							People people=dbUtils.findFirst(Selector.from(People.class).where("cardno", "=", realHouseInfo.data.peoplelist.get(i).idcard.trim()));
//							if (people!=null){
//								if (!people.module.equals("注销")){
//									Toast.makeText(SearchRentalHouseActivity.this,"请先注销房屋内的所有人员",Toast.LENGTH_SHORT).show();
//									Intent intent =new Intent(SearchRentalHouseActivity.this,SearchPeopleActivity.class);
//									intent.putExtra("housecode",house_code);
//									startActivity(intent);
//								}else {
//									if (i==(realHouseInfo.data.peoplelist.size()-1)){
//										try {
//											dbUtils.delete(HouseOperation.class, WhereBuilder.b("house_code", "=", house_code));
//											SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//											String date =sDateFormat.format(new java.util.Date());
//											dbUtils.save(new HouseOperation(house_code,address,date,"注销",MyInfomationManager.getUserName(SearchRentalHouseActivity.this)));
//											Toast.makeText(SearchRentalHouseActivity.this,"注销成功!",Toast.LENGTH_SHORT).show();
//										} catch (DbException e) {
//											e.printStackTrace();
//										}
//									}
//								}
//							}else{
//								Toast.makeText(SearchRentalHouseActivity.this,"请先注销房屋内的所有人员",Toast.LENGTH_SHORT).show();
//								Intent intent =new Intent(SearchRentalHouseActivity.this,SearchPeopleActivity.class);
//								intent.putExtra("housecode",house_code);
//								startActivity(intent);
//							}
//						}else{
//							if (i==(realHouseInfo.data.peoplelist.size()-1)){
//								try {
//									dbUtils.delete(HouseOperation.class, WhereBuilder.b("house_code", "=", house_code));
//									SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//									String date =sDateFormat.format(new java.util.Date());
//									dbUtils.save(new HouseOperation(house_code,address,date,"注销",MyInfomationManager.getUserName(SearchRentalHouseActivity.this)));
//									Toast.makeText(SearchRentalHouseActivity.this,"注销成功!",Toast.LENGTH_SHORT).show();
//								} catch (DbException e) {
//									e.printStackTrace();
//								}
//							}
//						}
//					}
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//			@Override
//			public void onError(Throwable ex, boolean isOnCallback) {}
//			@Override
//			public void onCancelled(CancelledException cex) {}
//			@Override
//			public void onFinished() {dismissProgressDialog();}
//		});
//	}
}
