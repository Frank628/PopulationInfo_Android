package com.jinchao.population.mainmenu;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.view.NavigationLayout;
@ContentView(R.layout.activity_sys)
public class SysActivity extends BaseActiviy{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("系统设置");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	@Event(value={R.id.ib_tongji})
	private void statisticClick(View view){
		Intent intent =new Intent(this, StatisticsActivity.class);
		startActivity(intent);
	}
	@Event(value={R.id.ib_fasong})
	private void fasongClick(View view){
		Intent intent =new Intent(this, UpLoadActivity.class);
		startActivity(intent);
	}
}
