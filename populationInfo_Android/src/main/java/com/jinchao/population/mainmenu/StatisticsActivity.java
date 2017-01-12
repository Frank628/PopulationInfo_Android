package com.jinchao.population.mainmenu;

import java.util.Calendar;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.RenYuanXinXiBean;
import com.jinchao.population.entity.StatisticsBean;
import com.jinchao.population.entity.StatisticsJsonBean;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.NavigationLayout;
@ContentView(R.layout.activity_statistics)
public class StatisticsActivity extends BaseActiviy{
	@ViewInject(R.id.tv_start)private TextView tv_start;
	@ViewInject(R.id.tv_end)private TextView tv_end;
	@ViewInject(R.id.tv_content)private TextView tv_content;
	private Calendar c;
	private int yearI=1999,MonthI=1,DayI=1;
	private String dengji="登记数： 0",biangeng="变更数： 0",zhuxiao="注销数： 0";
	private DialogLoading dialogLoading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("作业统计");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		c = Calendar.getInstance();
		yearI=c.get(Calendar.YEAR);
		MonthI=c.get(Calendar.MONTH);
		DayI=c.get(Calendar.DAY_OF_MONTH);
		tv_start.setText(yearI+"-"+((MonthI+1)>9?(MonthI+1):("0"+(MonthI+1)))+"-"+(DayI>9?DayI:("0"+DayI)));
		tv_end.setText(yearI+"-"+((MonthI+1)>9?(MonthI+1):("0"+(MonthI+1)))+"-"+(DayI>9?DayI:("0"+DayI)));
		dialogLoading=new DialogLoading(this, "数据加载中...", true);
	}
	
	private OnDateSetListener onDateChangedListenerstart=new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			tv_start.setText(year+"-"+(((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1))))+"-"+(dayOfMonth>9?dayOfMonth:("0"+dayOfMonth)));
			
		}
	};
	private OnDateSetListener onDateChangedListenerend=new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			tv_end.setText(year+"-"+(((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1))))+"-"+(dayOfMonth>9?dayOfMonth:("0"+dayOfMonth)));
			
		}
	};
	@Event(value={R.id.rl_start})
	private void startClick(View view){
		DatePickerDialog datePickerDialogstart=new DatePickerDialog(this,onDateChangedListenerstart, yearI, MonthI, DayI);
		datePickerDialogstart.show();
	}
	@Event(value={R.id.rl_end})
	private void endClick(View view){
		DatePickerDialog datePickerDialogend=new DatePickerDialog(this,onDateChangedListenerend, yearI, MonthI, DayI);
		datePickerDialogend.show();
	}
	@Event(value={R.id.btn_ensure})
	private void ensureClick(View view){
		String start =tv_start.getText().toString().trim();
		String end =tv_end.getText().toString().trim();
		getRequest(start, end);
	}
	private void getRequest(String start,String end){
		dialogLoading.show();
		dengji="登记数： 0";biangeng="变更数： 0";zhuxiao="注销数： 0";
		RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
		params.addBodyParameter("type", "get_count");//http://222.92.144.66:91/population/GdPeople.aspx?type=get_count&ch=A10000420F6A7F&start=2016-12-10&end=2017-1-9
		params.addBodyParameter("ch", CommonUtils.getIMEI(StatisticsActivity.this));
		params.addBodyParameter("start", start);
		params.addBodyParameter("end", end);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				dialogLoading.dismiss();
				Log.i("Tongji",result);
				try {
					StatisticsJsonBean statisticsJsonBean=GsonTools.changeGsonToBean(result, StatisticsJsonBean.class);
					for (int i = 0; i < statisticsJsonBean.data.size(); i++) {
						if (statisticsJsonBean.data.get(i).action_type.equals("0")) {
							 dengji="登记数： "+statisticsJsonBean.data.get(i).acount;
						}
						if (statisticsJsonBean.data.get(i).action_type.equals("1")) {
							biangeng="变更数： "+statisticsJsonBean.data.get(i).acount;
						}
						if (statisticsJsonBean.data.get(i).action_type.equals("2")) {
							zhuxiao="注销数： "+statisticsJsonBean.data.get(i).acount;
						}
					}
					tv_content.setText(dengji+"\n\n\n"+biangeng+"\n\n\n"+zhuxiao);
				} catch (Exception e) {
					tv_content.setText(dengji+"\n\n\n"+biangeng+"\n\n\n"+zhuxiao);
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
			public void onFinished(){
				dialogLoading.dismiss();
			}
		});
	}
}
