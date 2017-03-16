package com.jinchao.population.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ViewFlipper;

import com.jinchao.population.R;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择室号
 * @author FRANK
 *
 */
public class PopRoom extends PopupWindow{

	private View mMenuView;
	private ViewFlipper viewfipper;
	private ListView lv;
	private OnbEnsureClickListener onEnsureClickListener;
	public  interface OnbEnsureClickListener{
		void onEnsureClick(String type);
	}
	public PopRoom(Activity context, final OnbEnsureClickListener onEnsureClickListener,List<String> list) {
		super();
		this.onEnsureClickListener = onEnsureClickListener;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView=inflater.inflate(R.layout.pop_important, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		list.add(0,"不选");
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



}
