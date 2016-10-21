package com.jinchao.population.base;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.Toast;
import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.Link;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.SysApplication;
import com.jinchao.population.utils.GlobalPref;
import com.jinchao.population.view.DeviceListDialogFragment;
import com.jinchao.population.view.FacePop;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

public class BaseReaderActiviy extends FragmentActivity{
	public ProgressDialog dialog;
	public boolean isReading = false;// NFC用
	public DeviceListDialogFragment deviceListDialogFragment;
	public byte[] imgA,imgB;
	public File photofile,tempFile;
	public PersonInfo muser;
	public FacePop facePop;
	public AlertDialog dialoge;
	public ImageOptions options=new ImageOptions.Builder()
			.setImageScaleType(ImageView.ScaleType.FIT_CENTER).build();
	public IDReader idReader;
	public Link link = null;
	public NfcAdapter nfcAdapter;
	public PendingIntent nfcPi;
	public IntentFilter[] nfcIfs;
	public String[][] techLists;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		SysApplication.getInstance().addActivity(this);
		idReader = new IDReader(this);
		idReader.setUseSpecificServer(!GlobalPref.getUseAuto());
		idReader.addSpecificServer(GlobalPref.getAddress(), GlobalPref.getPort());
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(this, "该设备不支持NFC", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		if (nfcAdapter.isEnabled() == false) {
			Toast.makeText(this, "NFC没有打开", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		nfcPi = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
		nfcIfs = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };
		techLists = new String[][] { new String[] { NfcB.class.getName() }, new String[] { IsoDep.class.getName() } };
	}
	@Override
	public void onResume() {
		super.onResume();
		nfcAdapter.enableForegroundDispatch(this, nfcPi, nfcIfs, techLists);
	}





	public void showError(String msg){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(msg);
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialoge=builder.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		nfcAdapter.disableForegroundDispatch(this);
	}



	@Override
	protected void onDestroy() {
		try {
			idReader.cancelReadCard();
		} catch (Exception e) {

		}
		try {
			link.disconnect();
		} catch (Exception e) {

		}
		super.onDestroy();
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


}
