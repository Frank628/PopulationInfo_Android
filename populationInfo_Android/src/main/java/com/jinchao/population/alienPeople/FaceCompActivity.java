package com.jinchao.population.alienPeople;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyApplication;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.FaceCompareResult;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.IDCardView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.bitmap;

/**
 * Created by user on 2017/9/4.
 */
@ContentView(R.layout.activity_facecomp)
public class FaceCompActivity extends BaseReaderActiviy implements IDReader.IDReaderListener{
    public static final String app_key="jiangsumiaolian";
    @ViewInject(R.id.idcard)IDCardView idCardView;
    @ViewInject(R.id.iv1)ImageView iv1;
    @ViewInject(R.id.iv2)ImageView iv2;
    @ViewInject(R.id.tv_result)TextView tv_result;
    public  File iv1File,iv2File;
    public  PersonInfo person;
    private static final int REQUEST_CODE_CAMERA=3;
    ImageOptions options=new ImageOptions.Builder()
            .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
            .setUseMemCache(false)
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("身份信息录入");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        idReader.setListener(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) idCardView.getLayoutParams();
        params.width= CommonUtils.getWindowWidth(this)-CommonUtils.dip2px(this,35);
        params.height=(CommonUtils.getWindowWidth(this)-CommonUtils.dip2px(this,35))*377/600;
        idCardView.setLayoutParams(params);

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
                person=null;
                showCard(null);
                idCardView.reading();
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
                    person=personInfo;
                    showCard(personInfo);
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
                    showError(s);
                }
            });

        }

    private void showCard(PersonInfo personInfo){
        if (personInfo==null){
            idCardView.clearIDCard();
            return;
        }
        iv1File=new File(Constants.DB_PATH+"image1.jpg");
        Bitmap bitmap=BitmapFactory.decodeByteArray(personInfo.getPhoto(), 0, personInfo.getPhoto().length);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(iv1File));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv1.setImageBitmap(bitmap);
        idCardView.setIDCard(personInfo.getName().trim(),personInfo.getSex().trim(),personInfo.getNation().trim(),
                personInfo.getBirthday().trim().substring(0,4),personInfo.getBirthday().trim().substring(4,6),personInfo.getBirthday().trim().substring(6,8),
                personInfo.getAddress(),personInfo.getIdNumber().trim(), bitmap);

    }
    public void showError(String error){
        iv1File=null;
        idCardView.showError(error);
    }
    @Event(value={R.id.iv2})
    private void addPic(View view){
        iv2File=new File(Constants.DB_PATH+"image2.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(iv2File));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
    @Event(value={R.id.btn_compare})
    private void compare(View view){
        if (iv1File==null){
            Toast.makeText(this, "请先读身份证", Toast.LENGTH_SHORT).show();
            return;
        }
        if (iv2File==null){
            Toast.makeText(this, "请先拍照", Toast.LENGTH_SHORT).show();
            return;
        }
        showProcessDialog("比对中，请稍后...");
        compareFace(iv1File,iv2File);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    setPhoto(iv2File);
                    break;
                default:
                    break;
            }
        }
    }
    public void setPhoto(File photofile) {

        if (photofile != null && photofile.exists() && photofile.length() > 10) {

            byte[] img = CommonUtils.getByte(photofile);// 获得源图片
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);// 将原图片转换成bitmap，方便后面转换
            compressImageToFile(bitmap,iv2File);
            x.image().bind(iv2,iv2File.getAbsolutePath(),options);
        } else {
            iv2File=null;
            Toast.makeText(this, "拍摄照片失败", Toast.LENGTH_SHORT).show();
        }
    }
    public static void compressImageToFile(Bitmap bmp,File file) {
        // 0-100 100为不压缩
        int options = 20;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void compareFace(File image1,File image2){
        RequestParams params=new RequestParams("http://218.94.149.27:20175/Facedetection");
        params.addBodyParameter("app_key",app_key);
        params.addBodyParameter("image1",image1,"application/octet-stream","image1.jpg");
        params.addBodyParameter("image2",image2,"application/octet-stream","image2.jpg");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("cccccc",result);
                hideProcessDialog();
                parseJson(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {
                hideProcessDialog();
            }
        });

    }
    private void parseJson(String result){

        try {
            FaceCompareResult faceCompareResult= GsonTools.changeGsonToBean(result,FaceCompareResult.class);
            String res="";
            if(faceCompareResult.status.equals("ok")){
                res="相似度："+faceCompareResult.Similarity+"%"+(faceCompareResult.Similarity>70?"可以判断为同一人":"可以判断不为同一人");
            }else{
                if (faceCompareResult.msg.contains("face feature")){
                    res="比对失败，第二张图中未发现人脸特征";
                }else{
                    res="比对失败，"+faceCompareResult.msg;
                }

            }
            tv_result.setText(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
