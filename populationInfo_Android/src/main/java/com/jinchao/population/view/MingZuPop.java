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
 * 名族选择弹出框
 * @author FRANK
 *
 */
public class MingZuPop extends PopupWindow implements OnClickListener,OnWheelChangedListener{
	
	private WheelView mCarTypeWheel;
	private String CurrentName;
	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private TextView submit,cancel;
	private OnEnsureClickListener onEnsureClickListener;
    public interface OnEnsureClickListener{
        void OnEnSureClick(String nationid);
    }
	public static  String[] mingzu=new String[]{"汉族","蒙古族","回族","藏族","维吾尔族","苗族","彝族","壮族","布依族","朝鲜族","满族","侗族","瑶族","白族","土家族",  
        "哈尼族","哈萨克族","傣族","黎族","傈僳族","佤族","畲族","高山族","拉祜族","水族","东乡族","纳西族","景颇族","柯尔克孜族",  
        "土族","达斡尔族","仫佬族","羌族","布朗族","撒拉族","毛南族","仡佬族","锡伯族","阿昌族","普米族","塔吉克族","怒族", "乌孜别克族",  
       "俄罗斯族","鄂温克族","德昂族","保安族","裕固族","京族","塔塔尔族","独龙族","鄂伦春族","赫哲族","门巴族","珞巴族","基诺族","穿身人族"};
	
	public MingZuPop(Activity context,OnEnsureClickListener onEnsureClickListener) {
		super();
		this.mContext = context;
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
		
		mCarTypeWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, mingzu));
		CurrentName=mingzu[0];
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
				CurrentName= mingzu[newValue];
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ensure:
			String str="";
			if(CurrentName.substring(CurrentName.length()-1, CurrentName.length()).equals("族")){
				str=CurrentName.substring(0,CurrentName.length()-1);
			}
            onEnsureClickListener.OnEnSureClick(str);
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
