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
import com.jinchao.population.R;
import com.jinchao.population.alienPeople.registchangelogoff.PeoplesInHouseActivity;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddress;
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
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.KeyBoardUtils;
import com.jinchao.population.view.Dialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/1/19.
 */

public class SearchFragment extends DialogFragment implements DialogInterface.OnKeyListener, View.OnClickListener {
    public static final String TAG = "SearchFragment";
    private ImageView ivSearchBack;
    private EditText etSearchKeyword;
    private ImageView ivSearchSearch;
    private ListView lv;
    private TextView tv_search_clean;
    private int database_tableNo=0;
    DbUtils dbUtils;
    public static SearchFragment newInstance(){
        Bundle bundle = new Bundle();
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        return searchFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
        if (((MyApplication)getActivity().getApplication()).database_tableNo==0){
            database_tableNo= DatabaseUtil.getNullDB(getActivity());
        }else{
            database_tableNo=((MyApplication)getActivity().getApplication()).database_tableNo;
        }
        dbUtils= DeviceUtils.getDbUtils(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
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
        View view = inflater.inflate(R.layout.dialog_search, container, false);
        ivSearchBack = (ImageView) view.findViewById(R.id.iv_search_back);
        etSearchKeyword = (EditText) view.findViewById(R.id.et_search_keyword);
        ivSearchSearch = (ImageView) view.findViewById(R.id.iv_search_search);
        tv_search_clean= (TextView) view.findViewById(R.id.tv_search_clean);
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
            String keyword = editable.toString();
            if (!TextUtils.isEmpty(keyword.trim())) {
                try {
                    switch (database_tableNo){
                        case 0:

                            break;
                        case 1:
                            List<HouseAddressOldBean> list1=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list1 = new ArrayList<>();
                            } else {
                                list1 = dbUtils.findAll(Selector.from(HouseAddressOldBean.class).where("scode", "like", "%" + keyword + "%"));
                                if (list1.size() == 0)
                                    list1 = dbUtils.findAll(Selector.from(HouseAddressOldBean.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list1==null)list1=new ArrayList<>();
                            if (list1.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean> adapter1=new CommonAdapter<HouseAddressOldBean>(getActivity(),list1,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter1);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean houseAddressOldBean=(HouseAddressOldBean)((ListView)parent).getItemAtPosition(position);
                                    Intent intent =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean.scode, houseAddressOldBean.idcard, houseAddressOldBean.idcard, houseAddressOldBean.address, "", "");
                                    intent.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent);
                                    hideAnim();
                                }
                            });
                            break;
                        case 2:
                            List<HouseAddressOldBean2> list2=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list2 = new ArrayList<>();
                            } else {
                                list2 = dbUtils.findAll(Selector.from(HouseAddressOldBean2.class).where("scode", "like", "%" + keyword + "%"));
                                if (list2.size() == 0)
                                    list2 = dbUtils.findAll(Selector.from(HouseAddressOldBean2.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list2==null)list2=new ArrayList<>();
                            if (list2.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean2> adapter2=new CommonAdapter<HouseAddressOldBean2>(getActivity(),list2,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean2 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter2);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean2 houseAddressOldBean2=(HouseAddressOldBean2)((ListView)parent).getItemAtPosition(position);
                                    Intent intent2 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean2.scode, houseAddressOldBean2.idcard, houseAddressOldBean2.idcard, houseAddressOldBean2.address, "", "");
                                    intent2.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent2);
                                    hideAnim();
                                }
                            });
                            break;
                        case 3:
                            List<HouseAddressOldBean3> list3=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list3 = new ArrayList<>();
                            }else {
                                list3 = dbUtils.findAll(Selector.from(HouseAddressOldBean3.class).where("scode", "like", "%" + keyword + "%"));
                                if (list3.size() == 0)list3 = dbUtils.findAll(Selector.from(HouseAddressOldBean3.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list3==null)list3=new ArrayList<>();
                            if (list3.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean3> adapter3=new CommonAdapter<HouseAddressOldBean3>(getActivity(),list3,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean3 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter3);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean3 houseAddressOldBean3=(HouseAddressOldBean3)((ListView)parent).getItemAtPosition(position);
                                    Intent intent3 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean3.scode, houseAddressOldBean3.idcard, houseAddressOldBean3.idcard, houseAddressOldBean3.address, "", "");
                                    intent3.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent3);
                                    hideAnim();
                                }
                            });
                            break;
                        case 4:
                            List<HouseAddressOldBean4> list4=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list4 = new ArrayList<>();
                            }else {
                                list4 = dbUtils.findAll(Selector.from(HouseAddressOldBean4.class).where("scode", "like", "%" + keyword + "%"));
                                if (list4.size() == 0)list4 = dbUtils.findAll(Selector.from(HouseAddressOldBean4.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list4==null)list4=new ArrayList<>();
                            if (list4.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean4> adapter4=new CommonAdapter<HouseAddressOldBean4>(getActivity(),list4,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean4 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter4);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean4 houseAddressOldBean4=(HouseAddressOldBean4)((ListView)parent).getItemAtPosition(position);
                                    Intent intent4 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean4.scode, houseAddressOldBean4.idcard, houseAddressOldBean4.idcard, houseAddressOldBean4.address, "", "");
                                    intent4.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent4);
                                    hideAnim();
                                }
                            });
                            break;
                        case 5:
                            List<HouseAddressOldBean5> list5=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list5 = new ArrayList<>();
                            }else {
                                list5 = dbUtils.findAll(Selector.from(HouseAddressOldBean5.class).where("scode", "like", "%" + keyword + "%"));
                                if (list5.size() == 0)list5 = dbUtils.findAll(Selector.from(HouseAddressOldBean5.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list5==null)list5=new ArrayList<>();
                            if (list5.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean5> adapter5=new CommonAdapter<HouseAddressOldBean5>(getActivity(),list5,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean5 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter5);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean5 houseAddressOldBean5=(HouseAddressOldBean5)((ListView)parent).getItemAtPosition(position);
                                    Intent intent5 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean5.scode, houseAddressOldBean5.idcard, houseAddressOldBean5.idcard, houseAddressOldBean5.address, "", "");
                                    intent5.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent5);
                                    hideAnim();
                                }
                            });
                            break;
                        case 6:
                            List<HouseAddressOldBean6> list6=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list6 = new ArrayList<>();
                            }else {
                                list6 = dbUtils.findAll(Selector.from(HouseAddressOldBean6.class).where("scode", "like", "%" + keyword + "%"));
                                if (list6.size() == 0)list6 = dbUtils.findAll(Selector.from(HouseAddressOldBean6.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list6==null)list6=new ArrayList<>();
                            if (list6.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean6> adapter6=new CommonAdapter<HouseAddressOldBean6>(getActivity(),list6,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean6 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter6);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean6 houseAddressOldBean6=(HouseAddressOldBean6)((ListView)parent).getItemAtPosition(position);
                                    Intent intent6 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean6.scode, houseAddressOldBean6.idcard, houseAddressOldBean6.idcard, houseAddressOldBean6.address, "", "");
                                    intent6.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent6);
                                    hideAnim();
                                }
                            });
                            break;
                        case 7:
                            List<HouseAddressOldBean7> list7=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list7 = new ArrayList<>();
                            }else {
                                list7 = dbUtils.findAll(Selector.from(HouseAddressOldBean7.class).where("scode", "like", "%" + keyword + "%"));
                                if (list7.size() == 0)list7 = dbUtils.findAll(Selector.from(HouseAddressOldBean7.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list7==null)list7=new ArrayList<>();
                            if (list7.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean7> adapter7=new CommonAdapter<HouseAddressOldBean7>(getActivity(),list7,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean7 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter7);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean7 houseAddressOldBean7=(HouseAddressOldBean7)((ListView)parent).getItemAtPosition(position);
                                    Intent intent7 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean7.scode, houseAddressOldBean7.idcard, houseAddressOldBean7.idcard, houseAddressOldBean7.address, "", "");
                                    intent7.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent7);
                                    hideAnim();
                                }
                            });
                            break;
                        case 8:
                            List<HouseAddressOldBean8> list8=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list8 = new ArrayList<>();
                            }else {
                                list8 = dbUtils.findAll(Selector.from(HouseAddressOldBean8.class).where("scode", "like", "%" + keyword + "%"));
                                if (list8.size() == 0)list8 = dbUtils.findAll(Selector.from(HouseAddressOldBean8.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list8==null)list8=new ArrayList<>();
                            if (list8.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean8> adapter8=new CommonAdapter<HouseAddressOldBean8>(getActivity(),list8,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean8 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter8);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean8 houseAddressOldBean8=(HouseAddressOldBean8)((ListView)parent).getItemAtPosition(position);
                                    Intent intent8 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean8.scode, houseAddressOldBean8.idcard, houseAddressOldBean8.idcard, houseAddressOldBean8.address, "", "");
                                    intent8.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent8);
                                    hideAnim();
                                }
                            });
                            break;
                        case 9:
                            List<HouseAddressOldBean9> list9=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list9 = new ArrayList<>();
                            }else {
                                list9 = dbUtils.findAll(Selector.from(HouseAddressOldBean9.class).where("scode", "like", "%" + keyword + "%"));
                                if (list9.size() == 0)list9 = dbUtils.findAll(Selector.from(HouseAddressOldBean9.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list9==null)list9=new ArrayList<>();
                            if (list9.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean9> adapter9=new CommonAdapter<HouseAddressOldBean9>(getActivity(),list9,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean9 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter9);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean9 houseAddressOldBean9=(HouseAddressOldBean9)((ListView)parent).getItemAtPosition(position);
                                    Intent intent9 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean9.scode, houseAddressOldBean9.idcard, houseAddressOldBean9.idcard, houseAddressOldBean9.address, "", "");
                                    intent9.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent9);
                                    hideAnim();
                                }
                            });
                            break;
                        case 10:
                            List<HouseAddressOldBean10> list10=new ArrayList<>();
                            if (TextUtils.isEmpty(keyword.trim())) {
                                list10 = new ArrayList<>();
                            }else {
                                list10 = dbUtils.findAll(Selector.from(HouseAddressOldBean10.class).where("scode", "like", "%" + keyword + "%"));
                                if (list10.size() == 0)list10 = dbUtils.findAll(Selector.from(HouseAddressOldBean10.class).where("address", "like", "%" + keyword + "%"));
                            }
                            if (list10==null)list10=new ArrayList<>();
                            if (list10.size()==0){
                                tv_search_clean.setText("无此房屋编号");
                            }else{
                                tv_search_clean.setText("自动匹配房屋编号");
                            }
                            CommonAdapter<HouseAddressOldBean10> adapter10=new CommonAdapter<HouseAddressOldBean10>(getActivity(),list10,R.layout.item_text) {
                                @Override
                                public void convert(ViewHolder helper, HouseAddressOldBean10 item, int position) {
                                    helper.setText(R.id.tv_content, item.address);
                                    if (item.source_id.equals("8")){
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#9c070f"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode+"【休眠】");
                                    }else{
                                        ((TextView)helper.getView(R.id.tv_bianhao)).setTextColor(Color.parseColor("#666666"));
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                }
                            };
                            lv.setAdapter(adapter10);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HouseAddressOldBean10 houseAddressOldBean10=(HouseAddressOldBean10)((ListView)parent).getItemAtPosition(position);
                                    Intent intent10 =new Intent(getActivity(),PeoplesInHouseActivity.class);
                                    NFCJsonBean nfcJsonBean=new NFCJsonBean(houseAddressOldBean10.scode, houseAddressOldBean10.idcard, houseAddressOldBean10.idcard, houseAddressOldBean10.address, "", "");
                                    intent10.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                                    getActivity().startActivity(intent10);
                                    hideAnim();
                                }
                            });
                            break;
                    }
                } catch (NullPointerException e){
                    Dialog.showForceDialog(getActivity(), "提示", "未检测到地址库，请全库地址下载！", new Dialog.DialogClickListener() {
                        @Override
                        public void confirm() {}
                        @Override
                        public void cancel() {}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
        String searchKey = etSearchKeyword.getText().toString();
        if (TextUtils.isEmpty(searchKey.trim())|| !CommonUtils.isFangwuBianHao(searchKey.trim())) {
            Toast.makeText(getContext(), "请输入6位房屋编号", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(),"请直接点击列表中的查询结果！",Toast.LENGTH_SHORT).show();
//            Intent intent =new Intent(getActivity(),PeopleListinHouseActivity.class);
//            intent.putExtra("housecode",searchKey);
//            getActivity().startActivity(intent);
//            hideAnim();
        }
    }
}
