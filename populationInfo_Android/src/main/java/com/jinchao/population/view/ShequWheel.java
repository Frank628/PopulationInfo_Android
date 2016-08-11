package com.jinchao.population.view;

import java.util.List;

import com.jinchao.population.R;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.UserBean.AccountOne;
import com.jinchao.population.utils.SharePrefUtil;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;
/**
 * 选择弹出框
 * @author FRANK
 *
 */
public class ShequWheel extends PopupWindow implements OnClickListener,OnWheelChangedListener{
	
	private WheelView mCarTypeWheel;
	private String CurrentName,CurrentId;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private TextView submit,cancel;
	private String[] sqname;
	private String[] sqid;
	private OnEnsureClickListener onEnsureClickListener;
	public interface OnEnsureClickListener{
		void OnEnSureClick(String sqid,String sqname);
	}
	public ShequWheel(Activity context,OnEnsureClickListener onEnsureClickListener) {
		super();
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
		initdata(context);
		mCarTypeWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, sqname));
		CurrentName=sqname[0];
		CurrentId=sqid[0];
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
	private void initdata(Activity context){
		@SuppressWarnings("unchecked")
		List<AccountOne> list =(List<AccountOne>) SharePrefUtil.getObj(context, Constants.Accountlist);
		if (list!=null) {
			sqid=new String[list.size()];
			sqname=new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				sqid[i]=list.get(i).sqId;
				sqname[i]=list.get(i).sqName;
			}
		}
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
				CurrentName= sqname[newValue];
				CurrentId= sqid[newValue];
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ensure:
            onEnsureClickListener.OnEnSureClick(CurrentId,CurrentName);
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
