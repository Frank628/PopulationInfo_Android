package com.jinchao.population.view;

import android.app.*;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jinchao.population.R;
import com.jinchao.population.utils.CommonUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/8/18.
 */
public class DialogShowPic extends android.app.Dialog {
    private String url;
    private Context context;
    public DialogShowPic(Context context,String url) {
        super(context, R.style.DialogStyle);
        this.context=context;
        this.url=url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_showpic, null);
        setContentView(view);
        ImageView iv_pic= (ImageView) view.findViewById(R.id.iv_pic);
        x.image().bind(iv_pic,url,new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).build());
        view.findViewById(R.id.btn_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogShowPic.this.dismiss();
            }
        });
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
    }
}
