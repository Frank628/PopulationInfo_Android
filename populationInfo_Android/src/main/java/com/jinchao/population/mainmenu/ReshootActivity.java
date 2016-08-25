package com.jinchao.population.mainmenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.caihua.cloud.common.User;
import com.caihua.cloud.common.enumerate.ConnectType;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.ActionSheetDialog;
import com.jinchao.population.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.jinchao.population.widget.ActionSheetDialog.SheetItemColor;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;

@ContentView(R.layout.activity_reshoot)
public class ReshootActivity extends BaseReaderActiviy{
	@ViewInject(R.id.edt_content)EditText edt_content;
	@ViewInject(R.id.iv_photo)ImageView iv_photo;
	public static final String TAG="IDCARD_DEVICE";

	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private String pic="";
	private DialogLoading dialogLoading; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("照片补拍");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		idReader = new IDReader(this, mHandler);
		dialogLoading = new DialogLoading(ReshootActivity.this, "读卡中...", true);
		navigationLayout.setRightText("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				String cardno=edt_content.getText().toString().trim();
				if (cardno.equals("")) {
					Toast.makeText(ReshootActivity.this, "请先录入身份证号码!", Toast.LENGTH_SHORT).show();
					return;
				}
				if (CommonIdcard.validateCard(cardno)) {
					if (cardno.length() == 15) {
						cardno = CommonIdcard.conver15CardTo18(cardno);
						edt_content.setText(cardno);
                        Toast.makeText(ReshootActivity.this, "15位转18位证件号成功", Toast.LENGTH_SHORT).show();
                    } else if (cardno.length() == 17) {
                    	cardno = CommonIdcard.conver17CardTo18(cardno);
                    	edt_content.setText(cardno);
                        Toast.makeText(ReshootActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
                    }
				}else{
					Toast.makeText(ReshootActivity.this, "请输入合法的身份证！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (pic.equals("")) {
					Toast.makeText(ReshootActivity.this, "请拍照后再保存！", Toast.LENGTH_SHORT).show();
					return;
				}
				SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
				String date =sDateFormat.format(new java.util.Date());
				DbUtils dbUtils =DeviceUtils.getDbUtils(ReshootActivity.this);
					try {
						People people = dbUtils.findFirst(Selector.from(People.class).where("cardno", "=", cardno));
						if (people!=null) {
							people.setPicture(pic);
							people.setCard_type("1");
							people.setCollect_datetime(date);
							dbUtils.update(people);
						}else{
							people=new People(date, cardno, pic, "补", CommonUtils.GenerateGUID(), "5", MyInfomationManager.getUserName(ReshootActivity.this));
							dbUtils.save(people);	
						}
						Toast.makeText(ReshootActivity.this, "保存成功~", Toast.LENGTH_SHORT).show();
						ReshootActivity.this.finish();
					} catch (Exception e) {
						Toast.makeText(ReshootActivity.this, "保存失败~", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
			}
		});

	}
	public void onNewIntent(Intent intent){
		Parcelable parcelable = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (parcelable == null) {
			return;
		}
		// 正在处理上一次读取的数据，不再读取数据
		if (isReading) {
			return;
		}
		isReading = true;
		showProcessDialog("正在读卡中，请稍后");
		idReader.connect(ConnectType.NFC, parcelable);
	}

	 private void showIDCardInfo(boolean isClear,User user) {//显示身份证数据到界面
		 hideProcessDialog();
		 if (isClear){
			 return;
		 }
		 if (user==null){
			 showError();
		 }else{
			 edt_content.setText(user.id.trim());
		 }
		 isReading = false;
	 }
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case IDReader.CONNECT_SUCCESS:
					break;
				case IDReader.CONNECT_FAILED:
					hideProcessDialog();
					if (idReader.strErrorMsg != null) {
//                        Toast.makeText(getActivity(),"连接失败：" + idReader.strErrorMsg,Toast.LENGTH_SHORT).show();
						String str="未响应，请将身份证紧贴手机背部重试!";
						if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
							str="未响应，请将身份证紧贴读卡器重试!";
						}else if(idReader.getConnectType() == ConnectType.NFC){
							str="未响应，请将身份证紧贴手机背部重试!";
						}else{
							str="读卡失败，"+idReader.strErrorMsg;
						}
						AlertDialog.Builder builder = new AlertDialog.Builder(ReshootActivity.this);
						builder.setMessage(str);
						builder.setTitle("提示");
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.create().show();
					}
					isReading = false;
					if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
						SharePrefUtil.saveString(ReshootActivity.this,"mac",null);
					}
					break;
				case IDReader.READ_CARD_SUCCESS:
