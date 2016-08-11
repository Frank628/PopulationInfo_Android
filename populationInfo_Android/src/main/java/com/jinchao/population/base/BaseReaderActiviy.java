package com.jinchao.population.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.caihua.cloud.common.User;
import com.caihua.cloud.common.entity.Server;
import com.caihua.cloud.common.enumerate.ConnectType;
import com.caihua.cloud.common.enumerate.NetType;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.SysApplication;
import com.jinchao.population.config.Constants;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.DeviceListDialogFragment;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.FacePop;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.Locale;

public class BaseReaderActiviy extends FragmentActivity implements DeviceListDialogFragment.DeviceListDialogFragmentListener{
	private NfcAdapter mAdapter;
	public PendingIntent mPendingIntent;
	public NdefMessage mNdefPushMessage;
	public USBBroadcastReceiver receiveBroadCast;
	public ProgressDialog dialog;
	public IDReader idReader;
	public boolean isReading = false;// NFC用
	public DeviceListDialogFragment deviceListDialogFragment;
	public byte[] imgA,imgB;
	public File photofile,tempFile;
	public User muser;
	public FacePop facePop;
	public AlertDialog dialoge;
	public ImageOptions options=new ImageOptions.Builder()
			.setImageScaleType(ImageView.ScaleType.FIT_CENTER).build();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		SysApplication.getInstance().addActivity(this);

		deviceListDialogFragment = DeviceListDialogFragment.newInstance();
		deviceListDialogFragment.setDeviceListDialogFragmentListener(this);
		receiveBroadCast = new USBBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		registerReceiver(receiveBroadCast, filter);
		initNFC();
	}
	@Override
	public void onResume() {
		super.onResume();

		if (mAdapter != null) {
			// 显示activity的时候开始nfc的监控
			if (!mAdapter.isEnabled()) {
				// showNfcSettingsDialog();
			} else {
				mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
				mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
			}
		}
		String net="自动";
		switch (net) {
			case "自动":
				idReader.SetNetType(NetType.publicNetwork);
				break;
			case "移动":
				idReader.SetNetType(NetType.privateNetwork, new Server("221.181.64.41", 2005));
				break;
			case "电信":
				idReader.SetNetType(NetType.privateNetwork, new Server("61.155.106.65", 2005));
				break;
			case "联通":
				idReader.SetNetType(NetType.privateNetwork, new Server("61.51.110.49", 2005));
				break;
			case "手动":
				idReader.SetNetType(NetType.privateNetwork, new Server(PreferenceManager.getDefaultSharedPreferences(this).getString("ManualIP",""),2005));
				break;
			default:
				break;
		}
		UsbManager mUsbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
		if (!mUsbManager.getDeviceList().isEmpty()) {
//            headPanel.setRightTitle(R.drawable.usb);
		}
	}
	private void initNFC(){
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
			builder.setMessage("手机不支持NFC,部分功能无法使用");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.create().show();
		}
		mPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		mNdefPushMessage = new NdefMessage(
				new NdefRecord[] {CommonUtils.newTextRecord("Message from NFC Reader :-)", Locale.ENGLISH, true) });
	}
	public class USBBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
			} else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
			}
		}
	}

	public void showError(){
		if (idReader.strErrorMsg != null) {
			String str = "未响应，请将身份证紧贴手机背部重试!";
			if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
				str = "未响应，" + idReader.strErrorMsg;
			} else if (idReader.getConnectType() == ConnectType.NFC) {
				str = "未响应，" + idReader.strErrorMsg;
			} else {
				str = "未响应，" + idReader.strErrorMsg;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(str);
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialoge=builder.show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mAdapter != null) {
			// activity暂停的时候，暂停nfc的监控
			mAdapter.disableForegroundDispatch(this);
			mAdapter.disableForegroundNdefPush(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiveBroadCast != null) {
			unregisterReceiver(receiveBroadCast);
			receiveBroadCast = null;
		}
	}
	protected void showProcessDialog(String msg) {
		if (dialoge != null && dialoge.isShowing()) {
			dialoge.dismiss();
			dialoge = null;
		}
		dialog = new ProgressDialog(this);
		dialog.setMessage(msg);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
		dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
		dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
		dialog.show();
	}
	protected void hideProcessDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
	protected int HasOTGDeviceConnected() {
		// TODO Auto-generated method stub
		UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		if (!mUsbManager.getDeviceList().isEmpty()) {
			return 0;
		} else if (mUsbManager.getAccessoryList() != null) {
			return 1;
		}
		return -1;
	}
	@Override
	public void onConnect(String mac) {

		if (SharePrefUtil.getBoolean(this, Constants.REMEMBER_BLUETOOTH,false)) {
			SharePrefUtil.saveString(this, "mac", mac);
		}
		if (mac != null) {
			showProcessDialog("正在读卡中，请稍后");
			int delayMillis = SharePrefUtil.getInt(this,Constants.BluetoothSetting_long_time,15);
			idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
		}
	}
}
