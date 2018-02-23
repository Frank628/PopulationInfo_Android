package com.jinchao.population.alienPeople;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.jinchao.population.entity.EventBusString;
import com.jinchao.population.entity.LZHResult;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.entity.JLXResult;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.KeyBoardUtils;
import com.jinchao.population.view.Dialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by OfferJiShu01 on 2017/1/19.
 */

public class SearchAddressFragment extends DialogFragment implements DialogInterface.OnKeyListener, View.OnClickListener {
    public static final String TAG = "SearchFragment";
    private ImageView ivSearchBack;
    private EditText etSearchKeyword;
    private ImageView ivSearchSearch;
    private ListView lv;
    private TextView tv_search_clean,tv_address;
    private EventBusString objCurrent;
    private String json;
    public static SearchAddressFragment newInstance(EventBusString obj){
        Bundle bundle = new Bundle();
        SearchAddressFragment searchFragment = new SearchAddressFragment();
        bundle.putSerializable(TAG, obj);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
        objCurrent=(EventBusString)getArguments().get(TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
        if(objCurrent.type==1){
            getLZHData(objCurrent.address);
        }
    }

    private void initDialog() {
        Window window = getDialog().getWindow();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels * 0.98); //DialogSearch的宽
        window.setLayout(width, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.TOP);
    }
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            hideAnim();
        } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            search();
        }
        return false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search_address, container, false);
        ivSearchBack = (ImageView) view.findViewById(R.id.iv_search_back);
        etSearchKeyword = (EditText) view.findViewById(R.id.et_search_keyword);
        ivSearchSearch = (ImageView) view.findViewById(R.id.iv_search_search);
        tv_search_clean= (TextView) view.findViewById(R.id.tv_search_clean);
        tv_address= (TextView) view.findViewById(R.id.tv_address);
        lv=(ListView)view.findViewById(R.id.lv);
        getDialog().setOnKeyListener(this);//键盘按键监听
        etSearchKeyword.addTextChangedListener(new TextWatcherImpl());
        ivSearchBack.setOnClickListener(this);
        ivSearchSearch.setOnClickListener(this);
        etSearchKeyword.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                etSearchKeyword.getViewTreeObserver().removeOnPreDrawListener(this);
                if (isVisible()) {
                    KeyBoardUtils.openKeyboard(getContext(), etSearchKeyword);
                }
                return true;
            }
        });
        if(objCurrent.type==0){
            etSearchKeyword.setHint("请输入街路巷（小区名）");
        }else if(objCurrent.type==1){
            etSearchKeyword.setHint("请输入楼幢号（或单元）");

        }
        return view;
    }
    /**
     * 监听编辑框文字改变
     */
    private class TextWatcherImpl implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String keyword = editable.toString().trim();
            if(objCurrent.type==0) {
                if ((!TextUtils.isEmpty(keyword)) && keyword.length() >= 2) {
                    getJLXData(keyword);
                }
            }else if(objCurrent.type==1){
                    findLZH(keyword,json);
            }
        }
    }
    private void getJLXData(String str){
        RequestParams params=new RequestParams(Constants.URL+"HousePeople.aspx");
        params.addBodyParameter("type","getjlxName");
        params.addBodyParameter("jlx_name",str);
        params.addBodyParameter("sq_id",MyInfomationManager.getSQID(getActivity()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("tag",result);
                processJLXData(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {

            }
        });
    }
    private void getLZHData(String str){
        RequestParams params=new RequestParams(Constants.URL+"HousePeople.aspx");
        params.addBodyParameter("type","getadr");
        params.addBodyParameter("jlx_name",str);
        params.addBodyParameter("sq_id",MyInfomationManager.getSQID(getActivity()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("tag",result);
                json=result;
                processLZHData(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {

            }
        });
    }
    private void processLZHData(String json){
        LZHResult lzh= GsonTools.changeGsonToBean(json,LZHResult.class);
        if(!lzh.data.isEmpty()){
            lv.setVisibility(View.VISIBLE);
            CommonAdapter<LZHResult.LZHOne> adapter=new CommonAdapter<LZHResult.LZHOne>(getActivity(),lzh.data,R.layout.item_text_address) {
                @Override
                public void convert(ViewHolder helper, LZHResult.LZHOne item, int position) {
                    helper.setText(R.id.tv_content,item.adr);
                }
            };
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LZHResult.LZHOne one =(LZHResult.LZHOne)((ListView)parent).getItemAtPosition(position);
                    EventBus.getDefault().post(new EventBusString(1,one.adr));
                    hideAnim();
                }
            });
        }else{

        }
    }
    private void findLZH(String keyword,String json){
        try {
            LZHResult lzh= GsonTools.changeGsonToBean(json,LZHResult.class);
            if(!lzh.data.isEmpty()){
                lv.setVisibility(View.VISIBLE);
                List<LZHResult.LZHOne> list =new ArrayList<>();
                for (int i=0;i<lzh.data.size();i++){
                    if(lzh.data.get(i).adr.contains(keyword)){
                        list.add(lzh.data.get(i));
                    }
                }
                CommonAdapter<LZHResult.LZHOne> adapter=new CommonAdapter<LZHResult.LZHOne>(getActivity(),list,R.layout.item_text_address) {
                    @Override
                    public void convert(ViewHolder helper, LZHResult.LZHOne item, int position) {
                        helper.setText(R.id.tv_content,item.adr);
                    }
                };
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LZHResult.LZHOne one =(LZHResult.LZHOne)((ListView)parent).getItemAtPosition(position);
                        EventBus.getDefault().post(new EventBusString(1,one.adr));
                        hideAnim();
                    }
                });
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void processJLXData(String json){
        JLXResult jlx= GsonTools.changeGsonToBean(json,JLXResult.class);
        if(!jlx.data.isEmpty()){
            lv.setVisibility(View.VISIBLE);
                CommonAdapter<JLXResult.JLXOne> adapter=new CommonAdapter<JLXResult.JLXOne>(getActivity(),jlx.data,R.layout.item_text_address) {
                    @Override
                    public void convert(ViewHolder helper, JLXResult.JLXOne item, int position) {
                        helper.setText(R.id.tv_content,item.jlx);
                    }
                };
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        JLXResult.JLXOne one =(JLXResult.JLXOne)((ListView)parent).getItemAtPosition(position);
                        EventBus.getDefault().post(new EventBusString(0,one.jlx));
                        hideAnim();
                    }
                });
        }else{

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_search_back || view.getId() == R.id.view_search_outside) {
            hideAnim();
        } else if (view.getId() == R.id.iv_search_search) {
            search();
        }
    }
    private void hideAnim() {
        KeyBoardUtils.closeKeyboard(getContext(), etSearchKeyword);
        dismiss();
        etSearchKeyword.setText("");
    }
    private void search() {
        String searchKey = etSearchKeyword.getText().toString().trim();
        if (TextUtils.isEmpty(searchKey.trim())) {
            Toast.makeText(getContext(), "请输入至少两个字符", Toast.LENGTH_SHORT).show();
        } else {
            getJLXData(searchKey);
        }
    }
}
