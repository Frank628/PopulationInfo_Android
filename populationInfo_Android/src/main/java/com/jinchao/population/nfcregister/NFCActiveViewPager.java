package com.jinchao.population.nfcregister;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by gaofeng on 2016-07-26.
 */
public class NFCActiveViewPager extends ViewPager {
    public NFCActiveViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NFCActiveViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
