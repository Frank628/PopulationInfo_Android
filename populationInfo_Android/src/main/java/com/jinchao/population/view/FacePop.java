package com.jinchao.population.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.jinchao.population.R;
import com.jinchao.population.utils.CommonUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/7/14.
 */
public class FacePop extends PopupWindow {
    private ImageView iv1,iv2;
    private TextView tv_content;
    private ProgressBar pb;
    private Button btn_compare;
    private ImageButton close;
    ImageOptions imageOptions =new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).setUseMemCache(false).build();
    public interface OnCompareClickListener{
        void onClick();
    };
    private OnCompareClickListener onCompareClickListener;
    public FacePop(final Activity context, Bitmap apath, String bpath, OnCompareClickListener onCompareClickListener) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView=inflater.inflate(R.layout.pop_face,null);

        this.onCompareClickListener=onCompareClickListener;
        initView(contentView,context,apath,bpath);
        this.setContentView(contentView);
        this.setWidth(CommonUtils.dip2px(context,255));
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.update();
        ColorDrawable dw = new ColorDrawable(000000);
        this.setBackgroundDrawable(dw);
    }
    private void initView(View v,final Activity context,Bitmap apath,String bpath){
        pb= (ProgressBar) v.findViewById(R.id.pb);
        tv_content= (TextView) v.findViewById(R.id.tv_content);
        iv1 = (ImageView) v.findViewById(R.id.iv1);
        iv2 = (ImageView) v.findViewById(R.id.iv2);
        close= (ImageButton) v.findViewById(R.id.close);
        btn_compare=(Button) v.findViewById(R.id.btn_compare);
         iv1.setImageBitmap(apath);
        x.image().bind(iv2,bpath,imageOptions);
        btn_compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                tv_content.setText("正在进行人脸比对，请稍等...");
                onCompareClickListener.onClick();
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((CaiJiFragment)f).photofile=CommonUtils.getCompareTempImage();
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(((CaiJiFragment)f).photofile));
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                f.startActivityForResult(intent, ((CaiJiFragment)f).REQUEST_CODE_CAMERA);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacePop.this.dismiss();
            }
        });
    }

    public void setResult(String tv){
        if (pb!=null){
           pb.setVisibility(View.GONE);
            tv_content.setText(tv);
        }
    }

    public void setIv2(String path){
        if (iv2!=null){
            x.image().bind(iv2,path);
        }
    }
    public void showPopupWindow(View parent,int x,int y){
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER,x,y);
        } else {
            this.dismiss();
        }
    }


}