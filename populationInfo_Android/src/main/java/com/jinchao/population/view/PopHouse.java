package com.jinchao.population.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinchao.population.R;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.dbentity.HouseJLX;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.entity.HouseAddressOldBean;
import com.jinchao.population.entity.UserBean;
import com.jinchao.population.entity.UserBean.AccountOne;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.widget.wheel.OnWheelChangedListener;
import com.jinchao.population.widget.wheel.WheelView;
import com.jinchao.population.widget.wheel.adapter.ArrayWheelAdapter;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class PopHouse extends PopupWindow implements OnWheelChangedListener,OnClickListener{
	public OnHouseEnsureClickListener onEnsureClickListener;
	private View mPopView;
	private ViewFlipper viewfipper;
	private WheelView PCSView,USERView;
	private TextView btn_ensure,btn_cancle;
	private AccountOne accountOne;
	private Activity context1;
	private String[] PSCName,USERName,UserId;
	private Map<String, String[]> UserNameMap=new HashMap<String, String[]>();
	private Map<String, String[]> UserIdMap=new HashMap<String, String[]>();
	private String mCurrentPcs,mCurrentUserName,mCurrentUserId;
	private ListView lv;
	private EditText edt_content;
	private DbUtils dbUtils;
	private CommonAdapter<HouseAddressOldBean> adapter;
	public interface OnHouseEnsureClickListener{
		void OnHouseEnSureClick(String bianhao,String dizhi);
	}

	public PopHouse(Activity context,OnHouseEnsureClickListener onEnsureClickListener1) {
		this.onEnsureClickListener = onEnsureClickListener1;
		this.context1=context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopView=inflater.inflate(R.layout.pop_house, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		PCSView=(WheelView) mPopView.findViewById(R.id.wv_1);
		USERView=(WheelView) mPopView.findViewById(R.id.wv_2);
		btn_ensure=(TextView) mPopView.findViewById(R.id.btn_ensure);
		btn_cancle=(TextView) mPopView.findViewById(R.id.btn_cancle);
		lv=(ListView) mPopView.findViewById(R.id.lv);
		edt_content=(EditText) mPopView.findViewById(R.id.edt_content);
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
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		dbUtils=DeviceUtils.getDbUtils(context);
		new Thread(new Runnable() {
			@Override
			public void run() {
				initData();
			}
		}).start();
		edt_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				lv.setAdapter(null);
				String content =edt_content.getText().toString().trim();
				List<HouseAddressOldBean> list=new ArrayList<HouseAddressOldBean>();
				if (content.length()>=1) {
					try {
						list=dbUtils.findAll(Selector.from(HouseAddressOldBean.class).where("address", "like", "%"+content+"%"));
						if (list==null) {
							Toast.makeText(context1, "未下载地址库,请先下载全库地址~", 0).show();
							return;
						}
						if (list.size()==0) {
							list=dbUtils.findAll(Selector.from(HouseAddressOldBean.class).where("scode", "like", "%"+content+"%"));
							if (list.size()==0) {
								
							}
						}
							adapter = new CommonAdapter<HouseAddressOldBean>(context1,list,R.layout.item_text) {
								@Override
								public void convert(ViewHolder helper,HouseAddressOldBean item, int position) {
									helper.setText(R.id.tv_content, item.address);
									
								}
							};
							lv.setAdapter(adapter);	
						
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				HouseAddressOldBean houseAddress =(HouseAddressOldBean) ((ListView)parent).getItemAtPosition(position);
				onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
				PopHouse.this.dismiss();
			}
		});
		try {
			List<HouseAddress> list=dbUtils.findAll(HouseAddress.class);
			if (list==null) {
				Dialog.showSelectDialog(context1, "未下载地址库,请先下载全库地址~", new DialogClickListener() {
					@Override
					public void confirm() {
						
					}
					
					@Override
					public void cancel() {
						
					}
				});
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	private void initData(){
				try {
					List<JLX> jlxList=dbUtils.findAll(Selector.from(JLX.class));
					PSCName=new String[jlxList.size()];
					for (int i = 0; i < jlxList.size(); i++) {
						PSCName[i]=jlxList.get(i).jlx_name;
						List<HouseJLX> housejlxList=dbUtils.findAll(Selector.from(HouseJLX.class).where("fk_p","=",jlxList.get(i).id));
						if (housejlxList.size()!=0) {
							USERName=new String[housejlxList.size()];
							UserId=new String[housejlxList.size()];
							for (int j = 0; j <housejlxList.size(); j++) {
								USERName[j]=housejlxList.get(j).hosejlx_name;
								UserId[j]=housejlxList.get(j).id;
							}
							UserNameMap.put(jlxList.get(i).jlx_name, USERName);
							UserIdMap.put(jlxList.get(i).jlx_name, UserId);
						}else{
							USERName=new String[]{""};
							UserId=new String[]{""};
							UserNameMap.put(jlxList.get(i).jlx_name, USERName);
							UserIdMap.put(jlxList.get(i).jlx_name, UserId);
						}
					}
					PCSView.setViewAdapter(new ArrayWheelAdapter<String>(context1, PSCName));
					PCSView.addChangingListener(PopHouse.this);
					USERView.addChangingListener(PopHouse.this);
					PCSView.setVisibleItems(7);
					USERView.setVisibleItems(7);
					updateUser();
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		USERView.setViewAdapter(new ArrayWheelAdapter<String>(context1, users));
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
			onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName);
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
