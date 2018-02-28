package com.jinchao.population.alienPeople;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.alienPeople.registchangelogoff.PeoplesInHouseActivity;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.EventBusString;
import com.jinchao.population.entity.JLXResult;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.entity.RoomBean;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.view.MultiRoomPop;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.NoRoomPop;
import com.jinchao.population.widget.LoadingView;
import com.jinchao.population.widget.ValidateEidtText;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.io.ByteArrayOutputStream;

import de.greenrobot.event.EventBus;

/**
 * Created by OfferJiShu01 on 2017/1/19.
 */
@ContentView(R.layout.activity_searchbyaddress)
public class SearchByAddressActivity extends BaseReaderActiviy {
    @ViewInject(R.id.edt_jlx) EditText edt_jlx;
    @ViewInject(R.id.edt_lzh) EditText edt_lzh;
    @ViewInject(R.id.loadingview) LoadingView loadingview;
    @ViewInject(R.id.gv) GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("按地名查询");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        EventBus.getDefault().register(this);
    }


    @Event(value={R.id.edt_jlx})
    private void jlxclick(View view){
        SearchAddressFragment.newInstance(new EventBusString(0,"")).show(getSupportFragmentManager(),SearchFragment.TAG);
    }
    @Event(value={R.id.edt_lzh})
    private void lzhclick(View view){
        String jlx=edt_jlx.getText().toString().trim();
        if(TextUtils.isEmpty(jlx)){
           Toast.makeText(this,"请先搜索街路巷（小区）！",Toast.LENGTH_SHORT).show();
        }else{
            SearchAddressFragment.newInstance(new EventBusString(1,jlx)).show(getSupportFragmentManager(),SearchFragment.TAG);
        }
    }
    public void onEventMainThread(EventBusString one){
        if(one.type==0){
            edt_jlx.setText(one.address);
            edt_lzh.setText("");
            edt_lzh.requestFocus();
        }else if(one.type==1){
            edt_lzh.setText(one.address);
            gv.setAdapter(null);
            getData(edt_jlx.getText().toString().trim(),one.address);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void getData(final String jlx,final String lzh){
        loadingview.loading();
//        RequestParams params=new RequestParams(Constants.URL+"HousePeople.aspx?type=getroom&jlx_name=周市镇永平花园&sq_id=4&adr=41号楼");
        RequestParams params=new RequestParams(Constants.URL+"HousePeople.aspx?type=getroom&jlx_name="+jlx+"&sq_id="+MyInfomationManager.getSQID(this)+"&adr="+lzh);
//        RequestParams params=new RequestParams(Constants.URL+"HousePeople.aspx");
//        params.addBodyParameter("type","getroom");
//        params.addBodyParameter("jlx_name",jlx);
//        params.addBodyParameter("adr",lzh);
//        params.addBodyParameter("sq_id", MyInfomationManager.getSQID(this));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("tag",result);
                processData(result,jlx,lzh);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadingview.reload("服务器接口异常！", new LoadingView.OnReloadClickListener() {
                    @Override
                    public void onReload() {
                        getData(jlx,lzh);
                    }
                });
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {
                loadingview.loadComplete();
            }
        });
    }
    private void processData(String json,final String jlx,final String lzh){
        try {
            RoomBean rooms= GsonTools.changeGsonToBean(json,RoomBean.class);
            if(rooms.data.size()==1){
                if(TextUtils.isEmpty(rooms.data.get(0).room)){
                    NoRoomPop pop1 =new NoRoomPop(SearchByAddressActivity.this, rooms.data.get(0).hourseData, new NoRoomPop.OnEnsureClick() {
                        @Override
                        public void onClick(boolean isEnsure) {}
                    });
                    pop1.showPopupWindow(findViewById(R.id.root),0,0);
                    return;
                }
            }
            CommonAdapter<RoomBean.RoomOne> adapter=new CommonAdapter<RoomBean.RoomOne>(SearchByAddressActivity.this,rooms.data,R.layout.item_roomnum) {
                @Override
                public void convert(ViewHolder helper, RoomBean.RoomOne item, int position) {
                    helper.setText(R.id.textview,item.room);
                    if(item.tot>1){
                        ((TextView)helper.getView(R.id.textview)).setBackgroundResource(R.drawable.roomnum_bg_red);
                        ((TextView)helper.getView(R.id.textview)).setTextColor(getResources().getColor(R.color.white));
                    }else{
                        if(item.hourseData.get(0)!=null){
                            if(item.hourseData.get(0).sfjz.isEmpty()) {
                                if (item.hourseData.get(0).r_status == 1) {
                                    ((TextView) helper.getView(R.id.textview)).setTextColor(getResources().getColor(R.color.white));
                                    ((TextView) helper.getView(R.id.textview)).setBackgroundResource(R.drawable.roomnum_bg_green);
                                } else {
                                    ((TextView) helper.getView(R.id.textview)).setTextColor(getResources().getColor(R.color.title2));
                                    ((TextView) helper.getView(R.id.textview)).setBackgroundResource(R.drawable.roomnum_bg);
                                }
                            }else{
                                if (item.hourseData.get(0).sfjz.equals("1")) {
                                    ((TextView) helper.getView(R.id.textview)).setTextColor(getResources().getColor(R.color.white));
                                    ((TextView) helper.getView(R.id.textview)).setBackgroundResource(R.drawable.roomnum_bg_green);
                                } else {
                                    ((TextView) helper.getView(R.id.textview)).setTextColor(getResources().getColor(R.color.title2));
                                    ((TextView) helper.getView(R.id.textview)).setBackgroundResource(R.drawable.roomnum_bg);
                                }
                            }
                        }else{
                            ((TextView)helper.getView(R.id.textview)).setTextColor(getResources().getColor(R.color.title2));
                            ((TextView)helper.getView(R.id.textview)).setBackgroundResource(R.drawable.roomnum_bg);
                        }
                    }
                }
            };
            gv.setAdapter(adapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RoomBean.RoomOne one =(RoomBean.RoomOne)((GridView)parent).getItemAtPosition(position);
                    if(one.tot>1){
                        MultiRoomPop pop =new MultiRoomPop(SearchByAddressActivity.this, one.hourseData, new MultiRoomPop.OnEnsureClick() {
                            @Override
                            public void onClick(boolean isEnsure) {}
                        });
                        pop.showPopupWindow(findViewById(R.id.root),0,0);
                    }else{
                        RoomBean.BianhaoOne bhone=one.hourseData.get(0);
                        Intent intent =new Intent(SearchByAddressActivity.this,PeoplesInHouseActivity.class);
                        NFCJsonBean nfcJsonBean=new NFCJsonBean(bhone.scode,bhone.hrsPname, bhone.idcard, bhone.hrsAdress, bhone.telphone, "");
                        intent.putExtra(Constants.NFCJSONBEAN,nfcJsonBean);
                        intent.putExtra("TAG",1);
                        intent.putExtra("HOUSE",bhone);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            loadingview.reload("服务器返回数据有误", new LoadingView.OnReloadClickListener() {
                @Override
                public void onReload() {
                    getData(jlx,lzh);
                }
            });
            e.printStackTrace();
        }
    }
}
