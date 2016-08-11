package com.jinchao.population.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.jinchao.population.utils.BluetoothManager;

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceListBaseAdapter extends BaseAdapter implements DeviceListItemView.DeviceListItemListener {
	private ArrayList<HashMap<String, String>> mDeviceList = null;
	private DeviceListListener mListener = null;

	public DeviceListBaseAdapter(ArrayList<HashMap<String, String>> deviceList) {
		mDeviceList = deviceList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDeviceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDeviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DeviceListItemView itemView = (DeviceListItemView) convertView;
		if (itemView == null) {
			itemView = new DeviceListItemView(parent.getContext());
			itemView.setDeviceListItemListener(this);
		}
		itemView.updateUI(mDeviceList.get(position).get(BluetoothManager.DEVICE_NAME), position);
		return itemView;
	}

	@Override
	public void onConnect(int position) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onConnect(mDeviceList.get(position).get(BluetoothManager.DEVICE_ADDRESS));
		}
	}

	public void setDeviceListListener(DeviceListListener listener) {
		mListener = listener;
	}

	public interface DeviceListListener {
		public void onConnect(String mac);
	}
}
