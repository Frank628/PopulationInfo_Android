package com.jinchao.population.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class MonPickerDialog extends DatePickerDialog {  
    public MonPickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {  
        super(context, callBack, year, monthOfYear, dayOfMonth);  
        this.setTitle(year + "年" + (monthOfYear + 1) + "月");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
//            if (daySpinnerId != 0) {
//                View daySpinner = this.findViewById(daySpinnerId);
//                if (daySpinner != null) {
//                    daySpinner.setVisibility(View.GONE);
//                }
//            }
//        }else{
//            ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
//        }

    }  
  
    @Override  
    public void onDateChanged(DatePicker view, int year, int month, int day) {  
        super.onDateChanged(view, year, month, day);  
        this.setTitle(year + "年" + (month + 1) + "月");  
    }  
  
}  
