package com.jinchao.population.realpopulation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.entity.DeleteRealPeopleBean;
import com.jinchao.population.view.DialogLoading;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.activity.PersonInHouseActivity;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.NearbyHouseBean;
import com.jinchao.population.entity.RealPeopleSearchBean;
import com.jinchao.population.entity.RealPeopleinHouseBean;
import com.jinchao.population.entity.SortbyRoomCodeClass;
import com.jinchao.population.entity.SortbyTimeClass;
import com.jinchao.population.entity.SortbyzaizhuClass;
import com.jinchao.population.mainmenu.HandleIDActivity;
import com.jinchao.population.mainmenu.SearchPeopleActivity;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonHttp;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.DialogShowPic;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.LoadMoreListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by OfferJiShu01 on 2016/11/25.
 */

@org.xutils.view.annotation.ContentView(R.layout.activity_searchpeoplereal)
public class SearchPeopleRealActivity extends BaseReaderActiviy implements  IDReader.IDReaderListener{
    @ViewInject(R.id.rg_search) private RadioGroup rg_search;
    @ViewInject(R.id.rb_renyuan) private RadioButton rb_renyuan;
    @ViewInject(R.id.rb_time) private RadioButton rb_time;
    @ViewInject(R.id.sv_content) private ScrollView sv_content;
    @ViewInject(R.id.rl_housecode) private RelativeLayout rl_housecode;
    @ViewInject(R.id.tv_content) private TextView tv_content;
    @ViewInject(R.id.lv) private ListView lv;
    @ViewInject(R.id.rg)private RadioGroup rg_sort;
    @ViewInject(R.id.edt_content) private EditText edt_content;
    @ViewInject(R.id.ll_search) private LinearLayout ll_search;
    @ViewInject(R.id.ll_operation) private LinearLayout ll_operation;
    @ViewInject(R.id.btn_add) private Button btn_add;
    @ViewInject(R.id.btn_delay) private Button btn_delay;
    @ViewInject(R.id.btn_logout) private Button btn_logout;
    @ViewInject(R.id.loadmorelv) private LoadMoreListView loadmorelv;
    @ViewInject(R.id.rotate_header_list_view_frame) private PtrClassicFrameLayout mPtrFrame;
    private CommonAdapter<RealPeopleinHouseBean.RealPeopleinHouseOne> adapter;
    private List<RealPeopleinHouseBean.RealPeopleinHouseOne> listreal=new ArrayList<RealPeopleinHouseBean.RealPeopleinHouseOne>();
    private People peoplereadcard;//读卡读取的人员信息
    private People peoplefromadd;//加入后返回的人员信息
    private RelativeLayout layout;
    private PopupWindow popdelete;
    private RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("人员信息查询");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        idReader.setListener(this);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getNearbyList();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        //the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        loadmorelv.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
        MyApplication.myApplication.locationService.start();
        rg_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ll_operation.setVisibility(View.GONE);
                switch (checkedId) {
                    case R.id.rb_fangwu:
                        ll_search.setVisibility(View.VISIBLE);
                        mPtrFrame.setVisibility(View.GONE);
                        rl_housecode.setVisibility(View.GONE);
                        sv_content.setVisibility(View.GONE);
                        edt_content.setHint("请输入房屋编号");
                        edt_content.setText("");
                        tv_content.setText("");
                        break;
                    case R.id.rb_renyuan:
                        ll_search.setVisibility(View.VISIBLE);
                        mPtrFrame.setVisibility(View.GONE);
                        rl_housecode.setVisibility(View.GONE);
                        sv_content.setVisibility(View.VISIBLE);
                        edt_content.setHint("请输入身份证号或读卡");
                        edt_content.setText("");
                        tv_content.setText("");
                        break;
                    case R.id.rb_nearby:
                        rl_housecode.setVisibility(View.GONE);
                        sv_content.setVisibility(View.GONE);
                        ll_search.setVisibility(View.GONE);
                        mPtrFrame.setVisibility(View.VISIBLE);
                        mPtrFrame.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPtrFrame.autoRefresh();
                            }
                        }, 100);
                        break;
                    default:
                        break;
                }
            }
        });
        rg_sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (listreal.size()>0) {
                    switch (checkedId) {
                        case R.id.rb_time:
                            if (adapter!=null) {
                                Collections.sort(listreal, new SortbyTimeClass());
                                adapter.notifyDataSetChanged();
                                lv.setSelection(0);
                            }
                            break;
                        case R.id.rb_shihao:
                            if (adapter!=null) {
                                Collections.sort(listreal, new SortbyRoomCodeClass());
                                adapter.notifyDataSetChanged();
                                lv.setSelection(0);
                            }
                            break;
                        case R.id.rb_zaizhu:
                            if (adapter!=null) {
                                Collections.sort(listreal, new SortbyzaizhuClass());
                                adapter.notifyDataSetChanged();
                                lv.setSelection(0);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (link != null)
            return;
        Tag nfcTag = null;
        try {
            nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        } catch (Exception e) {

        }
        if (nfcTag != null) {
            link = LinkFactory.createExNFCLink(nfcTag);
            try {
                link.connect();
            } catch (Exception e) {
                showError(e.getMessage());
                try {
                    link.disconnect();
                } catch (Exception ex) {
                } finally {
                    link = null;
                }
                return;
            }
            idReader.setLink(link);
            showIDCardInfo(true, null,null);
            showProcessDialog("正在读卡中，请稍后");

            idReader.startReadCard();
        }
    }
    private void showIDCardInfo(boolean isClear,PersonInfo user,String msg)  {//显示身份证数据到界面
        hideProcessDialog();
        if (isClear){
            return;
        }
        if (user==null&&msg!=null){
            showError(msg);
        }else{
            edt_content.setText(user.getIdNumber().trim());
            BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length).compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byte[] b = stream.toByteArray();
            String picture = new String(Base64Coder.encodeLines(b));
            peoplereadcard= new People(user.getName(), user.getIdNumber(), user.getNation(), user.getSex(), user.getBirthday(), user.getAddress(),picture,user.getIdNumber().trim().substring(0, 6),"1",MyInfomationManager.getUserName(this),MyInfomationManager.getSQNAME(this),"");
        }
        isReading = false;

    }
    @Override
    public void onReadCardSuccess(final PersonInfo personInfo) {
        try {
            link.disconnect();
        } catch (Exception e) {
            Log.e("readcard", e.getMessage());
        } finally {
            link = null;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProcessDialog();
                showIDCardInfo(false,personInfo,null);
            }
        });

    }

    @Override
    public void onReadCardFailed(final String s) {
        try {
            link.disconnect();
        } catch (Exception e) {
            Log.e("readcard", e.getMessage());
        } finally {
            link = null;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProcessDialog();
                showError(s);
            }
        });

    }
    private void getPeopleinHouse(String house_id){
        showProcessDialog("数据加载中，请稍等...");
        RequestParams params=new RequestParams(Constants.URL+"syrkServer.aspx");
        params.addBodyParameter("type","house_people");
        params.addBodyParameter("scode",house_id);
        params.addBodyParameter("sq_id", MyInfomationManager.getSQID(SearchPeopleRealActivity.this));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processPeoleinHouse(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {hideProcessDialog();}
        });
    }
    private void processPeoleinHouse(String json){
        try {
            RealPeopleSearchBean realPeopleinHouseBean = GsonTools.changeGsonToBean(json, RealPeopleSearchBean.class);
            listreal.clear();
            listreal.addAll(realPeopleinHouseBean.data);
            if (listreal.size()==0){
                rl_housecode.setVisibility(View.GONE);
                sv_content.setVisibility(View.VISIBLE);
                tv_content.setText("该房屋编号不存在，或者无在住人员！");
                return;
            }
            rl_housecode.setVisibility(View.VISIBLE);
            sv_content.setVisibility(View.GONE);
            rb_time.setChecked(true);
            SortbyTimeClass sortbyTimeClass =new SortbyTimeClass();
            Collections.sort(listreal, sortbyTimeClass);
            adapter = new CommonAdapter<RealPeopleinHouseBean.RealPeopleinHouseOne>(SearchPeopleRealActivity.this,listreal,R.layout.item_realhousepopulation) {
                @Override
                public void convert(ViewHolder helper, RealPeopleinHouseBean.RealPeopleinHouseOne item, int position) {
                    String sQx = "在住";
                    if (item.status == -1){
                        sQx = "所内他处";
                    }else if (item.status == -2){
                        sQx = "区内所外";
                    }else if (item.status == -3){
                        sQx = "市内区外";
                    }else if (item.status == -4){
                        sQx = "市外";
                    }else if (item.status == -5){
                        sQx = "其他";
                    }

                    helper.setText(R.id.tv_top,item.sname.trim()+"     "+item.idcard+"     "+item.sex);
                    String[] split = item.chkudt.split("\\s+");
                    if (item.idcard.trim().substring(0, 4).equals("3205")||item.house_addr.trim().contains("苏州")||item.house_addr.trim().contains("昆山")||item.house_addr.trim().contains("吴江")||item.house_addr.trim().contains("张家港")||item.house_addr.trim().contains("太仓")||item.house_addr.trim().contains("常熟")) {
                        ((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
                        helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
                        helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
                        helper.setText(R.id.tv_time, item.chkudt);
                    }else{
                        if (split.length>1) {
                            if (isOneYearLater(split[0])) {
                                ((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#ff0000"));
                                helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
                                helper.setText(R.id.tv_time, Html.fromHtml( "<font color=\'#ff0000\'>"+item.chkudt+"</font>"));
                                helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#ff0000\'>室号："+item.roomCode+"</font>"));
                            }else{
                                ((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
                                helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
                                helper.setText(R.id.tv_time, item.chkudt);
                                helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
                            }
                        }else{
                            ((TextView)helper.getView(R.id.tv_top)).setTextColor(Color.parseColor("#666666"));
                            helper.setText(R.id.tv_bottom, Html.fromHtml("<font color=\'#2360B9\'>"+sQx+"&nbsp&nbsp</font><font color=\'#2360B9\'>"+item.pcs+"</font>"));
                            helper.setText(R.id.tv_time, item.chkudt);
                            helper.setText(R.id.tv_shihao, Html.fromHtml("<font color=\'#666666\'>室号："+item.roomCode+"</font>"));
                        }
                    }


                }
            };
            lv.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Event(value={R.id.btn_seach})
    private void seachClick(View view){
        String cardno=edt_content.getText().toString().trim();
        if (rb_renyuan.isChecked()){
            if (cardno.equals("")) {
                Toast.makeText(this, "请录入身份证号~", Toast.LENGTH_SHORT).show();
                return;
            }
            if (CommonIdcard.validateCard(cardno)) {
                if (cardno.length() == 15) {
                    cardno = CommonIdcard.conver15CardTo18(cardno);
                    edt_content.setText(cardno);
                    Toast.makeText(SearchPeopleRealActivity.this, "15位转18位证件号成功", Toast.LENGTH_SHORT).show();
                } else if (cardno.length() == 17) {
                    cardno = CommonIdcard.conver17CardTo18(cardno);
                    edt_content.setText(cardno);
                    Toast.makeText(SearchPeopleRealActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(SearchPeopleRealActivity.this, "请输入合法身份证！", Toast.LENGTH_SHORT).show();
                return;
            }
            requestYanZheng(cardno);
        }else {
            if (!CommonUtils.isFangwuBianHao(edt_content.getText().toString().trim())) {
                Toast.makeText(SearchPeopleRealActivity.this, "请输入6位的房屋编号！", Toast.LENGTH_SHORT).show();
            }
            getPeopleinHouse(edt_content.getText().toString().trim());
        }
    }
    public static boolean isOneYearLater(String day) {
        String[] s=day.split("-");
        String dayf=s[0]+"-"+(s[1].length()==1?("0"+s[1]):s[1])+"-"+(s[2].length()==1?("0"+s[2]):s[2]);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(dayf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + (long)334 * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        int falg=dateOk.compareTo(simpleDateFormat.format(new Date()));
        if (falg>=0) {
            return false;
        }else{
            return true;
        }
    }
    private void getNearbyList(){
        if (MyApplication.myApplication.myLocation.getLat()==0){
            Toast.makeText(this,"位置信息获取失败，正在为您重新获取！",Toast.LENGTH_SHORT).show();
            MyApplication.myApplication.locationService.start();
            return;
        }
        int databaseType = SharePrefUtil.getInt(this,Constants.DATABASE_TYPE,0);
        if (databaseType==0){
            Toast.makeText(this,"数据库类型未获取，请稍后！",Toast.LENGTH_SHORT).show();
            CommonHttp.getDataBaseType(this);
            return;
        }
        RequestParams params =new RequestParams(Constants.URL+"HousePosition.aspx");
        if (databaseType==1) {//1外来人口，2实有人口
            params.addBodyParameter("type", "get_wlrklist");
        }else {
            params.addBodyParameter("type", "get_syrklist");
        }
        params.addBodyParameter("jd",MyApplication.myApplication.myLocation.getLog()+"");
        params.addBodyParameter("wd",MyApplication.myApplication.myLocation.getLat()+"");
        params.addBodyParameter("userid",MyInfomationManager.getUserID(SearchPeopleRealActivity.this));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("houselistnearby",result);
                processNearbyHouse(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("houselistnearby",ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mPtrFrame.refreshComplete();
            }
        });
    }
    private void processNearbyHouse(String result){
        try {
            NearbyHouseBean nearbyHouseBean =GsonTools.changeGsonToBean(result,NearbyHouseBean.class);
            loadmorelv.setTotalNum(nearbyHouseBean.data.size());
            if (nearbyHouseBean.data.size()==0){
                loadmorelv.setNodata("附近暂无房屋",false,null);
            }else{
                CommonAdapter<NearbyHouseBean.NearbyHouseOne> adapter =new CommonAdapter<NearbyHouseBean.NearbyHouseOne>(SearchPeopleRealActivity.this,nearbyHouseBean.data,R.layout.item_nearby) {
                    @Override
                    public void convert(ViewHolder helper, final NearbyHouseBean.NearbyHouseOne item, int position) {
                        helper.setText(R.id.tv_housecode,"房屋编号："+item.scode);
                        helper.setText(R.id.tv_houseaddress,item.address);
                        helper.setText(R.id.tv_distance,item.distance);
                        helper.getView(R.id.tv_showpic).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogShowPic dialogShowPic =new DialogShowPic(SearchPeopleRealActivity.this,item.house_pic);
                                dialogShowPic.show();
                            }
                        });
                        if (item.house_pic.trim().equals("")){
                            helper.getView(R.id.tv_showpic).setVisibility(View.GONE);
                        }else{
                            helper.getView(R.id.tv_showpic).setVisibility(View.VISIBLE);
                        }
                    }
                };
                loadmorelv.setAdapter(adapter);
                loadmorelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        NearbyHouseBean.NearbyHouseOne  nearbyHouseOne=(NearbyHouseBean.NearbyHouseOne)((ListView)adapterView).getItemAtPosition(i);
                        Intent intent=new Intent(SearchPeopleRealActivity.this, PersonInHouseActivity.class);
                        intent.putExtra("id",nearbyHouseOne.scode);
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void requestYanZheng(String idcard){
        showProcessDialog("数据加载中，请稍等...");
        if(peoplereadcard!=null){
            if (!peoplereadcard.getCardno().trim().equals(idcard)){
                peoplereadcard=null;
            }
        }
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=get_people&idcard="+idcard);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("quePeople", result);
                tv_content.setText(XmlUtils.parseXML(result));
                if (XmlUtils.parseXMLhasthisPeople(result)) {
//                    ll_operation.setVisibility(View.VISIBLE);
//                    btn_add.setEnabled(true);
//                    btn_delay.setEnabled(false);
//                    btn_logout.setEnabled(false);
                    if (peoplereadcard==null){//如果不是读卡，是输入身份证搜索
                        peoplereadcard=XmlUtils.parseXMLtoPeople(result);
                    }else{
                        People p=XmlUtils.parseXMLtoPeople(result);
                        peoplereadcard.setHomecode(p.getHomecode());
                        peoplereadcard.setResidentAddress(p.getResidentAddress());
                        peoplereadcard.setPhone(p.getPhone());
                    }

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {
                hideProcessDialog();
            }
        });
    }
    @Event(value={R.id.btn_add})
    private void addClick(View view){
        Intent intent = new Intent(SearchPeopleRealActivity.this,HandleIDActivity.class);
        intent.putExtra("people",peoplereadcard );
        intent.putExtra(Constants.Where_from,2);
        intent.putExtra("isHandle", true);
        startActivityForResult(intent,1);
    }
    @Event(value={R.id.btn_delay})
    private void yanqi(View view){
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date =sDateFormat.format(new java.util.Date());
        People people=new People(peoplefromadd.name, peoplefromadd.cardno, peoplefromadd.cardno.substring(0, 6), "变更", CommonUtils.GenerateGUID(), "1", "1",
                MyInfomationManager.getUserName(SearchPeopleRealActivity.this), "1", peoplefromadd.homecode, peoplefromadd.ResidentAddress, peoplefromadd.Roomcode,
                MyInfomationManager.getSQNAME(SearchPeopleRealActivity.this),date);
        DbUtils dbUtils = DeviceUtils.getDbUtils(this);
        List<People> list=new ArrayList<People>();
        try {
            list = dbUtils.findAll(Selector.from(People.class).where("cardno", "=", peoplefromadd.cardno));
            if (list!=null) {
                if (list.size()>0) {
                    dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", peoplefromadd.cardno));
                }
            }
            dbUtils.save(people);
            Toast.makeText(this, "延期成功~", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "延期失败~", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Event(value={R.id.btn_logout})
    private void zhuxiao(View view){
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date =sDateFormat.format(new java.util.Date());
        People people=new People(peoplefromadd.name, date, peoplefromadd.cardno, "注销", CommonUtils.GenerateGUID(), "2",
                MyInfomationManager.getUserName(SearchPeopleRealActivity.this), peoplereadcard.homecode,peoplereadcard.ResidentAddress);
        DbUtils dbUtils =DeviceUtils.getDbUtils(this);
        List<People> list=new ArrayList<People>();
        try {
            list = dbUtils.findAll(Selector.from(People.class).where("cardno", "=", peoplereadcard.cardno));
            if (list!=null) {
                if (list.size()>0) {
                    dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", peoplereadcard.cardno));
                }
            }
            dbUtils.save(people);
            Toast.makeText(this, "注销成功~", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "注销失败~", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode ==RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if (data != null) {
                        peoplefromadd=(People) data.getSerializableExtra("people");
                        if (peoplefromadd!=null){
                            XmlUtils.createXml(peoplefromadd,SearchPeopleRealActivity.this);
                            String str = FileUtils.getStringFromFile(new File(Constants.DB_PATH + peoplefromadd.uuid + ".xml"));
                            save(peoplefromadd);
                            upload2(str,peoplefromadd);
                        }
                    }
                    break;

                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void save(final People people){
        RequestParams params=new RequestParams(Constants.URL+"superOper.aspx");
        params.addBodyParameter("type", "save_ck_population_parent");
        params.addBodyParameter("proc", "sp_save_ck_population_parent");
        params.addBodyParameter("name", people.name);
        params.addBodyParameter("idcard", people.cardno);
        params.addBodyParameter("sex", people.sex);
        params.addBodyParameter("house_id", people.realId);//realhouse变更为houseid
        params.addBodyParameter("hk_num", "");
        params.addBodyParameter("relation", "不详");
        params.addBodyParameter("status", "1");
        params.addBodyParameter("coll_id", "11");
        params.addBodyParameter("hjdz", people.address);
        params.addBodyParameter("pcs", MyInfomationManager.getSQNAME(SearchPeopleRealActivity.this));
        params.addBodyParameter("roomCode", people.Roomcode);
        params.addBodyParameter("parent_idcard","");
        x.http().post(params, new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if (result.trim().equals("<result>0</result>")) {
//                    Toast.makeText(getActivity(), "添加成功",Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                save(people);
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {
            }
        });
    }
    private void upload2(String str,final People people){
        showProcessDialog("数据上传中...");
        com.lidroid.xutils.http.RequestParams params =new com.lidroid.xutils.http.RequestParams();
        params.addBodyParameter("ActionType", people.actiontype);
        params.addBodyParameter("CollID", MyInfomationManager.getUserName(SearchPeopleRealActivity.this));
        params.addBodyParameter("IdCard", people.cardno);
        params.addBodyParameter("XmlFileName", people.uuid);
        params.addBodyParameter("XmlFileNameExt", ".xml");
        params.addBodyParameter("XmlBody", str);
        HttpUtils http=new HttpUtils();
        http.configRequestRetryCount(0);
        http.send(HttpMethod.POST, "http://222.92.144.66:90/IDcollect/Import.aspx",params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Log.d("upload", arg1+"faile");
                hideProcessDialog();
            }
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                hideProcessDialog();
                String result=arg0.result.replaceAll("<(.|\n)*?>", "").trim();
                result=result.replaceAll("Import", "");
                if (result.trim().equals("-1")) {
                    Log.d("upload", result);
                    Toast.makeText(SearchPeopleRealActivity.this,"上传失败！",Toast.LENGTH_SHORT).show();
                }else{
                    btn_delay.setEnabled(true);
                    btn_logout.setEnabled(true);
                    btn_add.setEnabled(false);
                    FileUtils.deleteFile(Constants.DB_PATH+people.uuid+".xml");
                    Toast.makeText(SearchPeopleRealActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
