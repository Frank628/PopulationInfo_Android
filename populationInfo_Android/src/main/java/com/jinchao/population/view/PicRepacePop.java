package com.jinchao.population.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.utils.CommonUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/7/14.
 */
public class PicRepacePop extends PopupWindow {
    private ImageView iv1;
    private Button btn_retake,btn_ensure;
    ImageOptions imageOptions =new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).setUseMemCache(false).build();
    public interface OnEnsureClick{
        void onClick(boolean isEnsure);
    };
    private OnEnsureClick onEnsureClick;
    public PicRepacePop(final Activity context, Drawable apath, OnEnsureClick onEnsureClick) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView=inflater.inflate(R.layout.pop_replace,null);

        this.onEnsureClick=onEnsureClick;
        initView(contentView,context,apath);
        this.setContentView(contentView);
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
    }
    private void initView(View v,final Activity context,Drawable apath){
        iv1 = (ImageView) v.findViewById(R.id.iv1);
        btn_retake=(Button) v.findViewById(R.id.btn_retake);
        btn_ensure=(Button) v.findViewById(R.id.btn_ensure);
        iv1.setImageDrawable(apath);
        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnsureClick.onClick(true);
                PicRepacePop.this.dismiss();
            }
        });
        btn_retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnsureClick.onClick(false);
                PicRepacePop.this.dismiss();
            }
        });

    }


    public void showPopupWindow(View parent,int x,int y){
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER,x,y);
        } else {
            this.dismiss();
        }
    }


}