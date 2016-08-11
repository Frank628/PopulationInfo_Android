package com.jinchao.population.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
	public static final int REQUEST_ENABLE_BT = 1;
	public static final String DEVICE_NAME = "DEVICE_NAME";
	public static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";
	public static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	BluetoothAdapter mBluetoothAdapter = null;
	Activity activity = null;
	BluetoothReceiver btReceiver = null;
	BluetoothStatusChangedListener btStatusChangedListener = null;
	BluetoothDeviceDiscoveredListener btDeviceDiscoveredListener = null;
	BluetoothSocket socket = null;
	InputStream inputStream = null;
	OutputStream outputStream = null;

	public BluetoothManager(Activity activity) {
		this.activity = activity;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		btReceiver = new BluetoothReceiver();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		activity.registerReceiver(btReceiver, intentFilter);
	}

	public void setBluetoothStatusChangedListener(BluetoothStatusChangedListener listener) {
		btStatusChangedListener = listener;
	}

	public void setBluetoothDeviceDiscoveredListener(BluetoothDeviceDiscoveredListener listener) {
		btDeviceDiscoveredListener = listener;
	}

	public boolean connect(String deviceMac) {
		if (socket != null) {
			return false;
		}
		if (mBluetoothAdapter.isEnabled()) {
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceMac);
			try {
				socket = device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
			} catch (IOException e) {
				e.printStackTrace();
			}
			cancelDiscovery();
			try {
				socket.connect();
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				return true;
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	public void write(byte[] bytes) {
		if (outputStream != null) {
			try {
				outputStream.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean disconnect() {
		try {
			socket.close();
			socket = null;
			inputStream = null;
			outputStream = null;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isEnabled() {
		if (mBluetoothAdapter != null)
			return mBluetoothAdapter.isEnabled();
		else
			return false;
	}

	public boolean openBluetooth() {
		if (isEnabled() == false) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		return true;
	}

	public BluetoothDevice getRemoteDevice(String bluetoothAddress) {
		return mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
	}

	public boolean closeBluetooth() {
		cancelDiscovery();
		if (isEnabled()) {
			mBluetoothAdapter.disable();
		}

		return true;
	}

	public boolean cancelDiscovery() {
		if (isEnabled()) {
			return mBluetoothAdapter.cancelDiscovery();
		} else
			return false;
	}

	public boolean startDiscovery() {
		if (isEnabled()) {
			return mBluetoothAdapter.startDiscovery();
		} else
			return false;
	}

	public boolean destroy() {
		cancelDiscovery();
		try {
			activity.unregisterReceiver(btReceiver);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	public ArrayList<HashMap<String, String>> getBondedDevices() {
		if (isEnabled()) {
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			if (pairedDevices.size() > 0) {
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				for (BluetoothDevice device : pairedDevices) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(DEVICE_NAME, device.getName());
					map.put(DEVICE_ADDRESS, device.getAddress());
					list.add(map);
				}
				return list;
			}
		}
		return null;
	}

	public interface BluetoothStatusChangedListener {
		void bluetoothStatusChanged(int status);
	}

	public interface BluetoothDeviceDiscoveredListener {
		void bluetoothDeviceDiscovered(HashMap<String, String> map);
	}

	private class BluetoothReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
			case BluetoothAdapter.ACTION_STATE_CHANGED: {
				int btStatus = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
				switch (btStatus) {
				case BluetoothAdapter.STATE_TURNING_ON: {
					break;
				}
				case BluetoothAdapter.STATE_ON: {
					break;
				}
				case BluetoothAdapter.STATE_TURNING_OFF: {
					break;
				}
				case BluetoothAdapter.STATE_OFF: {
					break;
				}
				default: {
					break;
				}
				}
				if (btStatusChangedListener != null)
					btStatusChangedListener.bluetoothStatusChanged(btStatus);
				break;
			}
			case BluetoothDevice.ACTION_FOUND: {
				if (btDeviceDiscoveredListener != null) {
					BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(DEVICE_ADDRESS, btDevice.getAddress());
					map.put(DEVICE_NAME, btDevice.getName());
					btDeviceDiscoveredListener.bluetoothDeviceDiscovered(map);
				}
				break;
			}
			}
		}
	}
}
