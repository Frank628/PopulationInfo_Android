package com.jinchao.population.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinchao.population.R;

/**
 * Created by OfferJiShu01 on 2016/7/11.
 */
public class IDCardView extends RelativeLayout {
    private TextView tv_name,tv_nation,tv_gender,tv_year,tv_month,tv_day,tv_address,tv_idcard,tv_content;
    private ImageView iv_pic;
    private RelativeLayout cover;
    private LinearLayout ll_loading;

    public IDCardView(Context context) {
        super(context);

    }

    public IDCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_idcardview, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        cover=(RelativeLayout) findViewById(R.id.cover);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_nation=(TextView) findViewById(R.id.tv_nation);
        tv_gender=(TextView) findViewById(R.id.tv_sex);
        tv_year=(TextView) findViewById(R.id.tv_year);
        tv_month=(TextView) findViewById(R.id.tv_month);
        tv_day=(TextView) findViewById(R.id.tv_day);
        tv_address=(TextView) findViewById(R.id.tv_address);
        tv_idcard=(TextView) findViewById(R.id.tv_cardnum);
        tv_content=(TextView) findViewById(R.id.tv_content);
        iv_pic= (ImageView) findViewById(R.id.iv_head);
        ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void reading(){
        tv_content.setVisibility(View.GONE);
        ll_loading.setVisibility(View.VISIBLE);
    }
    public void showError(String error){
        tv_content.setVisibility(View.VISIBLE);
        ll_loading.setVisibility(View.GONE);
        tv_content.setText(error);
//        tv_content.setTextColor();
    }
    public void setIDCard(String name, String gender, String nation, String year, String month, String day, String address, String cardnum, Bitmap pic){
        cover.setVisibility(View.GONE);
        tv_name.setText(name);
        tv_gender.setText(gender);
        tv_nation.setText(nation);
        tv_year.setText(year);
        tv_month.setText(month);
        tv_day.setText(day);
        tv_address.setText(address);
        tv_idcard.setText(cardnum);
        iv_pic.setImageBitmap(pic);
    }

    public void clearIDCard(){
        cover.setVisibility(View.VISIBLE);
        tv_name.setText("");
        tv_gender.setText("");
        tv_nation.setText("");
        tv_year.setText("");
        tv_month.setText("");
        tv_day.setText("");
        tv_address.setText("");
        tv_idcard.setText("");
        iv_pic.setImageBitmap(null);
    }

}
