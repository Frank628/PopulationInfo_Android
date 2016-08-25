package com.jinchao.population.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinchao.population.R;

/**
 * Created by OfferJiShu01 on 2016/8/25.
 */
public class RightButtonCheckBox extends LinearLayout{
    private CheckBox checkBox;
    private TextView textView;
    private int textColor;
    private float textSize;
    private boolean checked;
    private String text;
    public  interface OnCheckedChangeListener{
       void onCheckedChanged(CompoundButton compoundButton, boolean b);
    }
    public OnCheckedChangeListener onCheckedChangeListener;
    public RightButtonCheckBox(Context context) {
        this(context,null);

    }

    public RightButtonCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RightButtonCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(HORIZONTAL);
        checkBox =new CheckBox(context);
        textView=new TextView(context);
        addView(textView,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        addView(checkBox,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RightButtonCheckBox,defStyleAttr,0);
        for (int i=0;i<a.getIndexCount();i++){
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.RightButtonCheckBox_rc_text:
                    text=a.getString(attr);
                    break;
                case R.styleable.RightButtonCheckBox_rc_textColor:
                    textColor=a.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.RightButtonCheckBox_rc_textSize:
                    textSize=a.getDimension(attr,TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 18,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RightButtonCheckBox_rc_checked:
                    checked=a.getBoolean(attr,false);
                    break;
            }
        }
        a.recycle();
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        checkBox.setChecked(checked);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.toggle();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    textView.setTextColor(Color.parseColor("#FF4081"));
                } else {
                    textView.setTextColor(Color.parseColor("#333333"));
                }
                if (onCheckedChangeListener != null){
                    onCheckedChangeListener.onCheckedChanged(compoundButton, b);

                 }
            }
        });
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener){
        this.onCheckedChangeListener=onCheckedChangeListener;
    }

    public boolean isChecked(){
        return checkBox.isChecked();
    }
    public void setChecked(boolean b){
        checkBox.setChecked(b);
    }
}
