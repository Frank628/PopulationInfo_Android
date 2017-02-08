package com.jinchao.population.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jinchao.population.R;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.dbentity.UserHistory;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/7/14.
 */
public class UserDropDownPop extends PopupWindow {
    private ListView lv;
    private TextView tv_content;
    DbUtils dbUtils;
    CommonAdapter<UserHistory> adapter;
    List<UserHistory> list;
    public interface OnItemClickListener{
        void onClick(String username);
    };
    private OnItemClickListener onItemClickListener;
    public UserDropDownPop(final Activity context, OnItemClickListener onItemClickListener) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView=inflater.inflate(R.layout.pop_userdrop,null);
        this.onItemClickListener=onItemClickListener;
        initView(contentView,context);
        this.setContentView(contentView);
        this.setWidth(DeviceUtils.getWindowWidth(context)-CommonUtils.dip2px(context,30));
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);

    }
    private void initView(View v,final Activity context){
        lv= (ListView) v.findViewById(R.id.lv);
        tv_content= (TextView) v.findViewById(R.id.tv_bottom);
        dbUtils=DeviceUtils.getDbUtils(context);
        try {
            list =dbUtils.findAll(Selector.from(UserHistory.class).orderBy("time",true));
            if (list==null){
                tv_content.setText("暂无历史记录~");
                list=new ArrayList<>();
            }else if(list.size()==0){
                tv_content.setText("暂无历史记录~");
            }else{
                tv_content.setText("只保留最近的10条记录~");
            }
            adapter=new CommonAdapter<UserHistory>(context,list,R.layout.item_userhistory) {
                @Override
                public void convert(ViewHolder helper, UserHistory item, int position) {
                    helper.setText(R.id.tv_name,item.getUser_name()+"("+item.getSq_name()+")");
                }
            };
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserHistory userHistory =(UserHistory) ((ListView)parent).getItemAtPosition(position);
                    onItemClickListener.onClick(userHistory.getUser_name());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void showPopupWindow(View parent){
        if (!this.isShowing()) {
            try {
                list =dbUtils.findAll(UserHistory.class);
                if (list==null) {
                    tv_content.setText("暂无历史记录~");
                    list = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                e.printStackTrace();
            }
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }


}