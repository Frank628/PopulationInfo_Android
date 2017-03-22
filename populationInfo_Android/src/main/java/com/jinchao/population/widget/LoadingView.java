package com.jinchao.population.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinchao.population.R;

/**
 * Created by user on 2017/3/21.
 */

public class LoadingView extends RelativeLayout {
    private LinearLayout reload;
    private LinearLayout loading;
    private RelativeLayout root;
    private TextView tv_content;
    private TextView button;
    public interface OnReloadClickListener{
        void onReload();
    }
    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_loadingview, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        reload=(LinearLayout) findViewById(R.id.reload);
        loading=(LinearLayout) findViewById(R.id.loading);
        root=(RelativeLayout)findViewById(R.id.root);
        tv_content=(TextView) findViewById(R.id.tv_content);
        button=(TextView) findViewById(R.id.button);
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

    public void loading(){
        reload.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        root.setVisibility(VISIBLE);

    }
    public void reload(String str,final OnReloadClickListener onReloadClickListener){
        reload.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        root.setVisibility(View.VISIBLE);
        button.setVisibility(VISIBLE);
        tv_content.setCompoundDrawablesWithIntrinsicBounds(
                null,null, null,  null);
        tv_content.setText(str);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                onReloadClickListener.onReload();
            }
        });
    }
    public void reload(final OnReloadClickListener onReloadClickListener){
        reload.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        root.setVisibility(VISIBLE);
        button.setVisibility(VISIBLE);
        tv_content.setCompoundDrawablesWithIntrinsicBounds(
                null,null, null,  null);
        tv_content.setText("加载超时...");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                onReloadClickListener.onReload();
            }
        });
    }
    public void empty(String str){
        reload.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        root.setVisibility(VISIBLE);
        button.setVisibility(GONE);
        tv_content.setText(str);
        tv_content.setCompoundDrawablesWithIntrinsicBounds(
                null,getResources().getDrawable(R.drawable.icon_nopeople), null,  null);
    }
    public void loadComplete(){
        root.setVisibility(GONE);
    }
}
