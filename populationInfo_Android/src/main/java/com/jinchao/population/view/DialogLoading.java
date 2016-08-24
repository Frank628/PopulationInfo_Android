package com.jinchao.population.view;



import com.jinchao.population.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class DialogLoading extends Dialog{
	private Context context;
	private TextView tv_name;
	private String name;
	private boolean isSmall=false;
	public DialogLoading(Context context,String name) {
		super(context,R.style.MyDialogStyle);
		this.context = context;
		this.name=name;
	}
	public DialogLoading(Context context,String name,boolean isSmall) {
		super(context,R.style.MySmallDialogStyle);
		this.context = context;
		this.name=name;
		this.isSmall=isSmall;
	}
	public void setName(String name){
		tv_name.setText(name);
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.setCancelable(false);
        if (isSmall) {
        	setContentView(R.layout.loading_submit);
		}else{
			setContentView(R.layout.loading);	
		}
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_name.setText(name);
    }
}
