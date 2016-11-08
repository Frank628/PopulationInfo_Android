package com.jinchao.population.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jinchao.population.R;
import com.jinchao.population.config.Constants;
import com.jinchao.population.widget.wheel.OnWheelChangedListener;
import com.jinchao.population.widget.wheel.WheelView;
import com.jinchao.population.widget.wheel.adapter.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择弹出框
 * @author FRANK
 *
 */
public class PopCanBaoQingKuang extends PopupWindow implements OnClickListener{

	private String CurrentJUZHUSHIJIAN,CurrentLINGQUFANGSHI,CurrentSHENGQINGLEIBIE;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private TextView submit,cancel;
	private List<String> list =new ArrayList<String>();
	private OnbEnsureClickListener onEnsureClickListener;
	public  interface OnbEnsureClickListener{
		void onEnsureClick(String juzhu);
	}
	private String[] str;
	public PopCanBaoQingKuang(Activity context,String[] str, OnbEnsureClickListener onEnsureClickListener) {
		super();
		this.onEnsureClickListener = onEnsureClickListener;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView=inflater.inflate(R.layout.pop_canbao, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		submit =(TextView) mMenuView.findViewById(R.id.btn_ensure);
		cancel =(TextView) mMenuView.findViewById(R.id.btn_cancle);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		LinearLayout content=(LinearLayout) mMenuView.findViewById(R.id.content);
		for (int i=0;i<str.length;i++){
			CheckBox checkBox =new CheckBox(context);
			checkBox.setText(str[i]);
			checkBox.setTextSize(17);
			checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked){
						if(!list.contains(buttonView.getText().toString().trim())){
							list.add(buttonView.getText().toString().trim());
						}
					}else{
						if(list.contains(buttonView.getText().toString().trim())){
							list.remove(buttonView.getText().toString().trim());
						}
					}
				}
			});
			content.addView(checkBox);
		}
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
	private boolean isContansIt(String str){
		return true;
	}
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}

	private String  getStrs(List<String> strs){
		if (strs.size()==0){
			return "";
		}
		String rets="";
		for (int i=0;i<strs.size();i++){
			if (i==0){
				rets=strs.get(i);
			}else{
				rets=rets+","+strs.get(i);
			}
		}
		return rets;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ensure:
            onEnsureClickListener.onEnsureClick(getStrs(list));
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
