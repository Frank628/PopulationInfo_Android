package com.jinchao.population.alienPeople.nfcpop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.alienPeople.housemanagement.NFCReadPeopleInHouseActivity;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.entity.SortbyRoomCodeRenyuanInhouseOneClass;
import com.jinchao.population.entity.SortbyTimeRenyuanInhouseOneClass;
import com.jinchao.population.mainmenu.SearchPeopleDetailActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.widget.LoadingView;
import com.jinchao.population.widget.emptyview.HHEmptyView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.shizhefei.fragment.LazyFragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 2017/3/21.
 */
public class AllPeopleFragment extends LazyFragment {
    private static String KEY="JSON";
    private NFCJsonBean nfcJsonBean;
    private ListView lv;
    private LoadingView loadingView;
    private EditText edt_idcard;
    private RadioGroup rg_sort;
    private String resultJson="";
    private RadioButton rb_roomcode;
    public static AllPeopleFragment newInstance(NFCJsonBean json){
        AllPeopleFragment allPeopleFragment=new AllPeopleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, json);
        allPeopleFragment.setArguments(bundle);
        return allPeopleFragment;
    }


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_all);
        nfcJsonBean=(NFCJsonBean)getArguments().getSerializable(KEY);
        lv=(ListView) findViewById(R.id.lv);
        loadingView=(LoadingView) findViewById(R.id.loadingview);
        edt_idcard=(EditText) findViewById(R.id.edt_idcard);
        rg_sort=(RadioGroup) findViewById(R.id.rg_sort);
        rb_roomcode=(RadioButton) findViewById(R.id.rb_roomcode);
        rg_sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_roomcode:
                        if (!TextUtils.isEmpty(resultJson)) {
                            processData(resultJson);
                        }
                        break;
                    case R.id.rb_time:
                        if (!TextUtils.isEmpty(resultJson)) {
                            processData(resultJson);
                        }
                        break;
                }
            }
        });
        edt_idcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                processData(resultJson);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                RenyuanInHouseBean.RenyuanInhouseOne renyuanInHouseone=(RenyuanInHouseBean.RenyuanInhouseOne) ((ListView)parent).getItemAtPosition(position);
                Intent intent =new Intent(getActivity(), SearchPeopleDetailActivity.class);
                intent.putExtra("renyuan", renyuanInHouseone);
                startActivity(intent);
            }
        });
        getData(nfcJsonBean);
    }




    private void getData(final NFCJsonBean nfcJsonBean){
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","peopleList");
        params.addBodyParameter("sqdm",MyInfomationManager.getSQCODE(getActivity()));
        params.addBodyParameter("scode",nfcJsonBean.code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("allmp",result);
                resultJson=result;
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadingView.reload(new LoadingView.OnReloadClickListener() {
                    @Override
                    public void onReload() {
                        getData(nfcJsonBean);
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) { }

            @Override
            public void onFinished() {

            }
        });
    }
     private void processData(String  result){
        try {
            RenyuanInHouseBean renyuanInHouseBean= GsonTools.changeGsonToBean(result,RenyuanInHouseBean.class);
            if (renyuanInHouseBean.data.house_exist.equals("0")){
                loadingView.empty("无人居住");
                return;
            }
            if (renyuanInHouseBean.data.people_exist.equals("0")){
                loadingView.empty("无人居住");
                return;
            }

            List<RenyuanInHouseBean.RenyuanInhouseOne> list=new ArrayList<RenyuanInHouseBean.RenyuanInhouseOne>();
            if(!TextUtils.isEmpty(edt_idcard.getText().toString().trim())){
                for (int i = 0; i <renyuanInHouseBean.data.peoplelist.size(); i++) {
                    if (renyuanInHouseBean.data.peoplelist.get(i).idcard.trim().contains(edt_idcard.getText().toString().trim())){
                        list.add(renyuanInHouseBean.data.peoplelist.get(i));
                    }
                }
                if (list.size()==0){
                    loadingView.empty("没有符合搜索条件的人员~");
                }else {
                    loadingView.loadComplete();
                }
            }else {
                loadingView.loadComplete();
                list.addAll(renyuanInHouseBean.data.peoplelist);
            }
            if (rb_roomcode.isChecked()){
                Collections.sort(list,new SortbyRoomCodeRenyuanInhouseOneClass());
            }else{
                Collections.sort(list,new SortbyTimeRenyuanInhouseOneClass());
            }
            CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne> adapter =new CommonAdapter<RenyuanInHouseBean.RenyuanInhouseOne>(getActivity(),list,R.layout.item_renyuan) {
                @Override
                public void convert(ViewHolder helper, RenyuanInHouseBean.RenyuanInhouseOne item, int position) {
                    helper.setText(R.id.tv_name,"姓名: "+ item.sname);
                    DbUtils dbUtils = DeviceUtils.getDbUtils(getActivity());
                    People people = null;
                    try {
                        people=dbUtils.findFirst(Selector.from(People.class).where("cardno", "=", item.idcard.trim()));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    if (people!=null){
                        if (people.module.equals("变更")) {
                            helper.setText(R.id.tv_status, "【延期】");
                        }else if (people.module.equals("注销")) {
                            helper.setText(R.id.tv_status, "【注销】");
                        }else{

                            String[] split = item.write_time.split("\\s+");
                            if (split.length>1) {
                                if (CommonUtils.isBigerthanElevenandSmallthanTwelve(split[0])) {
                                    helper.setText(R.id.tv_status, "【"+item.resdients_status+"?】");
                                }else{
                                    helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
                                }
                            }else{
                                helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
                            }
                        }
                    }else{
                        String[] split = item.write_time.split("\\s+");
                        if (split.length>1) {
                            if (CommonUtils.isBigerthanElevenandSmallthanTwelve(split[0])) {
                                helper.setText(R.id.tv_status, "【"+item.resdients_status+"?】");
                            }else{
                                helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
                            }
                        }else{
                            helper.setText(R.id.tv_status, "【"+item.resdients_status+"】");
                        }
                    }
                    helper.setText(R.id.tv_shihao, "室号: "+item.shihao);
                    helper.setText(R.id.tv_time, item.write_time);
                }
            };
            lv.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
