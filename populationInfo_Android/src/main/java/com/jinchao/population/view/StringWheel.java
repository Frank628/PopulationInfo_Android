package com.jinchao.population.view;

import com.jinchao.population.R;
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
public class StringWheel extends PopupWindow implements OnClickListener,OnWheelChangedListener{
	
	private WheelView mCarTypeWheel;
	private String CurrentName;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private TextView submit,cancel;
	private OnEnsureClickListener onEnsureClickListener;
	private String[] str;
	public StringWheel(Activity context,String[] str,OnEnsureClickListener onEnsureClickListener) {
		super();
		this.str=str;
		this.onEnsureClickListener = onEnsureClickListener;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView=inflater.inflate(R.layout.wheel_one, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		submit =(TextView) mMenuView.findViewById(R.id.btn_ensure);
		cancel =(TextView) mMenuView.findViewById(R.id.btn_cancle);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		mCarTypeWheel=(WheelView) mMenuView.findViewById(R.id.wv_1);
		mCarTypeWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, str));
		CurrentName=str[0];
		mCarTypeWheel.addChangingListener(this);
		mCarTypeWheel.setVisibleItems(7);
		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
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
		if (wheel == mCarTypeWheel)
		{
				CurrentName= str[newValue];
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ensure:
            onEnsureClickListener.OnEnSureClick(CurrentName);
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
