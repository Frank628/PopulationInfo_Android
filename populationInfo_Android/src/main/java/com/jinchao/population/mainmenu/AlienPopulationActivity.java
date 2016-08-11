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
@ContentView(R.layout.activity_alienpopulation)
public class AlienPopulationActivity extends BaseActiviy{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("采集类别");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	@Event(value={R.id.iv_register})
	private void onRegistClick(View view){
		Intent intent =new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
	@Event(value={R.id.iv_rentalhouseregist})
	private void rentalhouseregistClick(View view){
		Intent intent =new Intent(this, RegistRentalHouseActivity.class);
		startActivity(intent);
	}
	@Event(value={R.id.iv_personinfocheck})
	private void personinfocheckClick(View view){
		Intent intent =new Intent(this, SearchPeopleActivity.class);
		startActivity(intent);
	}
	@Event(value={R.id.iv_rentalhousecheck})
	private void rentalhousecheckClick(View view){
		Intent intent =new Intent(this, SearchRentalHouseActivity.class);
		startActivity(intent);
	}
	@Event(value={R.id.iv_photo})
	private void reshootClick(View view){
		Intent intent =new Intent(this, ReshootActivity.class);
		startActivity(intent);
	}
	@Event(value={R.id.iv_sys})
	private void sysClick(View view){
		Intent intent =new Intent(this, SysActivity.class);
		startActivity(intent);
	}
}
