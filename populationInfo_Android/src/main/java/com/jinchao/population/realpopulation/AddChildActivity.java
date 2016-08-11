package com.jinchao.population.realpopulation;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RealPeopleinHouseBean;
import com.jinchao.population.entity.RealHouseBean.RealHouseOne;
import com.jinchao.population.entity.RealPeopleinHouseBean.RealPeopleinHouseOne;
import com.jinchao.population.mainmenu.AlienPopulationActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.RealhouseUser;
import com.jinchao.population.view.RealhouseUser.OnEnsureClickListener;
import com.jinchao.population.widget.ActionSheetDialog;
import com.jinchao.population.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.jinchao.population.widget.ActionSheetDialog.SheetItemColor;

import de.greenrobot.event.EventBus;
@ContentView(R.layout.activity_addchild)
public class AddChildActivity extends BaseActiviy{
	@ViewInject(R.id.spinner)private Spinner spinner;
	@ViewInject(R.id.rg)private RadioGroup rg;
	@ViewInject(R.id.edt_name)private EditText edt_name;
	@ViewInject(R.id.tv_birth)private TextView tv_birth;
	private RealPeopleinHouseBean realPeopleinHouseBean;
	private RealHouseOne realHouseOne;
	String parents="";
	String roomcode="";
	String childname="";
	String hjdz="";
	private Calendar c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("添加儿童");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		navigationLayout.setRightText("保存",new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name=edt_name.getText().toString().trim();
				childname=name;
				if (TextUtils.isEmpty(name)) {
					Toast.makeText(AddChildActivity.this, "请输入姓名！", 0).show();
					return;
				}
				String sex=((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString().trim();
				if (!TextUtils.isEmpty(parents)) {
					String str[]=parents.split(",");
					if (TextUtils.isEmpty(tv_birth.getText().toString().trim())) {
						Toast.makeText(AddChildActivity.this, "请选择出生日期！", 0).show();
						return;
					}
					save(str,name+"-"+tv_birth.getText().toString().trim(),sex);
				}
			}
		});
		c = Calendar.getInstance();
		realPeopleinHouseBean=(RealPeopleinHouseBean) getIntent().getSerializableExtra("peopleinhouse");
		realHouseOne=(RealHouseOne) getIntent().getSerializableExtra("house");
		initData(realPeopleinHouseBean);
	}
	private void initData(final RealPeopleinHouseBean realPeopleinHouseBean){
		List<RealPeopleinHouseOne> list =new ArrayList<RealPeopleinHouseBean.RealPeopleinHouseOne>();
		for (int i = 0; i < realPeopleinHouseBean.data.peoplelist.size(); i++) {
			if (realPeopleinHouseBean.data.peoplelist.get(i).status==1||realPeopleinHouseBean.data.peoplelist.get(i).status==2) {
				list.add(realPeopleinHouseBean.data.peoplelist.get(i));
			}
		}
		String mItems[]=new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			mItems[i]=list.get(i).sname.trim()+","+list.get(i).idcard;
		}
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinner_textview, mItems);
		adapter.setDropDownViewResource(R.layout.spinner_textview);
		//绑定 Adapter到控件
		spinner .setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
		    	parents=(String)parent.getItemAtPosition(pos);
		    	roomcode=realPeopleinHouseBean.data.peoplelist.get(pos).roomCode;
		    	hjdz=realPeopleinHouseBean.data.peoplelist.get(pos).hjdz.trim();
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		        // Another interface callback
		    }
		});
	}
	private void save(String str[],final String name,final String sex){
		RequestParams params=new RequestParams(Constants.URL+"superOper.aspx");
		params.addBodyParameter("type", "save_ck_population_parent");
		params.addBodyParameter("proc", "sp_save_ck_population_parent");
		params.addBodyParameter("name", childname);
		params.addBodyParameter("idcard",name);
		params.addBodyParameter("sex", sex);
		params.addBodyParameter("house_id", realHouseOne.houseId.trim());
		params.addBodyParameter("hk_num", "");
		params.addBodyParameter("relation", "不详");
		params.addBodyParameter("status", "1");
		params.addBodyParameter("coll_id",MyInfomationManager.getUserID(AddChildActivity.this));
		params.addBodyParameter("hjdz", hjdz);
		params.addBodyParameter("pcs", MyInfomationManager.getSQNAME(AddChildActivity.this));
		params.addBodyParameter("roomCode", roomcode.trim());
		params.addBodyParameter("parent_idcard",str[1]);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (result.trim().equals("<result>0</result>")) {
					SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
					String date =sDateFormat.format(new java.util.Date());
					EventBus.getDefault().post(new RealPeopleinHouseOne(date, "", "", "", "", name, MyInfomationManager.getSQNAME(AddChildActivity.this), roomcode, "不详", sex, childname, 1));
					AddChildActivity.this.finish();
				}else{
					Toast.makeText(AddChildActivity.this, "添加失败", 0).show();
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
	@Event(value={R.id.tv_birth})
	private void birthClick(View view){
		DatePickerDialog datePickerDialog =new DatePickerDialog(AddChildActivity.this, onDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		datePickerDialog.show();
	}
	OnDateSetListener onDateSetListener =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			tv_birth.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+((dayOfMonth+1)>9?((dayOfMonth+1)+""):("0"+(dayOfMonth+1))));
			
		}
	};
}
