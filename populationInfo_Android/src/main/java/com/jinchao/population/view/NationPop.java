package com.jinchao.population.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.jinchao.population.R;
import com.jinchao.population.dbentity.Area;
import com.jinchao.population.dbentity.City;
import com.jinchao.population.dbentity.Province;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.widget.wheel.OnWheelChangedListener;
import com.jinchao.population.widget.wheel.WheelView;
import com.jinchao.population.widget.wheel.adapter.ArrayWheelAdapter;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
/**
 * 省市区选择弹出框
 * @author FRANK
 */
public class NationPop extends PopupWindow implements OnClickListener,OnWheelChangedListener{
	
	private WheelView mProvince,mCity,mArea;
	private String CurrentProvince,CurrentCity,CurrentArea,CurrentID;
	private Map<String, String[]> CityDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> AreaDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> IDDatasMap = new HashMap<String, String[]>();
	private String[] ProvinceDatesArr,CityDatasArr,AreaDatasArr,IDDatasArr;
	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
    private LinearLayout loading;
	private TextView submit,cancel;
	private DbUtils dbUtils;
	private OnEnsureClickListener onEnsureClickListener;
	public interface OnEnsureClickListener{
		void OnEnSureClick(String nationid,String huji,String wushi_huji);
	}
	public NationPop(Activity context,OnEnsureClickListener onEnsureClickListener) {
		super();
		this.mContext = context;
		this.onEnsureClickListener = onEnsureClickListener;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView=inflater.inflate(R.layout.wheel_three, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		submit =(TextView) mMenuView.findViewById(R.id.btn_ensure);
		cancel =(TextView) mMenuView.findViewById(R.id.btn_cancle);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		mProvince=(WheelView) mMenuView.findViewById(R.id.wv_1);
		mCity=(WheelView) mMenuView.findViewById(R.id.wv_2);
		mArea=(WheelView) mMenuView.findViewById(R.id.wv_3);
        loading=(LinearLayout) mMenuView.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
		mProvince.addChangingListener(this);
		mCity.addChangingListener(this);
		mArea.addChangingListener(this);
		mProvince.setVisibleItems(7);
		mCity.setVisibleItems(7);
		mArea.setVisibleItems(7);
//		updateArea(context);
		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
		new Thread(new Runnable() {
			@Override
			public void run() {
				initData(mContext);
				mContext.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
                        loading.setVisibility(View.GONE);
						mProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, ProvinceDatesArr));
						updateCity(mContext);

					}
				});
			}
		}).start();
		
	}

	@Override
	public void showAtLocation(View parent, int gravity,int x,int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}
	
	private void updateCity(Activity ct){
		int pCurrent=mProvince.getCurrentItem();
		CurrentProvince=ProvinceDatesArr[pCurrent];
		String[] cities=CityDatasMap.get(CurrentProvince);
		mCity.setViewAdapter(new ArrayWheelAdapter<String>(ct, cities));
		if(cities[0].equals("")){
			CurrentCity="";
			String[] areas=AreaDatasMap.get(CurrentProvince);
			String[] IDs=IDDatasMap.get(CurrentProvince);
			if(areas==null){
				areas=new String[]{""};
			}
			if(IDs==null){
				IDs=new String[]{""};
			}
			mArea.setViewAdapter(new ArrayWheelAdapter<String>(ct, areas));
			mArea.setCurrentItem(0);
            if (areas[0].equals("")){
                CurrentArea=areas[0];
            }else{
                CurrentArea=areas[0];
                CurrentID=IDs[0];
            }
		}else{
			
			mCity.setCurrentItem(0);
			updateArea(ct);	
		}
		
		
	}
	private void updateArea(Activity ct){
		int cCurrent=mCity.getCurrentItem();
		CurrentCity =CityDatasMap.get(CurrentProvince)[cCurrent];
		String[] areas=AreaDatasMap.get(CurrentCity);
		String[] IDs=IDDatasMap.get(CurrentCity);
		if(areas==null){
			areas=new String[]{""};
		}
		if(IDs==null){
			IDs=new String[]{""};
		}
		mArea.setViewAdapter(new ArrayWheelAdapter<String>(ct, areas));
		mArea.setCurrentItem(0);
        if (areas[0].equals("")){
            CurrentArea=areas[0];
        }else{
            CurrentArea=areas[0];
            CurrentID=IDs[0];
        }
	}
	/**
	 * 省市区数据初始化
	 * @param ct
	 */
	private void initData(Activity ct){
		try {
			dbUtils = DeviceUtils.getDbUtils(ct);
			List<Province>  provincelist=dbUtils.findAll(Selector.from(Province.class));
			ProvinceDatesArr = new String[provincelist.size()];
			for (int i = 0; i < provincelist.size(); i++) {
				ProvinceDatesArr[i]=provincelist.get(i).getProvince_name();
				List<City> citieslist= dbUtils.findAll(Selector.from(City.class).where("fk_p", "=", provincelist.get(i).getId()));
				if(citieslist.size()!=0){
					CityDatasArr = new String[citieslist.size()];
					for (int j = 0; j < citieslist.size(); j++) {
						CityDatasArr[j]=citieslist.get(j).getCity_name();
						List<Area> areaslist= dbUtils.findAll(Selector.from(Area.class).where("fk_c", "=", citieslist.get(j).getId()));
						AreaDatasArr=new String[areaslist.size()];
						IDDatasArr = new String[areaslist.size()];
						for (int k = 0; k < areaslist.size(); k++) {
							AreaDatasArr[k]=areaslist.get(k).getArea_name();
							IDDatasArr[k]=areaslist.get(k).getId()+"";
						}
                        if(areaslist.size()!=0){
                            AreaDatasMap.put(citieslist.get(j).getCity_name(), AreaDatasArr);
                            IDDatasMap.put(citieslist.get(j).getCity_name(), IDDatasArr);
                        }
					}
					CityDatasMap.put(provincelist.get(i).getProvince_name(), CityDatasArr);
				}else{
					CityDatasArr=new String[]{""};
					List<Area> areaslist= dbUtils.findAll(Selector.from(Area.class).where("fk_c", "=", provincelist.get(i).getId()));
					AreaDatasArr=new String[areaslist.size()];
					IDDatasArr=new String[areaslist.size()];
					for (int j = 0; j < areaslist.size(); j++) {
						AreaDatasArr[j]=areaslist.get(j).getArea_name();
						IDDatasArr[j]=areaslist.get(j).getId()+"";
					}
                    if (areaslist.size()!=0){
                        AreaDatasMap.put(provincelist.get(i).getProvince_name(), AreaDatasArr);
                        IDDatasMap.put(provincelist.get(i).getProvince_name(),IDDatasArr);
                    }
                    CityDatasMap.put(provincelist.get(i).getProvince_name(), CityDatasArr);

				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel ==mProvince){
            CurrentID=ProvinceDatesArr[newValue];
			updateCity(mContext);
		} else if (wheel == mCity){
			updateArea(mContext);
		} else if (wheel == mArea){
			if(CurrentCity.equals("")){
				CurrentArea=AreaDatasMap.get(CurrentProvince)[newValue];
				CurrentID=IDDatasMap.get(CurrentProvince)[newValue];
			}else if(CurrentArea.equals("")){
                CurrentID=ProvinceDatesArr[newValue];
            }else{
                if(AreaDatasMap!=null) {
                    if (AreaDatasMap.get(CurrentCity)!=null) {
                        CurrentArea = AreaDatasMap.get(CurrentCity)[newValue];
                        CurrentID = IDDatasMap.get(CurrentCity)[newValue];
                    }
                }
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ensure:
			Message msg =new Message();
//           data.putString("name", CurrentProvince+CurrentCity+CurrentArea);
            onEnsureClickListener.OnEnSureClick(CurrentID,CurrentProvince+CurrentCity+CurrentArea,CurrentProvince+CurrentArea);
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
