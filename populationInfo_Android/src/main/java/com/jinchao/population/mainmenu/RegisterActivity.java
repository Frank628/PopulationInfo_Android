package com.jinchao.population.mainmenu;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.caihua.cloud.common.User;
import com.caihua.cloud.common.enumerate.ConnectType;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.Nation;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RealHouseBean.RealHouseOne;
import com.jinchao.population.realpopulation.SingleRealPopulationActivity;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.FacePop;
import com.jinchao.population.view.MingZuPop;
import com.jinchao.population.view.NationPop;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.webservice.CompareAsyncTask;
import com.jinchao.population.webservice.CompareResult;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.jinchao.population.view.NationPop.OnEnsureClickListener;
import com.jinchao.population.widget.ActionSheetDialog;
import com.jinchao.population.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.jinchao.population.widget.ActionSheetDialog.SheetItemColor;
@ContentView(R.layout.activity_register_idcard)
public class RegisterActivity extends BaseReaderActiviy{
	public static final String TAG="IDCARD_DEVICE";
	@ViewInject(R.id.ib_readcard)private ImageButton ib_readcard;
	@ViewInject(R.id.edt_idcard)private EditText edt_idcard;
	@ViewInject(R.id.edt_name)private EditText edt_name;
	@ViewInject(R.id.edt_region)private EditText edt_region;
	@ViewInject(R.id.edt_sex)private EditText edt_sex;
	@ViewInject(R.id.edt_birth)private EditText edt_birth;
	@ViewInject(R.id.edt_address)private EditText edt_address;
	@ViewInject(R.id.edt_xaddress)private EditText edt_xaddress;
	@ViewInject(R.id.compare)private ImageButton compare;
	@ViewInject(R.id.replace)private ImageButton replace;
	@ViewInject(R.id.iv_pic)private ImageView iv_pic;
	private Bitmap bmp;
	private DbUtils dbUtils;
	private NationPop nationPop;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final int REQUEST_CODE_CAMERA=3;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private boolean isFirstGenerationIDCard=false;
	private String hjdz="";
	private String pic;
	private DialogLoading dialogLoading;
	private boolean isreal=false,isReplace=false;
	private RealHouseOne realHouseOne ;//实有人口传参
	private FacePop facePop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("身份信息录入");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		idReader = new IDReader(this, mHandler);
		isreal =getIntent().getBooleanExtra("isreal", false);
		realHouseOne=(RealHouseOne) getIntent().getSerializableExtra("realhouseone");
		setListener();
		nationPop = new NationPop(RegisterActivity.this, new OnEnsureClickListener() {
				@Override
				public void OnEnSureClick(String nationid) {
					try {
			              dbUtils = DeviceUtils.getDbUtils(RegisterActivity.this);
			              Nation nation = dbUtils.findById(Nation.class,nationid);
			              edt_address.setText(nation.getNation_name());
			            } catch (DbException e) {
			                    e.printStackTrace();
			            }
				}
		});
		dialogLoading = new DialogLoading(RegisterActivity.this, "读卡中...", true);
	}
	private void setListener(){
		 edt_idcard.setOnFocusChangeListener(new OnFocusChangeListener() {
	            @Override
	            public void onFocusChange(View arg0, boolean hasFoused) {
	                if (!hasFoused) {
	                    try {
	                        String idcardNO = edt_idcard.getText().toString().trim();
	                        if (CommonIdcard.validateCard(idcardNO)) {
	                            if (idcardNO.length() == 15) {
	                                idcardNO = CommonIdcard.conver15CardTo18(idcardNO);
	                                edt_idcard.setText(idcardNO);
	                                Toast.makeText(RegisterActivity.this, "15位转18位证件号成功",Toast.LENGTH_SHORT).show();
	                                getIDInfo(idcardNO);
	                            } else if (idcardNO.length() == 17) {
	                                idcardNO = CommonIdcard.conver17CardTo18(idcardNO);
	                                edt_idcard.setText(idcardNO);
	                                Toast.makeText(RegisterActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
	                                getIDInfo(idcardNO);
	                            } else {
	                                getIDInfo(idcardNO);
	                            }
	                        } else {
	                            Toast.makeText(RegisterActivity.this, "请先输入合法的身份证号", Toast.LENGTH_SHORT).show();
	                        }

	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
		 });
	}
	 private void getIDInfo(String idcardNO) {
         edt_birth.setText(idcardNO.substring(6, 14));
         edt_sex.setText(CommonIdcard.getGenderByIdCard(idcardNO));
         edt_region.setText("汉");
         try {
             dbUtils =DeviceUtils.getDbUtils(RegisterActivity.this);
             Nation nation = dbUtils.findFirst(Selector.from(Nation.class).where("id", "=", idcardNO.substring(0, 6)));
             if (nation != null) {
                 edt_address.setText(nation.getNation_name());
             } else {
                 Toast.makeText(RegisterActivity.this, "户籍地址未能匹配成功", Toast.LENGTH_SHORT).show();
             }
         } catch (DbException e) {
             e.printStackTrace();
         }
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
		showIDCardInfo(true, null);
		isReading = true;
		showProcessDialog("正在读卡中，请稍后");
		isFirstGenerationIDCard=false;
		isReplace=false;
		idReader.connect(ConnectType.NFC, parcelable);
	}

	 private void showIDCardInfo(boolean isClear,User user) {//显示身份证数据到界面
		 imgA=null;
		 muser=null;
		 compare.setVisibility(View.GONE);
		 replace.setVisibility(View.GONE);
		 hideProcessDialog();
		 if (isClear){
			 resetSecondGenerationIDCardText();
			 return;
		 }
		 if (user==null){
			showError();
		 }else{
			 compare.setVisibility(View.VISIBLE);
			 replace.setVisibility(View.VISIBLE);
			 imgA=user.headImg;
			 muser=user;
			 edt_name.setText(user.name.trim());
			 edt_sex.setText(user.sexL.trim());
			 edt_region.setText(user.nationL.trim());
			 edt_birth.setText(user.brithday.trim());
			 edt_address.setText(user.address.trim());
			 edt_idcard.setText(user.id.trim());
			 bmp =  BitmapFactory.decodeByteArray(user.headImg, 0, user.headImg.length);
			 iv_pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
			 iv_pic.setImageBitmap(bmp);
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 bmp.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			 byte[] b = stream.toByteArray();
			 pic = new String(Base64Coder.encodeLines(b));
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
					showError();
					isReading = false;
					if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
						SharePrefUtil.saveString(RegisterActivity.this,"mac",null);
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
	private void resetFirstGenerationIDCardText(){
		compare.setVisibility(View.GONE);
		replace.setVisibility(View.GONE);
		pic=null;
        edt_idcard.setHint("请输入身份证号");
        edt_name.setHint("请输入姓名");
        edt_region.setHint("点击选择名族");
        edt_sex.setHint("自动获取");
        edt_birth.setHint("自动获取");
        edt_address.setHint("自动获取（若信息不符请点击选择）");
        edt_xaddress.setHint("请输入户籍详址");
        edt_idcard.setText("");
        edt_name.setText("");
        edt_region.setText("");
        edt_sex.setText("");
        edt_birth.setText("");
        edt_address.setText("");
        edt_xaddress.setText("");
		iv_pic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		iv_pic.setImageResource(R.drawable.camera_large);
        edt_idcard.setFocusable(true);
        edt_name.setFocusable(true);
//		edt_region.setFocusable(true);
        edt_sex.setFocusable(true);
        edt_birth.setFocusable(true);
        edt_address.setFocusable(true);
        edt_xaddress.setFocusable(true);
        edt_idcard.setFocusableInTouchMode(true);
        edt_name.setFocusableInTouchMode(true);
//		edt_region.setFocusableInTouchMode(true);
        edt_sex.setFocusableInTouchMode(true);
        edt_birth.setFocusableInTouchMode(true);
		edt_address.setFocusableInTouchMode(true);
        edt_xaddress.setVisibility(View.VISIBLE);
	}
	private void resetSecondGenerationIDCardText(){
		edt_idcard.setHint("自动读取");
        edt_name.setHint("自动读取");
        edt_region.setHint("自动读取");
        edt_sex.setHint("自动读取");
        edt_birth.setHint("自动读取");
        edt_address.setHint("自动读取");
        edt_idcard.setText("");
        edt_name.setText("");
        edt_region.setText("");
        edt_sex.setText("");
        edt_birth.setText("");
        edt_address.setText("");
        edt_xaddress.setText("");
        iv_pic.setImageDrawable(null);
        edt_idcard.setFocusable(false);
        edt_name.setFocusable(false);
        edt_region.setFocusable(false);
        edt_sex.setFocusable(false);
        edt_birth.setFocusable(false);
        edt_address.setFocusable(false);
        edt_xaddress.setVisibility(View.GONE);
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
					Toast.makeText(RegisterActivity.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			case REQUEST_CODE_CAMERA :
					setPhoto(photofile);
					showfacepop();
					break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public void setPhoto(File photofile) {
		imgB = null;
		if (photofile != null && photofile.exists() && photofile.length() > 10) {
			byte[] img = CommonUtils.getByte(photofile);// 获得源图片
			Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);// 将原图片转换成bitmap，方便后面转换
			imgB = CommonUtils.Bitmap2Bytes(bitmap);// 得到有损图
		} else {
			Toast.makeText(this, "拍摄照片失败", Toast.LENGTH_SHORT).show();
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
			String url=DeviceUtils.getPath(RegisterActivity.this,uri);
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
			isReplace=true;
			iv_pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
			iv_pic.setImageDrawable(drawable);
		}
	}
	private void showfacepop() {
		facePop =new FacePop(this, BitmapFactory.decodeByteArray(muser.headImg, 0, muser.headImg.length),photofile.getAbsolutePath(), new FacePop.OnCompareClickListener() {
			@Override
			public void onClick() {
				if (imgB != null && imgA != null) {
					new MyCompareAsyncTask(imgA, imgB).execute();
				} else if (imgA == null) {
					showDialog("请读取身份证");
				} else if (imgB == null) {
					showDialog("请拍照");
				}
			}
		});
		facePop.showPopupWindow(findViewById(R.id.root),0,0);
		backgroundAlpha(0.3f);
		facePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});
	}
	public void showDialog(String str) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage(str);
		builder.setPositiveButton(android.R.string.ok, null);
		builder.create().show();
	}
	@Event(value={R.id.compare})//
	private void compareClick(View view) {
		photofile = CommonUtils.getCompareTempImage();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}
	@Event(value=R.id.replace)
	private void replaceClick(View view){
        new ActionSheetDialog(RegisterActivity.this)
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

//	@Event(value={R.id.ib_readcard})//点击“读卡”，读取身份证；
//	private void onReadCardClick(View view) {
//		isFirstGenerationIDCard=false;
//		showIDCardInfo(true,null);
//		String net = SharePrefUtil.getString(this,Constants.DEVICE_WAY,"自动");
//		switch (net) {
//
//			case "自动":
//				int a = HasOTGDeviceConnected();
//				if (a == 0) {
//					showProcessDialog("正在读卡中，请稍后");
//					idReader.connect(ConnectType.OTG);
//				} else if (a == 1) {
//					showProcessDialog("正在读卡中，请稍后");
//					idReader.connect(ConnectType.OTGAccessory);
//				} else {
//					String mac=SharePrefUtil.getString(RegisterActivity.this,"mac",null);
//					if (mac == null) {
//						deviceListDialogFragment.show(getSupportFragmentManager(), "");
//					} else {
//						showProcessDialog("正在读卡中，请稍后");
//						int delayMillis = SharePrefUtil.getInt(RegisterActivity.this, Constants.BluetoothSetting_long_time,15);
//						idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
//					}
//				}
//				break;
//			case "蓝牙":
//				String mac=SharePrefUtil.getString(RegisterActivity.this,"mac",null);
//				if (mac == null) {
//					deviceListDialogFragment.show(getSupportFragmentManager(), "");
//				} else {
//					showProcessDialog("正在读卡中，请稍后");
//					int delayMillis = SharePrefUtil.getInt(RegisterActivity.this, Constants.BluetoothSetting_long_time,15);
//					idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
//				}
//				break;
//			case "OTG":
//				int a2 = HasOTGDeviceConnected();
//				if (a2 == 0) {
//					showProcessDialog("正在读卡中，请稍后");
//					idReader.connect(ConnectType.OTG);
//				} else if (a2 == 1) {
//					showProcessDialog("正在读卡中，请稍后");
//					idReader.connect(ConnectType.OTGAccessory);
//				} else {
//					AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, AlertDialog.THEME_HOLO_LIGHT);
//					builder.setMessage("找不到OTG设备");
//					builder.setPositiveButton("确定", null);
//					builder.show();
//				}
//				break;
//
//			case "NFC":
//				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, AlertDialog.THEME_HOLO_LIGHT);
//				builder.setMessage("当前是NFC模式，请将身份证贴在手机背面");
//				builder.setPositiveButton("确定", null);
//				builder.show();
//				break;
//		}
//	}
	@Event(value={R.id.ib_firstgencard})//点击  一代证；
	private void onReadFirstGenerationCardClick(View view) {
		isFirstGenerationIDCard=true;
		resetFirstGenerationIDCardText();
	}
//	@Event(value={R.id.edt_address})暂时去除选择户籍地址
//	private void onAddressClick(View view){
//		if (getCurrentFocus()!=null) {
//			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//		}
//		 nationPop.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
//	}
	@Event(value={R.id.edt_region})
	private void mingzuClick(View view){
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		MingZuPop mingZuPop = new MingZuPop(this, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				edt_region.setText(str);
			}
		});
        mingZuPop.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.iv_pic})
	private void takePhotoClick(View view){
		if (isFirstGenerationIDCard) {
		new ActionSheetDialog(RegisterActivity.this)
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
	@Event(value={R.id.ib_checkinfo})
	private void yanZhengClick(View view){
		String idcard =edt_idcard.getText().toString().trim();
		String name =edt_name.getText().toString().trim();
		String nation =edt_region.getText().toString().trim();
		String gender =edt_sex.getText().toString().trim();
		String birth =edt_birth.getText().toString().trim();
		String address =edt_address.getText().toString().trim();
		String xaddress =edt_xaddress.getText().toString().trim();
		if (CommonUtils.isEmpty(name)) {
			if (isFirstGenerationIDCard)
				Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "请先读卡", Toast.LENGTH_SHORT).show();
			return;
		}
		if (CommonUtils.isEmpty(gender)) {
			Toast.makeText(this, "请输入性别", Toast.LENGTH_SHORT).show();
			return;
		}
		if (CommonUtils.isEmpty(nation)) {
			Toast.makeText(this, "请选择名族",  Toast.LENGTH_SHORT).show();
			return;
		}
		if (CommonUtils.isEmpty(birth)) {
			Toast.makeText(this, "请输入出生日期",  Toast.LENGTH_SHORT).show();
			return;
		}
		if (CommonUtils.isEmpty(idcard)) {
			Toast.makeText(this, "请输入身份证号", Toast.LENGTH_SHORT).show();
			return;
		}
		if (CommonIdcard.validateCard(idcard)) {
			if (idcard.length() == 15) {
				idcard = CommonIdcard.conver15CardTo18(idcard);
				edt_idcard.setText(idcard);
				Toast.makeText(RegisterActivity.this, "15位转18位证件号成功",Toast.LENGTH_SHORT).show();
				getIDInfo(idcard);
			} else if (idcard.length() == 17) {
				idcard = CommonIdcard.conver17CardTo18(idcard);
				edt_idcard.setText(idcard);
				Toast.makeText(RegisterActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
				getIDInfo(idcard);
			} else {
				getIDInfo(idcard);
			}
		} else {
			Toast.makeText(RegisterActivity.this, "请先输入合法的身份证号", Toast.LENGTH_SHORT).show();
			return;
		}
		if (CommonUtils.isEmpty(address)) {
			Toast.makeText(this, "请输入住址",  Toast.LENGTH_SHORT).show();
			return;
		}
		if (isFirstGenerationIDCard) {
			if (CommonUtils.isEmpty(xaddress)) {
				Toast.makeText(this, "请输入详细住址",  Toast.LENGTH_SHORT).show();
				return;
			}
		}
		address=address+xaddress;
		if (pic==null) {
			final People oPeople= new People(name, idcard, nation, gender, birth, address,"",idcard.substring(0, 6),isReplace?"1":(isFirstGenerationIDCard?"1":"2"),MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this)) ;
			Dialog.showSelectDialog(RegisterActivity.this, "未拍照，是否放弃拍照?", new DialogClickListener() {
				@Override
				public void confirm() {
					if (isreal) {
						Intent intent =new Intent(RegisterActivity.this, SingleRealPopulationActivity.class);
						intent.putExtra("people", oPeople );
						intent.putExtra("realhouseone", realHouseOne);
						startActivity(intent);
						RegisterActivity.this.finish();
					}else{
						Intent intent =new Intent(RegisterActivity.this, ZanZhuActivity.class);
						intent.putExtra("people",oPeople);
						startActivity(intent);
					}
				}
				
				@Override
				public void cancel() {
					Toast.makeText(RegisterActivity.this, "请先拍摄证件照",  Toast.LENGTH_SHORT).show();
				}
			});
			return;
		}
		if (isreal) {
			Intent intent =new Intent(this, SingleRealPopulationActivity.class);
			intent.putExtra("people", new People(name, idcard, nation, gender, birth, address,pic,idcard.substring(0, 6),isReplace?"1":(isFirstGenerationIDCard?"1":"2"),MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this)) );
			intent.putExtra("realhouseone", realHouseOne);
			startActivity(intent);
			RegisterActivity.this.finish();
		}else{
			Intent intent =new Intent(this, ZanZhuActivity.class);
			intent.putExtra("people", new People(name, idcard, nation, gender, birth, address,pic,idcard.substring(0, 6),isReplace?"1":(isFirstGenerationIDCard?"1":"2"),MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this)) );
			startActivity(intent);
		}
	}
	private class MyCompareAsyncTask extends CompareAsyncTask {
		public MyCompareAsyncTask(byte[] imgA, byte[] imgB) {
			super(imgA, imgB);
		}
		@Override
		protected void onPreExecute() {}
		@Override
		protected void onPostExecute(CompareResult result) {
			if (result == null) {
				if (facePop!=null){
					facePop.setResult("人脸对比失败");
				}
				return;
			}
			StringBuilder builder = new StringBuilder();
			double score = result.getScore();
			builder.append("相似度：" + (score / 100) + "%,");
			builder.append(score >= 7000 ? "可以判断为同一人" : "可以判断不为同一个人");
			if (facePop!=null){
				facePop.setResult(builder.toString());
			}
		}
	}
	public void backgroundAlpha(float bgAlpha)
	{
		WindowManager.LayoutParams lp = RegisterActivity.this.getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		RegisterActivity.this.getWindow().setAttributes(lp);
	}
}
