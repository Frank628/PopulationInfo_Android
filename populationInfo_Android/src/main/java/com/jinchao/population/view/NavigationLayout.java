package com.jinchao.population.view;


import com.jinchao.population.R;

import android.R.integer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavigationLayout extends RelativeLayout{
	private TextView tv_left,tv_right,tv_center;

	public NavigationLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public NavigationLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public NavigationLayout(Context context) {
		super(context);
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		tv_left = (TextView) findViewById(R.id.tv_left);
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_center = (TextView) findViewById(R.id.tv_center);
	}
	public void setLeftText(String text,OnClickListener onClickListener){
		tv_left.setText(text);
		tv_left.setOnClickListener(onClickListener);
		
	}
	public void setLeftTextOnClick(OnClickListener onClickListener){
		tv_left.setOnClickListener(onClickListener);
	}
	public void setLeftText(String text,int res,OnClickListener onClickListener){
		tv_left.setText(text);
		Drawable drawable= getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
		tv_left.setCompoundDrawables(drawable, null, null, null);
		tv_left.setOnClickListener(onClickListener);
	}
	public void setCenterText(String text){
		tv_center.setText(text);
		
	}
	public void setLeftTextVisibility(int visiable){
		tv_left.setVisibility(visiable);
	}
	public void setRightText(String text,OnClickListener onClickListener){
		tv_right.setText(text);
		tv_right.setOnClickListener(onClickListener);
		
	}
	public void setRightText(String text,int res,OnClickListener onClickListener){
		tv_right.setText(text);
		Drawable drawable= getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
		tv_right.setCompoundDrawables(drawable, null, null, null);
		tv_right.setOnClickListener(onClickListener);
	}
}
