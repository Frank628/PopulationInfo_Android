package com.jinchao.population.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinchao.population.R;


public class DeviceListItemView extends FrameLayout {

	TextView snTV;
	Button connectBTN;
	int mPosition;
	DeviceListItemListener mListener;
	
	public DeviceListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		initUI();
	}
	public DeviceListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initUI();
	}
	public DeviceListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initUI();
	}
	public DeviceListItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initUI();
	}
	
	private void initUI(){
		mPosition=0;
		mListener=null;
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.device_list_item_layout, this);
	    snTV=(TextView)findViewById(R.id.snTV);
	    connectBTN=(Button)findViewById(R.id.connectBTN);
	    connectBTN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mListener!=null)
					mListener.onConnect(mPosition);
			}
		});
	}
	public void setDeviceListItemListener(DeviceListItemListener listener){
       		mListener=listener;
	}
	public void updateUI(String sn, int position){
		snTV.setText(sn);
		mPosition=position;
	}
	
	public interface DeviceListItemListener{
		public void onConnect(int position);
	}
	
}
