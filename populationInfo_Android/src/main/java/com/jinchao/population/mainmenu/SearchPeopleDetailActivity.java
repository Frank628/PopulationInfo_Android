package com.jinchao.population.mainmenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.alienPeople.IDCardResultActivity;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RealHouseBean;
import com.jinchao.population.entity.RenYuanXinXiBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
@ContentView(R.layout.activity_searchpeopledetail)
public class SearchPeopleDetailActivity extends BaseActiviy{
	@ViewInject(R.id.tv_shihao) TextView tv_shihao;
	@ViewInject(R.id.tv_name) TextView tv_name;
	@ViewInject(R.id.tv_card) TextView tv_card;
	@ViewInject(R.id.tv_phone) TextView tv_phone;
	@ViewInject(R.id.rl_phone) RelativeLayout rl_phone;
	@ViewInject(R.id.rl_phoneline) View rl_phoneline;
	@ViewInject(R.id.tv_status) TextView tv_status;
	@ViewInject(R.id.tv_bianhao) TextView tv_bianhao;
	@ViewInject(R.id.tv_address) TextView tv_address;
	@ViewInject(R.id.tv_time) TextView tv_time;
	@ViewInject(R.id.btn_zhuxiao)TextView btn_zhuxiao;
	private RenyuanInHouseBean.RenyuanInhouseOne renYuanXinXiBean;
	private String SQCODE="";
	private boolean isHistory=false;
	private String CurrentHouse="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("人员信息详情");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		renYuanXinXiBean = (RenyuanInHouseBean.RenyuanInhouseOne ) getIntent().getSerializableExtra("renyuan");
		isHistory=getIntent().getBooleanExtra("isHistory",false);
		if (isHistory){
			rl_phone.setVisibility(View.VISIBLE);
			rl_phoneline.setVisibility(View.VISIBLE);
			CurrentHouse=getIntent().getStringExtra("CurrentHouse");
			btn_zhuxiao.setVisibility(View.GONE);
			requestYanZheng(renYuanXinXiBean.idcard);
			return;
		}
		String phone =getIntent().getStringExtra("phone");
		if (phone!=null){
			rl_phone.setVisibility(View.VISIBLE);
			rl_phoneline.setVisibility(View.VISIBLE);
			tv_phone.setText(phone.trim());
		}
		tv_shihao.setText(renYuanXinXiBean.shihao);
		tv_card.setText(renYuanXinXiBean.idcard);
		tv_name.setText(renYuanXinXiBean.sname);
		tv_status.setText(renYuanXinXiBean.resdients_status);
		tv_bianhao.setText(renYuanXinXiBean.house_code);
		tv_address.setText(renYuanXinXiBean.house_addr);
		tv_time.setText(renYuanXinXiBean.write_time);
	}
	
	@Event(value={R.id.btn_yanqi})
	private void yanqi(View view){
		if ((isHistory&&(!SQCODE.trim().equals(MyInfomationManager.getSQCODE(this))))||(isHistory&&(isHistory?(!CurrentHouse.equals(renYuanXinXiBean.house_code)):false))){
			Dialog.showSelectDialog(this, "网上地址与当前地址不一致！请前往做‘变更’！", new Dialog.DialogClickListener() {
				@Override
				public void confirm() {

					tongwubiangeng(null);
				}

				@Override
				public void cancel() {

				}
			});
			return;
		}
		Dialog.showSelectDialog(this, "是否需要修改室号？", new Dialog.DialogClickListener() {
				@Override
				public void confirm() {
					View view=SearchPeopleDetailActivity.this.getLayoutInflater().inflate(R.layout.alert_roomcodedialog,null);
					final ValidateEidtText edt_shihao=(ValidateEidtText) view.findViewById(R.id.shihao);
					new AlertDialog.Builder(SearchPeopleDetailActivity.this)
							.setTitle("请输入4位室号")
							.setView(view)
							.setPositiveButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									hidenSoftKeyBoard(edt_shihao);
									String shihao=edt_shihao.getText().toString().trim();
									if (!CommonUtils.isRoom(shihao)){
										Toast.makeText(SearchPeopleDetailActivity.this,"请输入四位数字或字母的室号！",Toast.LENGTH_SHORT).show();
										return;
									}
									renYuanXinXiBean.shihao=shihao;
									yanqi();
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							hidenSoftKeyBoard(edt_shihao);
						}
					}).show();
				}

				@Override
				public void cancel() {
					yanqi();
				}
		},"修改室号","直接延期");


	}
	private void yanqi(){
		if(TextUtils.isEmpty(renYuanXinXiBean.house_addr)||TextUtils.isEmpty(renYuanXinXiBean.house_code)){
			Toast.makeText(this, "无房屋编号或地址，无法延期~", Toast.LENGTH_SHORT).show();
			return;
		}
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date =sDateFormat.format(new java.util.Date());
		People people=new People(renYuanXinXiBean.sname, renYuanXinXiBean.idcard, renYuanXinXiBean.idcard.substring(0, 6), "变更", CommonUtils.GenerateGUID(), "1", "1",
				MyInfomationManager.getUserName(SearchPeopleDetailActivity.this), "1", renYuanXinXiBean.house_code, renYuanXinXiBean.house_addr, renYuanXinXiBean.shihao,
				MyInfomationManager.getSQNAME(SearchPeopleDetailActivity.this),date);
		DbUtils dbUtils =DeviceUtils.getDbUtils(this);
		List<People> list=new ArrayList<People>();
		try {
			list = dbUtils.findAll(Selector.from(People.class).where("cardno", "=", renYuanXinXiBean.idcard));
			if (list!=null) {
				if (list.size()>0) {
					dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", renYuanXinXiBean.idcard));
				}
			}
			dbUtils.save(people);
			Toast.makeText(this, "延期成功~", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "延期失败~", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	@Event(value={R.id.btn_tongwu})
	private void tongwubiangeng(View view){
//		if(isHistory&&(!SQCODE.trim().equals(MyInfomationManager.getSQCODE(this)))){
			renYuanXinXiBean.house_addr="";
			renYuanXinXiBean.house_code="";
//		}
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date =sDateFormat.format(new java.util.Date());
		Intent intent = new Intent(SearchPeopleDetailActivity.this,HandleIDActivity.class);
		intent.putExtra("people", new People(renYuanXinXiBean.sname, renYuanXinXiBean.idcard, renYuanXinXiBean.idcard.trim().substring(0,6), "变更", CommonUtils.GenerateGUID(), "1", "1",
				MyInfomationManager.getUserName(SearchPeopleDetailActivity.this), "1",renYuanXinXiBean.house_code,renYuanXinXiBean.house_addr, renYuanXinXiBean.shihao,
				MyInfomationManager.getSQNAME(SearchPeopleDetailActivity.this),date));
		intent.putExtra("isHandle", false);
		if (renYuanXinXiBean!=null) {
			intent.putExtra(Constants.HOUSE_INFOR,renYuanXinXiBean);
		}
		startActivity(intent);
	}

	@Event(value={R.id.btn_zhuxiao})
	private void zhuxiao(View view){
		if(TextUtils.isEmpty(renYuanXinXiBean.house_addr)||TextUtils.isEmpty(renYuanXinXiBean.house_code)){
			Toast.makeText(this, "无房屋编号或地址，无法注销~", Toast.LENGTH_SHORT).show();
			return;
		}
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		String date =sDateFormat.format(new java.util.Date()); 
		People people=new People(renYuanXinXiBean.sname, date, renYuanXinXiBean.idcard, "注销", CommonUtils.GenerateGUID(), "2",
				MyInfomationManager.getUserName(SearchPeopleDetailActivity.this), renYuanXinXiBean.house_code,renYuanXinXiBean.house_addr);
		DbUtils dbUtils =DeviceUtils.getDbUtils(this);
		List<People> list=new ArrayList<People>();
		try {
			list = dbUtils.findAll(Selector.from(People.class).where("cardno", "=", renYuanXinXiBean.idcard));
			if (list!=null) {
			if (list.size()>0) {
				dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", renYuanXinXiBean.idcard));
			}
			}
			dbUtils.save(people);
			Toast.makeText(this, "注销成功~", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "注销失败~", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	private void requestYanZheng(final String idcard){
		showProgressDialog("","数据加载中...");
		RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=get_people&idcard="+idcard);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				People peoplefromXml=XmlUtils.parseXMLtoPeople(result);
				SQCODE=XmlUtils.parseXMLtoShequcode(result);
				processData(peoplefromXml);
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				requestYanZheng(renYuanXinXiBean.idcard);
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {
				dismissProgressDialog();
			}
		});
	}
	private void processData(People people){
		tv_shihao.setText("");
		tv_card.setText(people.getCardno());
		tv_phone.setText(people.getPhone());
		tv_name.setText(people.getName());
		tv_status.setText("注销");
		tv_bianhao.setText(people.getHomecode());
		tv_address.setText(people.getResidentAddress());
		tv_time.setText("");
		renYuanXinXiBean.house_code=people.getHomecode();
		renYuanXinXiBean.house_addr=people.getResidentAddress();
		renYuanXinXiBean.sname=people.getName();
	}

}
