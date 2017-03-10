package com.jinchao.population.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jinchao.population.R;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择重要人员类型
 * @author FRANK
 *
 */
public class PopImportantPeople extends PopupWindow{

	private View mMenuView;
	private ViewFlipper viewfipper;
	private ListView lv;
	private List<String> list =new ArrayList<String>();
	private OnbEnsureClickListener onEnsureClickListener;
	public  interface OnbEnsureClickListener{
		void onEnsureClick(String type);
	}
	public PopImportantPeople(Activity context, final OnbEnsureClickListener onEnsureClickListener) {
		super();
		this.onEnsureClickListener = onEnsureClickListener;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView=inflater.inflate(R.layout.pop_important, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		list.add("精神病患者");
		list.add("新疆人");
		list.add("法轮功");
		list.add("藏独份子");
		list.add("犯罪嫌疑人");
		list.add("吸毒人员");
		lv =(ListView) mMenuView.findViewById(R.id.lv);
		lv.setAdapter(new CommonAdapter<String>(context,list,R.layout.item_important) {
			@Override
			public void convert(ViewHolder helper, String item, int position) {
				helper.setText(R.id.tv_content,item);
			}
		});
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String str=(String)((ListView)parent).getItemAtPosition(position);
				onEnsureClickListener.onEnsureClick(str);
				PopImportantPeople.this.dismiss();
			}
		});
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
