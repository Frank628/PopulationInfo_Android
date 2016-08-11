package com.jinchao.population.main;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.Common;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.SysApplication;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.MsgBean;
import com.jinchao.population.entity.VersionBean;
import com.jinchao.population.mainmenu.AlienPopulationActivity;
import com.jinchao.population.mainmenu.UpLoadActivity;
import com.jinchao.population.realpopulation.RealPopulationActivity;
import com.jinchao.population.service.DownLoadService;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.Dialog.DialogClickListener;
@ContentView(R.layout.activity_mainmenu)
public class MainMenuActivity extends BaseActiviy{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("主菜单");
		navigationLayout.setLeftTextVisibility(View.GONE);

	}

	@Event(value={R.id.iv_outpopulation})
	private void outPopulationClick(View view){
		Intent intent =new Intent();
		intent.setClass(this, AlienPopulationActivity.class);
		startActivity(intent);
		
	}
	@Event(value={R.id.iv_send})
	private void sendClick(View view){
		Intent intent =new Intent();
		intent.setClass(this, UpLoadActivity.class);
		startActivity(intent);
	}
	
	@Event(value={R.id.iv_allpopulation})
	private void realpopulationClick(View view){
		Intent intent =new Intent();
		intent.setClass(this, RealPopulationActivity.class);
		startActivity(intent);
	}

}
