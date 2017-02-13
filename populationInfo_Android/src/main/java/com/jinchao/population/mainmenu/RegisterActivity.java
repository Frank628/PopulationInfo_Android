package com.jinchao.population.mainmenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import android.app.AlertDialog;
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
import android.provider.MediaStore;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyApplication;
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
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.FacePop;
import com.jinchao.population.view.MingZuPop;
import com.jinchao.population.view.NationPop;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.webservice.CompareAsyncTask;
import com.jinchao.population.webservice.CompareResult;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.jinchao.population.view.NationPop.OnEnsureClickListener;
import com.jinchao.population.widget.ActionSheetDialog;
import com.jinchao.population.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.jinchao.population.widget.ActionSheetDialog.SheetItemColor;
import com.soundcloud.android.crop.Crop;

import de.greenrobot.event.EventBus;

@ContentView(R.layout.activity_register_idcard)
public class RegisterActivity extends BaseReaderActiviy  implements IDReader.IDReaderListener{
	public static final String TAG="IDCARD_DEVICE";
	@ViewInject(R.id.ib_readcard)private ImageButton ib_readcard;
	@ViewInject(R.id.edt_idcard)private EditText edt_idcard;
	@ViewInject(R.id.edt_name)private EditText edt_name;
	@ViewInject(R.id.edt_region)private EditText edt_region;
	@ViewInject(R.id.edt_sex)private EditText edt_sex;
	@ViewInject(R.id.edt_birth)private EditText edt_birth;
	@ViewInject(R.id.edt_address)private EditText edt_address;
	@ViewInject(R.id.edt_xaddress)private ValidateEidtText edt_xaddress;
	@ViewInject(R.id.compare)private ImageButton compare;
	@ViewInject(R.id.replace)private ImageButton replace;
    @ViewInject(R.id.ib_firstgencard)private ImageButton ib_firstgencard;
	@ViewInject(R.id.iv_pic)private ImageView iv_pic;
	@ViewInject(R.id.edt_formername)private ValidateEidtText edt_formername;
	private Bitmap bmp;
	private DbUtils dbUtils;
	private NationPop nationPop;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final int REQUEST_CODE_CAMERA=3;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private boolean isFirstGenerationIDCard=false;
	private String hjdz="",istakephoto="0";
	private String pic;
	private boolean isreal=false,isReplace=false;
	private RealHouseOne realHouseOne ;//实有人口传参
	private FacePop facePop;
	private boolean  isfromotherway=false;
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
		isfromotherway=getIntent().getBooleanExtra("fromotherway",false);
        dbUtils =DeviceUtils.getDbUtils(RegisterActivity.this);
		idReader.setListener(this);
		isreal =getIntent().getBooleanExtra("isreal", false);
		realHouseOne=(RealHouseOne) getIntent().getSerializableExtra("realhouseone");
		setListener();
		nationPop = new NationPop(RegisterActivity.this, new OnEnsureClickListener() {
				@Override
				public void OnEnSureClick(String nationid,final String huji,final String wushi_huji) {
					Dialog.showSelectDialog(RegisterActivity.this, "保留/去除中间的“市”！", new DialogClickListener() {
						@Override
						public void confirm() {
							edt_address.setText(huji);
						}

						@Override
						public void cancel() {
							edt_address.setText(wushi_huji);
						}
					},"保留","去除");
				}
		});
		People people=(People) getIntent().getSerializableExtra("people");
		String idcard=getIntent().getStringExtra("idcard");
		if(people!=null){
			edt_idcard.setText(idcard);
			edt_name.setText(people.getName());
			edt_sex.setText(people.getSex());
			edt_birth.setText(people.getBirthday());
			edt_address.setText(people.getAddress());
			edt_region.setText(people.getPeople());
            pic=people.getPicture();
            bmp= getIntent().getParcelableExtra("pic");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			imgA=stream.toByteArray();
            iv_pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv_pic.setImageBitmap(bmp);
			compare.setVisibility(View.VISIBLE);
			replace.setVisibility(View.VISIBLE);
		}else {
			if (isfromotherway){
                istakephoto="0";
                isFirstGenerationIDCard=true;
                resetFirstGenerationIDCardText();
				edt_idcard.setText(idcard);

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
					edt_name.setText(getIntent().getStringExtra("name"));
					edt_address.setText(getIntent().getStringExtra("huji1"));
					edt_xaddress.setText(getIntent().getStringExtra("huji2"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
			}
		}
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
            ((MyApplication)getApplication()).setIsSureDengji(false);
			idReader.setLink(link);
			showIDCardInfo(true, null,null);
			istakephoto="0";
			showProcessDialog("正在读卡中，请稍后");
			isFirstGenerationIDCard=false;
			isReplace=false;
			idReader.startReadCard();
		}
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
				showIDCardInfo(false, personInfo,null);
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

	 private void showIDCardInfo(boolean isClear,PersonInfo user,String msg) {//显示身份证数据到界面
		 imgA=null;
		 muser=null;
		 compare.setVisibility(View.GONE);
		 replace.setVisibility(View.GONE);
		 hideProcessDialog();
		 if (isClear){
			 resetSecondGenerationIDCardText();
			 return;
		 }
		 if (user==null&&msg!=null){
			showError(msg);
		 }else{
			 compare.setVisibility(View.VISIBLE);
			 replace.setVisibility(View.VISIBLE);
			 imgA=user.getPhoto();
			 muser=user;
			 edt_name.setText(user.getName().trim());
			 edt_sex.setText(user.getSex().trim());
			 edt_region.setText(user.getNation().trim());
			 edt_birth.setText(user.getBirthday().trim());
			 edt_address.setText(user.getAddress().trim());
			 edt_idcard.setText(user.getIdNumber().trim());
			 bmp =  BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length);
			 iv_pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
			 iv_pic.setImageBitmap(bmp);
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 bmp.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			 byte[] b = stream.toByteArray();
			 pic = new String(Base64Coder.encodeLines(b));
		 }
		 isReading = false;
	 }

