package com.jinchao.population.view;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import com.jinchao.population.R;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.UserBean;
import com.jinchao.population.entity.UserBean.AccountOne;
import com.jinchao.population.main.LoginActivity;
import com.jinchao.population.utils.ArraySortUtil;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.widget.wheel.OnWheelChangedListener;
import com.jinchao.population.widget.wheel.WheelView;
import com.jinchao.population.widget.wheel.adapter.ArrayWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class PopupUser extends PopupWindow implements OnWheelChangedListener,OnClickListener{
	private OnEnsureClickListener onEnsureClickListener;
	private View mPopView;
	private ViewFlipper viewfipper;
	private WheelView PCSView,USERView;
	private TextView btn_ensure,btn_cancle;
	private AccountOne accountOne;
	private Activity context;
	private String[] PSCName,USERName,UserId;
	private Map<String, String[]> UserNameMap=new HashMap<String, String[]>();
	private Map<String, String[]> UserIdMap=new HashMap<String, String[]>();
	private String mCurrentPcs,mCurrentUserName,mCurrentUserId;
	public interface OnEnsureClickListener{
		void OnEnSureClick(AccountOne accountOne);
	}

	public PopupUser(Activity context,OnEnsureClickListener onEnsureClickListener) {
		this.onEnsureClickListener = onEnsureClickListener;
		this.context=context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopView=inflater.inflate(R.layout.wheel_two, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		PCSView=(WheelView) mPopView.findViewById(R.id.wv_1);
		USERView=(WheelView) mPopView.findViewById(R.id.wv_2);
		btn_ensure=(TextView) mPopView.findViewById(R.id.btn_ensure);
		btn_cancle=(TextView) mPopView.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);
		btn_ensure.setOnClickListener(this);
		
		viewfipper.addView(mPopView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
		initData();
	}
	private void initData(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String users="";
					if (SharePrefUtil.getString(context, Constants.USER_DB,"").trim().equals("")){
						users=FileUtils.getAssetsTxt(context, "user.txt");
					}else{
						users=SharePrefUtil.getString(context, Constants.USER_DB,"").trim();
						Log.i("user_pop",users);
					}

					UserBean userBean =GsonTools.changeGsonToBean(users, UserBean.class);
					PSCName=new String[userBean.data.size()];
					for (int i = 0; i < userBean.data.size(); i++) {
						PSCName[i]=userBean.data.get(i).pcsName;
						USERName=new String[userBean.data.get(i).account.size()];
						UserId=new String[userBean.data.get(i).account.size()];
						for (int j = 0; j <userBean.data.get(i).account.size(); j++) {
							USERName[j]=userBean.data.get(i).account.get(j).userName;
							UserId[j]=userBean.data.get(i).account.get(j).userId;
						}
						UserNameMap.put(userBean.data.get(i).pcsName, USERName);
						UserIdMap.put(userBean.data.get(i).pcsName, UserId);
					}
					PCSView.setViewAdapter(new ArrayWheelAdapter<String>(context, PSCName));
					PCSView.addChangingListener(PopupUser.this);
					USERView.addChangingListener(PopupUser.this);
					PCSView.setVisibleItems(7);
					USERView.setVisibleItems(7);
					updateUser();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void updateUser(){
		int pcsCurrentPostion =PCSView.getCurrentItem();
		mCurrentPcs=PSCName[pcsCurrentPostion];
		String users[] =UserNameMap.get(mCurrentPcs);
		String userids[] =UserIdMap.get(mCurrentPcs);
		if (users==null) {
			users=new String[]{""};
		}
		if (userids==null) {
			userids=new String[]{""};
		}
		USERView.setViewAdapter(new ArrayWheelAdapter<String>(context, users));
		USERView.setCurrentItem(0);
		mCurrentUserId=userids[0];
		mCurrentUserName=users[0];
		
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel==PCSView) {
			updateUser();
		}else if (wheel==USERView) {
			mCurrentUserName=UserNameMap.get(mCurrentPcs)[newValue];
			mCurrentUserId=UserIdMap.get(mCurrentPcs)[newValue];
		}
	}
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ensure:
			onEnsureClickListener.OnEnSureClick(new AccountOne(mCurrentUserId, mCurrentUserName));
			this.dismiss();
			break;
		case R.id.btn_cancle:
			this.dismiss();
			break;
		default:
			break;
		}
		
	}
}