//                    BeepManager.playsuccess(getActivity());
					showIDCardInfo(false, (User) msg.obj);
					break;

				case IDReader.READ_CARD_FAILED:
//                    BeepManager.playfail(getActivity());
					showIDCardInfo(false, null);
					break;
				default:
					break;
			}
		}
	};
	@Event(value={R.id.btn_readcard})
	private void readcardClick(View view){
		String net = SharePrefUtil.getString(this, Constants.DEVICE_WAY,"自动");
		switch (net) {
			case "自动":
				int a = HasOTGDeviceConnected();
				if (a == 0) {
					showProcessDialog("正在读卡中，请稍后");
					idReader.connect(ConnectType.OTG);
				} else if (a == 1) {
					showProcessDialog("正在读卡中，请稍后");
					idReader.connect(ConnectType.OTGAccessory);
				} else {
					String mac=SharePrefUtil.getString(ReshootActivity.this,"mac",null);
					if (mac == null) {
						deviceListDialogFragment.show(getSupportFragmentManager(), "");
					} else {
						showProcessDialog("正在读卡中，请稍后");
						int delayMillis = SharePrefUtil.getInt(ReshootActivity.this, Constants.BluetoothSetting_long_time,15);
						idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
					}
				}
				break;
			case "蓝牙":
				String mac=SharePrefUtil.getString(ReshootActivity.this,"mac",null);
				if (mac == null) {
					deviceListDialogFragment.show(getSupportFragmentManager(), "");
				} else {
					showProcessDialog("正在读卡中，请稍后");
					int delayMillis = SharePrefUtil.getInt(ReshootActivity.this, Constants.BluetoothSetting_long_time,15);
					idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
				}
				break;
			case "OTG":
				int a2 = HasOTGDeviceConnected();
				if (a2 == 0) {
					showProcessDialog("正在读卡中，请稍后");
					idReader.connect(ConnectType.OTG);
				} else if (a2 == 1) {
					showProcessDialog("正在读卡中，请稍后");
					idReader.connect(ConnectType.OTGAccessory);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(ReshootActivity.this, AlertDialog.THEME_HOLO_LIGHT);
					builder.setMessage("找不到OTG设备");
					builder.setPositiveButton("确定", null);
					builder.show();
				}
				break;

			case "NFC":
				AlertDialog.Builder builder = new AlertDialog.Builder(ReshootActivity.this, AlertDialog.THEME_HOLO_LIGHT);
				builder.setMessage("当前是NFC模式，请将身份证贴在手机背面");
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode ==RESULT_OK) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (DeviceUtils.hasSDCard()) {
					File tempFile = new File(Environment.getExternalStorageDirectory()+"/"+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));		
				}else{
					Toast.makeText(ReshootActivity.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			String url=DeviceUtils.getPath(ReshootActivity.this,uri);
			intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		}else{
			intent.setDataAndType(uri, "image/*");
		}
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 5);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}
	/**
	 * 保存裁剪之后的图片数据
		 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			pic = new String(Base64Coder.encodeLines(b));
			iv_photo.setImageDrawable(drawable);
		}
	}
	@Event(value={R.id.iv_photo})
	private void takePhotoClick(View view){
		new ActionSheetDialog(ReshootActivity.this)
		.builder()
		.setCancelable(true)
		.setCanceledOnTouchOutside(true)
		.addSheetItem("用相机拍照", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Intent intentFromCapture = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 判断存储卡是否可以用，可用进行存储
						if (DeviceUtils.hasSDCard()) {
							intentFromCapture.putExtra(
									MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(Environment
											.getExternalStorageDirectory(),
											IMAGE_FILE_NAME)));
						}
						Log.i("camera", "IMAGE_FILE_NAME");
						startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
					}
				})
		.addSheetItem("去相册选择", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Intent intentFromGallery = new Intent();
						intentFromGallery.setType("image/*"); // 设置文件类型
						intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
					}
			}).show();
	}
}
