package com.jinchao.population.view;

import com.jinchao.population.R;
import com.jinchao.population.config.Constants;
import com.jinchao.population.view.NationPop.OnEnsureClickListener;
import com.jinchao.population.widget.wheel.OnWheelChangedListener;
import com.jinchao.population.widget.wheel.WheelView;
import com.jinchao.population.widget.wheel.adapter.ArrayWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;
/**
 * 选择弹出框
 * @author FRANK
 *
 */
public class PopBianzheng extends PopupWindow implements OnClickListener,OnWheelChangedListener{
	
	private WheelView mWheelView1,mWheelView2,mWheelView3;
	private String CurrentJUZHUSHIJIAN,CurrentLINGQUFANGSHI,CurrentSHENGQINGLEIBIE,CurrentJUZHUSHIJIANcode,CurrentSHENGQINGLEIBIEcode;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private TextView submit,cancel;
	private OnbEnsureClickListener onEnsureClickListener;
	public  interface OnbEnsureClickListener{
		void onEnsureClick(String juzhu,String lingqu,String shenq,String shijiancode,String leibiecode);
	}
	private String[] str;
	public PopBianzheng(Activity context,OnbEnsureClickListener onEnsureClickListener) {
		super();
		this.onEnsureClickListener = onEnsureClickListener;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView=inflater.inflate(R.layout.pop_banzheng_wheel_three, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		submit =(TextView) mMenuView.findViewById(R.id.btn_ensure);
		cancel =(TextView) mMenuView.findViewById(R.id.btn_cancle);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		mWheelView1=(WheelView) mMenuView.findViewById(R.id.wv_1);
		mWheelView2=(WheelView) mMenuView.findViewById(R.id.wv_2);
		mWheelView3=(WheelView) mMenuView.findViewById(R.id.wv_3);
		mWheelView1.setViewAdapter(new ArrayWheelAdapter<String>(context, Constants.JUZHUSHIJIAN));
		mWheelView2.setViewAdapter(new ArrayWheelAdapter<String>(context, Constants.LINGQUFANGSHI));
		mWheelView3.setViewAdapter(new ArrayWheelAdapter<String>(context, Constants.SHENGQINGLEIBIE));
		CurrentJUZHUSHIJIAN=Constants.JUZHUSHIJIAN[0];
		CurrentLINGQUFANGSHI=Constants.LINGQUFANGSHI[0];
		CurrentSHENGQINGLEIBIE=Constants.SHENGQINGLEIBIE[0];
		CurrentJUZHUSHIJIANcode=Constants.JUZHUSHIJIAN_CODE[0];
		CurrentSHENGQINGLEIBIEcode=Constants.SHENGQINGLEIBIE_C0DE[0];
		mWheelView1.addChangingListener(this);
		mWheelView1.setVisibleItems(7);
		mWheelView2.addChangingListener(this);
		mWheelView2.setVisibleItems(7);
		mWheelView3.addChangingListener(this);
		mWheelView3.setVisibleItems(7);
		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
	}
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mWheelView1)
		{
				CurrentJUZHUSHIJIAN= Constants.JUZHUSHIJIAN[newValue];
		}
		if (wheel == mWheelView2)
		{
				CurrentLINGQUFANGSHI= Constants.LINGQUFANGSHI[newValue];
		}
		if (wheel == mWheelView3)
		{
				CurrentSHENGQINGLEIBIE= Constants.SHENGQINGLEIBIE[newValue];
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ensure:
            onEnsureClickListener.onEnsureClick(CurrentJUZHUSHIJIAN,CurrentLINGQUFANGSHI,CurrentSHENGQINGLEIBIE,CurrentJUZHUSHIJIANcode,CurrentSHENGQINGLEIBIEcode);
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
