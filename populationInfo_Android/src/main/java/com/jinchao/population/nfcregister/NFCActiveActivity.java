package com.jinchao.population.nfcregister;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.caihua.cloud.common.entity.NFCSearch.ErrorStatus;
import com.caihua.cloud.common.entity.NFCSearch.UserType;
import com.caihua.cloud.common.util.NFCHttpUtil;
import com.caihua.cloud.common.util.NFCHttpUtil.SearchCallback;
import com.jinchao.population.R;
import com.jinchao.population.view.NavigationLayout;

import java.io.IOException;

public class NFCActiveActivity extends FragmentActivity {
	ViewPager mainVP;
	NFCActiveViewPagerAdapter mainViewPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfcactive);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("NFC读卡功能激活");
		navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mainVP = (ViewPager) findViewById(R.id.mainVP);
		mainViewPagerAdapter = new NFCActiveViewPagerAdapter(this, getSupportFragmentManager(), mainVP);
		mainVP.setAdapter(mainViewPagerAdapter);
		mainVP.setCurrentItem(NFCActiveViewPagerAdapter.WAIT_FRAG, false);
		NFCHttpUtil.search(new SearchCallback() {

			@Override
			public void onSearchSuccess(UserType userType, String userMsg, String activeCode, long startTime, long endTime) {
				// TODO Auto-generated method stub
				final ActiveInfo activeInfo = new ActiveInfo();
				activeInfo.setActCode(activeCode);
				activeInfo.setStartTime(startTime);
				activeInfo.setEndTime(endTime);
				final Bundle args = new Bundle();
				args.putParcelable(ActiveInfo.class.getName(), activeInfo);
				final UserType currentUserType = userType;
				final String currentUserMsg = userMsg;
				NFCActiveActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						switch (currentUserType) {
						case TRAIL_USER: {
							Toast.makeText(NFCActiveActivity.this,"试用通道已关闭，请注册为正式用户", Toast.LENGTH_SHORT).show();
							mainVP.setCurrentItem(NFCActiveViewPagerAdapter.UNACTIVE_FRAG, false);
							break;
						}
						case FORMAL_USER: {
							Toast.makeText(NFCActiveActivity.this, currentUserMsg, Toast.LENGTH_SHORT).show();
							mainViewPagerAdapter.getItem(NFCActiveViewPagerAdapter.FORMAL_USER_FRAG).setArguments(args);
							mainVP.setCurrentItem(NFCActiveViewPagerAdapter.FORMAL_USER_FRAG, false);
							break;
						}
						}
					}

				});
			}

			@Override
			public void onSearchError(ErrorStatus status, String errorMsg) {
				final ErrorStatus currentStatus = status;
				final String currentErrorMsg = errorMsg;
				// TODO Auto-generated method stub
				NFCActiveActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(NFCActiveActivity.this, currentErrorMsg, Toast.LENGTH_SHORT).show();
						switch (currentStatus) {
						case NOT_REGISTERED:
							mainVP.setCurrentItem(NFCActiveViewPagerAdapter.UNACTIVE_FRAG, false);
							break;
						case PERMISSION_EXPIRE:
							mainVP.setCurrentItem(NFCActiveViewPagerAdapter.ACTIVE_FRAG, false);
							break;
						case RESPONSE_BODY_ERR:
							mainVP.setCurrentItem(NFCActiveViewPagerAdapter.UNACTIVE_FRAG, false);
							break;
						case UNKNOW_ERR:
							mainVP.setCurrentItem(NFCActiveViewPagerAdapter.UNACTIVE_FRAG, false);
							break;
						case REQUEST_ERR:
							mainVP.setCurrentItem(NFCActiveViewPagerAdapter.UNACTIVE_FRAG, false);
							break;
						}
					}

				});
			}

			@Override
			public void onNetworkError(IOException e) {
				// TODO Auto-generated method stub
				NFCActiveActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(NFCActiveActivity.this, "验证失败，请确认网络连接是否正常", Toast.LENGTH_SHORT).show();
						mainVP.setCurrentItem(NFCActiveViewPagerAdapter.UNACTIVE_FRAG, false);
					}
				});
			}
		});
	}
}
