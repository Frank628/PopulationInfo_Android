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
import com.jinchao.population.widget.wheel.OnWheelChangedListener;
import com.jinchao.population.widget.wheel.WheelView;
import com.jinchao.population.widget.wheel.adapter.ArrayWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class RealhouseUser extends PopupWindow implements OnWheelChangedListener,OnClickListener{
	private OnEnsureClickListener onEnsureClickListener;
	private View mPopView;
	private ViewFlipper viewfipper;
	private WheelView zhuView,renView;
	private TextView btn_ensure,btn_cancle;
	private Activity context;
	private String[] Zhuzhai,renshu;
	private String mCurrentzhuzhai,mCurrentrenshu;
	public interface OnEnsureClickListener{
		void OnEnSureClick(String zhuzhai,String renshu);
	}

	public RealhouseUser(Activity context,OnEnsureClickListener onEnsureClickListener,String zhuzhail,String renshul) {
		this.onEnsureClickListener = onEnsureClickListener;
		this.context=context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopView=inflater.inflate(R.layout.wheel_two, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		zhuView=(WheelView) mPopView.findViewById(R.id.wv_1);
		renView=(WheelView) mPopView.findViewById(R.id.wv_2);
		btn_ensure=(TextView) mPopView.findViewById(R.id.btn_ensure);
		btn_cancle=(TextView) mPopView.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);
		btn_ensure.setOnClickListener(this);
		renshu=new String[501];
		for (int i = 0; i < 501; i++) {
			if (i==0) {
				renshu[i]="无人居住";
			}else{
				renshu[i]=i+"人"	;
			}
		}
		zhuView.setViewAdapter(new ArrayWheelAdapter<String>(context, Constants.ZHUZHAILEIXING));
		renView.setViewAdapter(new ArrayWheelAdapter<String>(context, renshu));
		zhuView.addChangingListener(this);
		renView.addChangingListener(this);
		zhuView.setVisibleItems(7);
		renView.setVisibleItems(7);
		viewfipper.addView(mPopView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		int renshuindex=0;
		int zhuzhaiindex=0;
		for (int i = 0; i < Constants.ZHUZHAILEIXING.length; i++) {
			if (zhuzhail.equals(Constants.ZHUZHAILEIXING[i])) {
				zhuzhaiindex=i;
			}
		}
		for (int i = 0; i <renshu.length; i++) {
			if (renshul.equals(renshu[i])) {
				renshuindex=i;
			}
		}
		mCurrentrenshu=renshul;
		mCurrentzhuzhai=zhuzhail;
		this.update();
		zhuView.setCurrentItem(zhuzhaiindex);
		renView.setCurrentItem(renshuindex);
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel==zhuView) {
			mCurrentzhuzhai=Constants.ZHUZHAILEIXING[newValue];
		}else if (wheel==renView) {
			mCurrentrenshu=renshu[newValue];
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
			onEnsureClickListener.OnEnSureClick(mCurrentzhuzhai,mCurrentrenshu);
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
