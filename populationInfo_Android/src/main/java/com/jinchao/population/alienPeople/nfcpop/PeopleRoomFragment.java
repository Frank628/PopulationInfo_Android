package com.jinchao.population.alienPeople.nfcpop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.adapter.NfcPopIndicatorAdapter;
import com.jinchao.population.alienPeople.PeopleListinHouseActivity;
import com.jinchao.population.alienPeople.housemanagement.NFCReadPeopleInHouseActivity;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.entity.SortbyTimeRenyuanInhouseOneClass;
import com.jinchao.population.mainmenu.SearchPeopleDetailActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.XMLParserUtil;
import com.jinchao.population.widget.LoadingView;
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
public class PeopleRoomFragment extends LazyFragment {
    private static String KEY="JSON";
    private NFCJsonBean nfcJsonBean;
    private ListView lv;
    private LoadingView loadingView;
    private EditText edt_idcard;
    String room="",resultJson="";
    public static PeopleRoomFragment newInstance(NFCJsonBean nfcJsonBean,String room){
        PeopleRoomFragment peopleRoomFragment=new PeopleRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, nfcJsonBean);
        bundle.putString("room",room);
        peopleRoomFragment.setArguments(bundle);
        return peopleRoomFragment;
    }


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_peopleinroom);
        nfcJsonBean=(NFCJsonBean)getArguments().getSerializable(KEY);
        room=getArguments().getString("room");
        lv=(ListView) findViewById(R.id.lv);
        loadingView=(LoadingView) findViewById(R.id.loadingview);
        edt_idcard=(EditText) findViewById(R.id.edt_idcard);
        getData(room,nfcJsonBean);
        edt_idcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(resultJson))processData(resultJson);
            }
        });
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        if (!TextUtils.isEmpty(resultJson)){
            processData(resultJson);
        }
    }

    private void getData(final String room, final NFCJsonBean nfcJsonBean){
        loadingView.loading();
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","peoplebyfjh");
        params.addBodyParameter("sqdm", MyInfomationManager.getSQCODE(getActivity()));
        params.addBodyParameter("scode",nfcJsonBean.code);
        params.addBodyParameter("fjh",room);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                resultJson=result;
                processData(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadingView.reload(new LoadingView.OnReloadClickListener() {
                    @Override
                    public void onReload() {
                        getData(room,nfcJsonBean);
                    }
                });
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() { }
        });
    }
    private void processData(String json){
        try {
            RenyuanInHouseBean renyuanInHouseBean= GsonTools.changeGsonToBean(json,RenyuanInHouseBean.class);
            if (renyuanInHouseBean.data.house_exist.equals("0")){
                loadingView.empty("无人居住");
                return;
            }
            if (renyuanInHouseBean.data.people_exist.equals("0")){
                loadingView.empty("无人居住");
                return;
            }
            List<RenyuanInHouseBean.RenyuanInhouseOne> list=new ArrayList<>();
            String str=edt_idcard.getText().toString().trim();
            for (int i=0;i<renyuanInHouseBean.data.peoplelist.size();i++){
                if (renyuanInHouseBean.data.peoplelist.get(i).idcard.contains(str)){
                    list.add(renyuanInHouseBean.data.peoplelist.get(i));
                }
            }
            if ((!TextUtils.isEmpty(str))&&list.size()==0){
                loadingView.empty("没有符合搜索条件的人员~");
            }else{
                loadingView.loadComplete();
            }
            Collections.sort(list,new SortbyTimeRenyuanInhouseOneClass());
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
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    RenyuanInHouseBean.RenyuanInhouseOne renyuanInHouseone=(RenyuanInHouseBean.RenyuanInhouseOne) ((ListView)parent).getItemAtPosition(position);
                    Intent intent =new Intent(getActivity(), SearchPeopleDetailActivity.class);
                    intent.putExtra("renyuan", renyuanInHouseone);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            loadingView.reload("服务器返回数据有误", new LoadingView.OnReloadClickListener() {
                @Override
                public void onReload() {
                    getData(room,nfcJsonBean);
                }
            });
            e.printStackTrace();
        }
    }

}
