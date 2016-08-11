package com.jinchao.population.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.jinchao.population.R;
import com.jinchao.population.adapter.DeviceListBaseAdapter;
import com.jinchao.population.utils.BluetoothManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceListDialogFragment extends DialogFragment implements BluetoothManager.BluetoothDeviceDiscoveredListener,
		DeviceListBaseAdapter.DeviceListListener {

	private ListView deviceListLV = null;
	private Button refreshBTN = null;
	private ArrayList<HashMap<String, String>> deviceList = new ArrayList<HashMap<String, String>>();
	private DeviceListBaseAdapter adapter = null;
	private BluetoothManager mBluetoothManager = null;
	private Timer timer;
	private DeviceListDialogFragmentListener mListener = null;



	public static DeviceListDialogFragment newInstance() {
		DeviceListDialogFragment fragment = new DeviceListDialogFragment();
		fragment.setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		return fragment;
	}

	public void setDeviceListDialogFragmentListener(DeviceListDialogFragmentListener listener) {
		mListener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.device_list_dialog_frag_layout, container, false);
		deviceListLV = (ListView) view.findViewById(R.id.devicelistLV);
		deviceListLV.setAdapter(adapter);
		deviceListLV.setEmptyView(view.findViewById(R.id.devicelistEmptyTV));
		refreshBTN = (Button) view.findViewById(R.id.refreshBTN);
		refreshBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deviceList.clear();
				adapter.notifyDataSetChanged();
				mBluetoothManager.startDiscovery();
			}
		});
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void bluetoothDeviceDiscovered(HashMap<String, String> map) {
		for(HashMap<String,String> item:deviceList){
			if(item.get(BluetoothManager.DEVICE_ADDRESS).equals(map.get(BluetoothManager.DEVICE_ADDRESS))){
				return;
			}
		}
		deviceList.add(map);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		deviceList.clear();
		adapter = new DeviceListBaseAdapter(deviceList);
		adapter.setDeviceListListener(this);
		mBluetoothManager = new BluetoothManager(getActivity());
		mBluetoothManager.setBluetoothDeviceDiscoveredListener(this);
		mBluetoothManager.openBluetooth();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (mBluetoothManager != null) {
					try {
						mBluetoothManager.startDiscovery();
					} catch (Exception e) {
					}
				}
			}
		}, 0, 10000);
	}

	@Override
	public void onDetach() {
		timer.cancel();
		mBluetoothManager.destroy();
		super.onDetach();
	}

	@Override
	public void onConnect(String mac) {
		dismiss();
		if (mListener != null) {
			mListener.onConnect(mac);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		getDialog().getWindow().setLayout((int) (width * 0.9), (int) (height * 0.9));
	}

	public interface DeviceListDialogFragmentListener {
		public void onConnect(String mac);
	}
}
