package com.jinchao.population.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import com.jinchao.population.R;

/**
 * Created by OfferJiShu01 on 2016/11/9.
 */

public class ValidateEidtText extends EditText {

    private int textColor;
    private float textSize;
    private int textColorHint;
    private String text;
    private Drawable drawableLeft,drawableRight;
    public static final int MOBILE=0x001;
    public ValidateEidtText(Context context) {
        this(context,null);
    }

    public ValidateEidtText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ValidateEidtText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ValidateEditText,defStyleAttr,0);
        for (int i=0;i<a.getIndexCount();i++){
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.ValidateEditText_vd_textColor:
                    textColor=a.getColor(attr,Color.BLACK);
                    break;


            }
        }
        a.recycle();
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                }
            }
        });
    }


}
