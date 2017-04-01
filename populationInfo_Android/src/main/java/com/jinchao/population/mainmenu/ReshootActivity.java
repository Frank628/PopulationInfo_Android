package com.jinchao.population.mainmenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import android.nfc.Tag;
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


import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.LinkFactory;
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
import com.soundcloud.android.crop.Crop;

@ContentView(R.layout.activity_reshoot)
public class ReshootActivity extends BaseReaderActiviy implements IDReader.IDReaderListener{
	@ViewInject(R.id.edt_content)EditText edt_content;
	@ViewInject(R.id.edt_name)EditText edt_name;
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
		idReader.setListener(this);
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
				if (edt_name.getText().toString().trim().equals("")){
					Toast.makeText(ReshootActivity.this, "请输入补拍者姓名！", Toast.LENGTH_SHORT).show();
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
							people.setName(edt_name.getText().toString().trim());
							people.setCollect_datetime(date);
							dbUtils.update(people);
						}else{
							people=new People(date, cardno, pic, "补", CommonUtils.GenerateGUID(), "5", MyInfomationManager.getUserName(ReshootActivity.this));
							people.setName(edt_name.getText().toString().trim());
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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (link != null)
			return;
		Tag nfcTag = null;
		try {
			nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		} catch (Exception e) {

		}
		if (nfcTag != null) {
			link = LinkFactory.createExNFCLink(nfcTag);
			try {
				link.connect();
			} catch (Exception e) {
				showError(e.getMessage());
				try {
					link.disconnect();
				} catch (Exception ex) {
				} finally {
					link = null;
				}
				return;
			}
			idReader.setLink(link);
			showIDCardInfo(true, null,null);
			showProcessDialog("正在读卡中，请稍后");
			idReader.startReadCard();
		}
	}
	 private void showIDCardInfo(boolean isClear,PersonInfo user,String msg) {//显示身份证数据到界面
		 hideProcessDialog();
		 if (isClear){
			 return;
		 }
		 if (user==null&&msg!=null){
			 showError(msg);
		 }else{
			 edt_content.setText(user.getIdNumber().trim());
			 edt_name.setText(user.getName().trim());
		 }
	 }



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode ==RESULT_OK) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					Crop.of(Uri.fromFile(new File(DeviceUtils.getPath(ReshootActivity.this,data.getData()))), Uri.fromFile(new File(getCacheDir(), "cropped"))).withAspect(4, 5).withMaxSize(160, 200).start(ReshootActivity.this);
				}else{
					Crop.of(data.getData(), Uri.fromFile(new File(getCacheDir(), "cropped"))).withAspect(4, 5).withMaxSize(160, 200).start(ReshootActivity.this);
				}
				break;

			case CAMERA_REQUEST_CODE:
//
				if (DeviceUtils.hasSDCard()) {
					File tempFile = new File(Environment.getExternalStorageDirectory()+"/"+ IMAGE_FILE_NAME);
					Crop.of(Uri.fromFile(tempFile), Uri.fromFile(new File(getCacheDir(), "cropped"))).withAspect(4, 5).withMaxSize(160, 200).start(ReshootActivity.this);
				}else{
					Toast.makeText(ReshootActivity.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;
				case Crop.REQUEST_CROP:

					if (data != null) {
						getImageToViewFromCrop(data);
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
	private void getImageToViewFromCrop(Intent data) {
		Uri mImageCaptureUri = Crop.getOutput(data);
		Bitmap photo = null;
		if (mImageCaptureUri != null) {
			try {
				photo = MediaStore.Images.Media.getBitmap(ReshootActivity.this.getContentResolver(), mImageCaptureUri);
				Drawable drawable = new BitmapDrawable(photo);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] b = stream.toByteArray();
				pic = new String(Base64Coder.encodeLines(b));
				iv_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
				iv_photo.setImageDrawable(drawable);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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
			iv_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

	@Override
	public void onReadCardSuccess(final PersonInfo personInfo) {
		try {
			link.disconnect();
		} catch (Exception e) {
			Log.e("readcard", e.getMessage());
		} finally {
			link = null;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hideProcessDialog();
				showIDCardInfo(false,personInfo,null);
			}
		});

	}

	@Override
	public void onReadCardFailed(final String s) {
		try {
			link.disconnect();
		} catch (Exception e) {
			Log.e("readcard", e.getMessage());
		} finally {
			link = null;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hideProcessDialog();
				showError(s);
			}
		});

	}
}
