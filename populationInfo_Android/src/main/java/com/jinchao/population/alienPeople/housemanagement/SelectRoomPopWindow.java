package com.jinchao.population.alienPeople.housemanagement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.adapter.NfcPopIndicatorAdapter;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.utils.XMLParserUtil;
import com.jinchao.population.widget.LoadingView;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/3/19.
 */

public class SelectRoomPopWindow extends PopupWindow{
    public interface OnEnsureClickListener{
        void  OnEnSureClick(String str);
    }
    private GridView gv;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private LoadingView loadingView;
    private OnEnsureClickListener onEnsureClickListener;
    String scode="";
    private Context context;
    public SelectRoomPopWindow(Activity context,String code, final OnEnsureClickListener onEnsureClickListener) {
        super();
        this.scode=code;
        this.context=context;
        this.onEnsureClickListener = onEnsureClickListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView=inflater.inflate(R.layout.pop_select_room, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        gv =(GridView) mMenuView.findViewById(R.id.gv);
        loadingView=(LoadingView) mMenuView.findViewById(R.id.loadingview);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String code=(String)(((GridView)adapterView).getItemAtPosition(i));
                if (code.trim().equals("不选")){
                    code="";
                }
                onEnsureClickListener.OnEnSureClick(code);
                SelectRoomPopWindow.this.dismiss();
            }
        });
        getRooms();
        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.update();
    }
    private void processData(List<String> list){
        list.add(0,"不选");
        CommonAdapter<String> adapter=new CommonAdapter<String>(context,list,R.layout.item_room) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                helper.setText(R.id.textview,item);
            }
        };
        gv.setAdapter(adapter);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }
    private void getRooms(){
        loadingView.loading();
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","searchRoom");
        params.addBodyParameter("sqdm", MyInfomationManager.getSQCODE(context));
        params.addBodyParameter("scode",scode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                loadingView.loadComplete();
                List<String> rooms= XMLParserUtil.parseXMLtoRooms(result);
                processData(rooms);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadingView.reload(new LoadingView.OnReloadClickListener() {
                    @Override
                    public void onReload() {
                        getRooms();
                    }
                });
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {
            }
        });
    }
}
