package com.jinchao.population.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jinchao.population.R;
import com.jinchao.population.alienPeople.registchangelogoff.PeoplesInHouseActivity;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.entity.RoomBean;
import com.jinchao.population.utils.CommonUtils;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/7/14.
 */
public class NoRoomPop extends PopupWindow {
    private ListView lv;
    private ImageView iv_close;
    private TextView tv;
    private Activity context;
    public interface OnEnsureClick{
        void onClick(boolean isEnsure);
    };
    private OnEnsureClick onEnsureClick;
    public NoRoomPop(final Activity context, List<RoomBean.BianhaoOne> list, OnEnsureClick onEnsureClick) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView=inflater.inflate(R.layout.pop_multiroom,null);
        this.context=context;
        this.onEnsureClick=onEnsureClick;
        initView(contentView,context,list);
        this.setContentView(contentView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(CommonUtils.getWindowHeight(context)/5*3);

        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundAlpha(0.5f);
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }
    private void initView(View v,final Activity context,List<RoomBean.BianhaoOne> list){
        lv = (ListView) v.findViewById(R.id.lv);
        iv_close=(ImageView) v.findViewById(R.id.iv_close);
        tv = (TextView) v.findViewById(R.id.tv);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv.setText("该地址直接对应的房屋编号");
        CommonAdapter<RoomBean.BianhaoOne> adapter=new CommonAdapter<RoomBean.BianhaoOne>(context,list,R.layout.item_text) {
            @Override
            public void convert(ViewHolder helper, RoomBean.BianhaoOne item, int position) {
                helper.setText(R.id.tv_bianhao,"房屋编号:"+item.scode);
                helper.setText(R.id.tv_content,item.hrsAdress);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoomBean.BianhaoOne bhone =(RoomBean.BianhaoOne)((ListView)parent).getItemAtPosition(position);
                Intent intent =new Intent(context,PeoplesInHouseActivity.class);
                NFCJsonBean nfcJsonBean=new NFCJsonBean(bhone.scode,bhone.hrsPname, bhone.idcard, bhone.hrsAdress, bhone.telphone, "");
                intent.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                intent.putExtra("TAG",1);
                context.startActivity(intent);
                dismiss();
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