package com.jinchao.population.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import android.widget.Toast;

import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.alienPeople.registchangelogoff.PeoplesInHouseActivity;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddressOldBean;
import com.jinchao.population.dbentity.HouseAddressOldBean10;
import com.jinchao.population.dbentity.HouseAddressOldBean2;
import com.jinchao.population.dbentity.HouseAddressOldBean3;
import com.jinchao.population.dbentity.HouseAddressOldBean4;
import com.jinchao.population.dbentity.HouseAddressOldBean5;
import com.jinchao.population.dbentity.HouseAddressOldBean6;
import com.jinchao.population.dbentity.HouseAddressOldBean7;
import com.jinchao.population.dbentity.HouseAddressOldBean8;
import com.jinchao.population.dbentity.HouseAddressOldBean9;
import com.jinchao.population.entity.DeleteRealPeopleBean;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.entity.RoomBean;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.XMLParserUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/7/14.
 */
public class NoRoomPop extends PopupWindow {
    private ListView lv;
    private ImageView iv_close;
    private TextView tv;
    private Activity context;
    public ProgressDialog dialog;
    private DbUtils dbUtils;
    public int database_tableNo=0;//当前登录账号使用的哪个数据库，0:未下载的地址库，1：表1,2：表2.。。。
    public interface OnEnsureClick{
        void onClick(boolean isEnsure);
    };
    private OnEnsureClick onEnsureClick;
    public NoRoomPop(final Activity context, List<RoomBean.BianhaoOne> list, OnEnsureClick onEnsureClick) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView=inflater.inflate(R.layout.pop_multiroom,null);
        this.context=context;
        this.onEnsureClick=onEnsureClick;
        if (((MyApplication)context.getApplication()).database_tableNo==0){
            database_tableNo= DatabaseUtil.getNullDB(context);
        }else{
            database_tableNo=((MyApplication)context.getApplication()).database_tableNo;
        }
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
        CommonAdapter<RoomBean.BianhaoOne> adapter=new CommonAdapter<RoomBean.BianhaoOne>(context,list,R.layout.item_text_multi) {
            @Override
            public void convert(ViewHolder helper, RoomBean.BianhaoOne item, int position) {
                helper.setText(R.id.tv_bianhao,"房屋编号:"+item.scode);
                helper.setText(R.id.tv_content,item.hrsAdress);
                final String  codes=item.scode;
                final String  house_id=item.house_id;
                helper.getView(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog.showSelectDialog(context, "确认注销“" + codes + "”此房屋编号吗？", new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {
                                deleteHouseInfo(codes,house_id);
                            }

                            @Override
                            public void cancel() {
                            }
                        });
                    }
                });
                if (item.sfjz.isEmpty()) {
                    if (item.r_status == 1) {
                        ((TextView) helper.getView(R.id.tv_bianhao)).setTextColor(context.getResources().getColor(R.color.green));
                        ((TextView) helper.getView(R.id.tv_content)).setTextColor(context.getResources().getColor(R.color.green));
                    } else {
                        ((TextView) helper.getView(R.id.tv_bianhao)).setTextColor(context.getResources().getColor(R.color.title2));
                        ((TextView) helper.getView(R.id.tv_content)).setTextColor(context.getResources().getColor(R.color.title2));
                    }
                } else {
                    if (item.sfjz.equals("1")) {
                        ((TextView) helper.getView(R.id.tv_bianhao)).setTextColor(context.getResources().getColor(R.color.green));
                        ((TextView) helper.getView(R.id.tv_content)).setTextColor(context.getResources().getColor(R.color.green));
                    } else {
                        ((TextView) helper.getView(R.id.tv_bianhao)).setTextColor(context.getResources().getColor(R.color.title2));
                        ((TextView) helper.getView(R.id.tv_content)).setTextColor(context.getResources().getColor(R.color.title2));
                    }
                }
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
                intent.putExtra("HOUSE",bhone);
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

    private void deleteHouseInfo(final String scode,final String house_id){
        showProcessDialog("数据提交中...");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","cancel");
        params.addBodyParameter("sqdm", MyInfomationManager.getSQCODE(context));
        params.addBodyParameter("scode",scode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("rr",result);
                XMLParserUtil.parseXMLforReportLoss(result, new XMLParserUtil.OnXmlParserListener() {
                    @Override
                    public void success(String result) {
                        deleteHouseTable(house_id);
                        deleteHouseInfoLocal(scode,house_id);
                        Toast.makeText(context,"注销成功",Toast.LENGTH_SHORT).show();
                        hideProcessDialog();
                    }
                    @Override
                    public void fail(String error) {
                        if ("此出租屋信息不存在！".equals(error)){
                            deleteHouseInfoLocal(scode,house_id);
                            return;
                        }
                        Dialog.showForceDialog(context,"提示",error, new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {

                            }

                            @Override
                            public void cancel() {

                            }
                        });
                    }
                });


            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context,"数据提交失败，请稍后重试",Toast.LENGTH_SHORT).show();
                hideProcessDialog();
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() { hideProcessDialog();}
        });
    }
    private void deleteHouseInfoLocal( String scode,final String house_id){
        RequestParams params=new RequestParams(Constants.URL+"HouseSave.aspx");
        params.addBodyParameter("type","housedel");
        params.addBodyParameter("user_id", MyInfomationManager.getUserID(context));
        params.addBodyParameter("scode",scode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("delete",result);
                try {
                    DeleteRealPeopleBean deleteRealPeopleBean= GsonTools.changeGsonToBean(result,DeleteRealPeopleBean.class);
                    Log.e("delete",deleteRealPeopleBean.data.get(0).success);
                    if (deleteRealPeopleBean.data.get(0).success.equals("true")){
                        deleteHouseTable(house_id);
                        Toast.makeText(context,"注销成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"注销失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context,"注销失败",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() { }
        });
    }
    private void deleteHouseTable(String house_id){
        try {
            dbUtils= DeviceUtils.getDbUtils(context);
            switch (database_tableNo){
                case 1:
                    dbUtils.update(new HouseAddressOldBean(house_id,"8"),"source_id");
                    break;
                case 2:
                    dbUtils.update(new HouseAddressOldBean2(house_id,"8"),"source_id");
                    break;
                case 3:
                    dbUtils.update(new HouseAddressOldBean3(house_id,"8"),"source_id");
                    break;
                case 4:
                    dbUtils.update(new HouseAddressOldBean4(house_id,"8"),"source_id");
                    break;
                case 5:
                    dbUtils.update(new HouseAddressOldBean5(house_id,"8"),"source_id");
                    break;
                case 6:
                    dbUtils.update(new HouseAddressOldBean6(house_id,"8"),"source_id");
                    break;
                case 7:
                    dbUtils.update(new HouseAddressOldBean7(house_id,"8"),"source_id");
                    break;
                case 8:
                    dbUtils.update(new HouseAddressOldBean8(house_id,"8"),"source_id");
                    break;
                case 9:
                    dbUtils.update(new HouseAddressOldBean9(house_id,"8"),"source_id");
                    break;
                case 10:
                    dbUtils.update(new HouseAddressOldBean10(house_id,"8"),"source_id");
                    break;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public void showProcessDialog(String msg) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.show();
    }
    public void hideProcessDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}