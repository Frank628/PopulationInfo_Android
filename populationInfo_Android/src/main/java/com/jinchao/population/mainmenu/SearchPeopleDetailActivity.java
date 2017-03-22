package com.jinchao.population.mainmenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RealHouseBean;
import com.jinchao.population.entity.RenYuanXinXiBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
@ContentView(R.layout.activity_searchpeopledetail)
public class SearchPeopleDetailActivity extends BaseActiviy{
	@ViewInject(R.id.tv_shihao) TextView tv_shihao;
	@ViewInject(R.id.tv_name) TextView tv_name;
	@ViewInject(R.id.tv_card) TextView tv_card;
	@ViewInject(R.id.tv_status) TextView tv_status;
	@ViewInject(R.id.tv_bianhao) TextView tv_bianhao;
	@ViewInject(R.id.tv_address) TextView tv_address;
	@ViewInject(R.id.tv_time) TextView tv_time;
	private RenyuanInHouseBean.RenyuanInhouseOne renYuanXinXiBean;
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
}