	private void resetFirstGenerationIDCardText(){
        ((MyApplication)getApplication()).setIsSureDengji(false);
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
		edt_formername.setText("");
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
//        edt_sex.setFocusable(true);
//        edt_birth.setFocusable(true);
        edt_address.setFocusable(false);
        edt_xaddress.setFocusable(true);
        edt_idcard.setFocusableInTouchMode(true);
        edt_name.setFocusableInTouchMode(true);
//		edt_region.setFocusableInTouchMode(true);
//        edt_sex.setFocusableInTouchMode(true);
//        edt_birth.setFocusableInTouchMode(true);
		edt_address.setFocusableInTouchMode(false);
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
		edt_formername.setText("");
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
//				startPhotoZoom(data.getData());

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					Crop.of(Uri.fromFile(new File(DeviceUtils.getPath(RegisterActivity.this,data.getData()))), Uri.fromFile(new File(getCacheDir(), "cropped"))).withAspect(4, 5).withMaxSize(160, 200).start(RegisterActivity.this);
				}else{
					Crop.of(data.getData(), Uri.fromFile(new File(getCacheDir(), "cropped"))).withAspect(4, 5).withMaxSize(160, 200).start(RegisterActivity.this);
				}

				break;
			case CAMERA_REQUEST_CODE:
				if (DeviceUtils.hasSDCard()) {
					File tempFile = new File(Environment.getExternalStorageDirectory()+"/"+ IMAGE_FILE_NAME);
//					startPhotoZoom(Uri.fromFile(tempFile));
					Crop.of(Uri.fromFile(tempFile), Uri.fromFile(new File(getCacheDir(), "cropped"))).withAspect(4, 5).withMaxSize(160, 200).start(RegisterActivity.this);
				}else{
					Toast.makeText(RegisterActivity.this, "未找到存储卡，无法存储照片！",
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
		intent.putExtra("scale", true);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 160);
		intent.putExtra("outputY", 200);
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
			istakephoto="1";
			isReplace=true;
			iv_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iv_pic.setImageDrawable(drawable);
		}
	}
	private void getImageToViewFromCrop(Intent data) {
		Uri mImageCaptureUri = Crop.getOutput(data);
		Bitmap photo = null;
		if (mImageCaptureUri != null) {
			try {
				photo = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(), mImageCaptureUri);
				Drawable drawable = new BitmapDrawable(photo);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] b = stream.toByteArray();
				pic = new String(Base64Coder.encodeLines(b));
				isReplace=true;
				iv_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
				iv_pic.setImageDrawable(drawable);
				istakephoto="1";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	private void showfacepop() {
		facePop =new FacePop(this, bmp,photofile.getAbsolutePath(), new FacePop.OnCompareClickListener() {
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


	@Event(value={R.id.ib_firstgencard})//点击  一代证；
	private void onReadFirstGenerationCardClick(View view) {
		istakephoto="0";
		isFirstGenerationIDCard=true;
		resetFirstGenerationIDCardText();
	}
	@Event(value={R.id.edt_address})//暂时去除选择户籍地址
	private void onAddressClick(View view){
		if (isFirstGenerationIDCard) {
			if (getCurrentFocus() != null) {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			edt_idcard.clearFocus();
			nationPop.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
		}
	}
	@Event(value={R.id.edt_region})
	private void mingzuClick(View view){
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		MingZuPop mingZuPop = new MingZuPop(this, new com.jinchao.population.view.MingZuPop.OnEnsureClickListener() {
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
		String formername =edt_formername.getText().toString().trim();
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
		if (!TextUtils.isEmpty(formername)){
			if (!CommonUtils.isChinese(formername)){
				Toast.makeText(this, "别名中含非中文字符！", Toast.LENGTH_SHORT).show();
				return;
			}
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
		if (isFirstGenerationIDCard){
			if (xaddress.length()<5){
				Toast.makeText(this, "详细地址至少5个字符！", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if (CommonIdcard.validateCard(idcard)) {
			if (idcard.length() == 15) {
				idcard = CommonIdcard.conver15CardTo18(idcard);
				edt_idcard.setText(idcard);
				Toast.makeText(RegisterActivity.this, "15位转18位证件号成功",Toast.LENGTH_SHORT).show();
			} else if (idcard.length() == 17) {
				idcard = CommonIdcard.conver17CardTo18(idcard);
				edt_idcard.setText(idcard);
				Toast.makeText(RegisterActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
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
			if (!CommonUtils.isAddress(xaddress)) {
				Toast.makeText(this, "户籍详细地址只能输入汉字、数字、大写字母和-(中横线)以及英文半角小括号和、(中文半角顿号)!",  Toast.LENGTH_SHORT).show();
				return;
			}
		}
		address=address+xaddress;
		if (pic==null) {
			final People oPeople= new People(name, idcard, nation, gender, birth, address,"",idcard.substring(0, 6),"1",MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this),edt_formername.getText().toString().trim()) ;
			Dialog.showSelectDialog(RegisterActivity.this, "未拍照，请拍照?", new DialogClickListener() {
				@Override
				public void confirm() {
                    if (isfromotherway){//如果是从人员信息验证界面进入的
                            Intent intent = new Intent(RegisterActivity.this,HandleIDActivity.class);
                            intent.putExtra("people", oPeople);
                            intent.putExtra("isHandle", getIntent().getBooleanExtra("isHandle",false));
                            startActivity(intent);
                            RegisterActivity.this.finish();
                        return;
                    }
					if (isreal) {
						Intent intent =new Intent(RegisterActivity.this, SingleRealPopulationActivity.class);
						intent.putExtra("people", oPeople );
						intent.putExtra("realhouseone", realHouseOne);
						startActivity(intent);
						RegisterActivity.this.finish();
					}else{
						Intent intent =new Intent(RegisterActivity.this, ZanZhuActivity.class);
						intent.putExtra("people",oPeople);
                        RegisterActivity.this.startActivity(intent);

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
			People p1=new People(name, idcard, nation, gender, birth, address,pic,idcard.substring(0, 6),"1",MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this));
			p1.setIstakephoto(istakephoto);
			intent.putExtra("people",p1 );
			intent.putExtra("realhouseone", realHouseOne);
			startActivity(intent);
			RegisterActivity.this.finish();
		}else{
            if (isfromotherway){//如果是从人员信息验证界面进入的
                Intent intent = new Intent(RegisterActivity.this,HandleIDActivity.class);
				People p2=new People(name, idcard, nation, gender, birth, address,pic,idcard.substring(0, 6),"1",MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this),edt_formername.getText().toString().trim());
				p2.setIstakephoto(istakephoto);
                intent.putExtra("people",p2);
				intent.putExtra("isHandle", getIntent().getBooleanExtra("isHandle",false));
                startActivity(intent);
                RegisterActivity.this.finish();
                return;
            }
			Intent intent =new Intent(this, ZanZhuActivity.class);//isReplace?"1":(isFirstGenerationIDCard?"1":"2")
			People p1=new People(name, idcard, nation, gender, birth, address,pic,idcard.substring(0, 6),"1",MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this),edt_formername.getText().toString().trim());
			p1.setIstakephoto(istakephoto);
            intent.putExtra("people", p1);
            RegisterActivity.this.startActivity(intent);

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

    @Override
    public void onResume() {
        super.onResume();
        if (((MyApplication)getApplication()).getIsSureDengji()){
            resetSecondGenerationIDCardText();
        }else{
            if (!TextUtils.isEmpty(edt_idcard.getText().toString())){
                try {
                    People people=dbUtils.findFirst(Selector.from(People.class).where("cardno","=",edt_idcard.getText().toString().trim()));
                    if (people!=null) {
                        if (!TextUtils.isEmpty(people.picture)) {
                            iv_pic.setImageBitmap(CommonUtils.base64ToBitmap(people.picture));
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
