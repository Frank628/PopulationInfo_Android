package com.jinchao.population.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseOperation;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.DeleteRealPeopleBean;
import com.jinchao.population.entity.RealPeopleinHouseBean;
import com.jinchao.population.main.MainActivity;
import com.jinchao.population.main.SplashActivity;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.widget.RoundProgressBar;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.xmlpull.v1.XmlSerializer;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/8/9.
 */
@ContentView(R.layout.fragment_senddata)
public class SendDataFragment  extends BaseFragment{
    @ViewInject(R.id.tv_content)TextView tv_content;
    @ViewInject(R.id.lv)ListView lv;
    private DbUtils dbUtils;
    private List<People> list;
    private android.app.Dialog dialog;
    private RoundProgressBar rb;
    private TextView text;
    private int succCount=0,failCount=0,total=0;
    private CommonAdapter<People> adapter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                succCount=0;failCount=0;
                if (list.size()>0) {
                    showProgressDialog();
                    rb.setProgress(0);
                    total=list.size();
                    text.setText(succCount+"/"+list.size()+",共"+list.size()+"条");
                }
                for (int i = 0; i < list.size(); i++) {

                        createXml(list.get(i));
                        String str = FileUtils.getStringFromFile(new File(Constants.DB_PATH + list.get(i).uuid + ".xml"));
                        upload2(str, list.get(i), i);
                        if (list.get(i).isReal.equals("1")) {
//                        uploadReal(list.get(i).realId);
                            save(list.get(i));

                    }
                }
            }
        });
        dbUtils= DeviceUtils.getDbUtils(getActivity());
        initData();
    }

    private void initData() {
        try {
            list = dbUtils.findAll(Selector.from(People.class));

            if (list==null) {
                list=new ArrayList<People>();
            }
            if (list.size()==0){
                dbUtils.dropTable(People.class);
            }

            tv_content.setText("共找到"+list.size()+"条待发送数据！");
            adapter = new CommonAdapter<People>(getActivity(),list,R.layout.item_daifasong) {
                @Override
                public void convert(ViewHolder helper, final People item, int position) {
                    if (item.cardno.length()<10){
                        helper.setText(R.id.tv_cardno,"房屋编号： " +item.cardno);
                    }else{
                        helper.setText(R.id.tv_cardno,"身份证： " +item.cardno);
                    }
                    helper.setText(R.id.tv_status,"【"+item.module+"】");
                    helper.setText(R.id.tv_name,"采集者： "+item.user_id);
                    helper.setText(R.id.tv_time,item.collect_datetime);
                    helper.getView(R.id.iv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Dialog.showSelectDialog(getActivity(), "确认删除该条数据吗？", new Dialog.DialogClickListener() {
                                @Override
                                public void confirm() {
                                    try {

                                            dbUtils.delete(People.class, WhereBuilder.b("id", "=", item.id));
                                            FileUtils.deleteFile(Constants.DB_PATH + item.uuid + ".xml");
                                            initData();
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void cancel() {

                                }
                            });
                        }
                    });

                }
            };
            lv.setAdapter(adapter);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    private void showProgressDialog(){
        LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
        RelativeLayout layout = (RelativeLayout)inflaterDl.inflate(R.layout.dialog_upload, null );
        dialog = new android.app.Dialog(getActivity(),R.style.LoginDialogTheme);
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().setGravity(Gravity.CENTER);
        rb = (RoundProgressBar) layout.findViewById(R.id.rp);
        text=(TextView) layout.findViewById(R.id.text);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void upload2(String str,final People people,final int i){
        com.lidroid.xutils.http.RequestParams params =new com.lidroid.xutils.http.RequestParams();
        params.addBodyParameter("ActionType", people.actiontype);
        params.addBodyParameter("CollID", MyInfomationManager.getUserName(getActivity()));
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
                failCount++;
                if ((succCount+failCount)==total) {
                    dialog.dismiss();
                    if (failCount>0) {
                        Dialog.showSelectDialog(getActivity(), "发送失败"+failCount+"条,现在重新发送吗？", new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {
                                // TODO Auto-generated method stub

                            }
                            @Override
                            public void cancel() {}
                        });
                    }
                }
            }
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
//				Log.d("upload", arg0.result.replaceAll("<(.|\n)*?>", ""));
                String result=arg0.result.replaceAll("<(.|\n)*?>", "").trim();
                result=result.replaceAll("Import", "");
                if (result.trim().equals("-1")) {
                    Log.d("upload", result);
                    failCount++;
                }else{
                    Log.d("upload", result);
                    tv_content.setText("共找到"+list.size()+"条待发送数据！");
                    rb.setProgress((int)((succCount/total)*100));
                    succCount++;
                    text.setText(succCount+"/"+total+",共"+total+"条");
                    try {
                        dbUtils.delete(People.class,WhereBuilder.b("id", "=", people.id));
                        FileUtils.deleteFile(Constants.DB_PATH+people.uuid+".xml");
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                if ((succCount+failCount)==total) {
                    initData();
                    dialog.dismiss();
                    if (failCount>0) {
                        Dialog.showSelectDialog(getActivity(), "有"+failCount+"条发送失败,现在重新发送吗？", new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {
                                // TODO Auto-generated method stub

                            }
                            @Override
                            public void cancel() {}
                        });
                    }else{
                        Dialog.showSelectDialog(getActivity(), "全部上传成功！", new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {}
                            @Override
                            public void cancel() {}
                        });
                    }
                }
            }
        });
    }
    private void uploadReal(String id){
        com.lidroid.xutils.http.RequestParams params =new com.lidroid.xutils.http.RequestParams();
        params.addBodyParameter("type", "update");
        params.addBodyParameter("id", id);
        HttpUtils http=new HttpUtils();
        http.configRequestRetryCount(10);
        http.send(HttpMethod.POST, Constants.URL+"syrkServer.aspx",params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Log.i("REAL_PEOPLE_UPDARE","fail");
            }
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Log.i("REAL_PEOPLE_UPDARE",arg0.result);
            }
        });
    }

    private void save(final People people){
        RequestParams params=new RequestParams(Constants.URL+"superOper.aspx");
        params.addBodyParameter("type", "save_ck_population_parent");
        params.addBodyParameter("proc", "sp_save_ck_population_parent");
        params.addBodyParameter("name", people.name);
        params.addBodyParameter("idcard", people.cardno);
//		params.addBodyParameter("name", "李荣辉");
//		params.addBodyParameter("idcard", "362422197909178413");
        params.addBodyParameter("sex", people.sex);
        params.addBodyParameter("house_id", people.realId);//realhouse变更为houseid
        params.addBodyParameter("hk_num", "");
        params.addBodyParameter("relation", "不详");
        params.addBodyParameter("status", "1");
        params.addBodyParameter("coll_id", "11");
        params.addBodyParameter("hjdz", people.address);
        params.addBodyParameter("pcs", MyInfomationManager.getSQNAME(getActivity()));
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
    private void createXml(People people) {
        XmlSerializer serializer = Xml.newSerializer();// xml文件生成器
        File file = new File(Constants.DB_PATH,people.uuid+".xml");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            serializer.setOutput(fos, "utf-8");// 为xml生成器设置输出流和字符编码
//        serializer.startDocument("utf-8", true);// 开始文档，参数分别为字符编码和是否保持独立
            serializer.startTag(null, "xml_reco"); // 开始标签,参数分别为：命名空间和标签名
            if (people.module.equals("注销")) {
                serializer.startTag(null, "module");// 开始标签
                serializer.text(people.module);// 文本内容
                serializer.endTag(null, "module");// 结束标签

                serializer.startTag(null, "user_id");
                serializer.text(people.user_id);
                serializer.endTag(null, "user_id");

                serializer.startTag(null, "name");
                serializer.text(people.name);
                serializer.endTag(null, "name");

                serializer.startTag(null, "cardno");
                serializer.text(people.cardno);
                serializer.endTag(null, "cardno");

                serializer.startTag(null, "Roomcode");
                serializer.text(people.Roomcode);
                serializer.endTag(null, "Roomcode");

                serializer.startTag(null, "Residentcode");
                serializer.text(people.homecode);
                serializer.endTag(null, "Residentcode");

                serializer.startTag(null, "ResidentAddress");
                serializer.text(people.ResidentAddress);
                serializer.endTag(null, "ResidentAddress");

                serializer.startTag(null, "collect_datetime");
                serializer.text(people.collect_datetime);
                serializer.endTag(null, "collect_datetime");
            }else if(people.module.equals("补")){
                serializer.startTag(null, "module");// 开始标签
                serializer.text(people.module);// 文本内容
                serializer.endTag(null, "module");// 结束标签

                serializer.startTag(null, "user_id");
                serializer.text(people.user_id);
                serializer.endTag(null, "user_id");

                serializer.startTag(null, "cardno");
                serializer.text(people.cardno);
                serializer.endTag(null, "cardno");

                serializer.startTag(null, "collect_datetime");
                serializer.text(people.collect_datetime);
                serializer.endTag(null, "collect_datetime");

                serializer.startTag(null, "picture");
                serializer.text(people.picture);
                serializer.endTag(null, "picture");

                serializer.startTag(null, "phototype");
                serializer.text("1");
                serializer.endTag(null, "phototype");
            }else{
                serializer.startTag(null, "module");// 开始标签
                serializer.text(people.module);// 文本内容
                serializer.endTag(null, "module");// 结束标签

                serializer.startTag(null, "card_type");
                serializer.text(people.card_type);
                serializer.endTag(null, "card_type");

                serializer.startTag(null, "user_id");
                serializer.text(people.user_id);
                serializer.endTag(null, "user_id");

                serializer.startTag(null, "name");
                serializer.text(people.name);
                serializer.endTag(null, "name");

                serializer.startTag(null, "formername");
                serializer.text(people.formername);
                serializer.endTag(null, "formername");

                serializer.startTag(null, "cbqk");
                serializer.text(people.cbqk);
                serializer.endTag(null, "cbqk");

                serializer.startTag(null, "sex");
                serializer.text(people.sex);
                serializer.endTag(null, "sex");

                serializer.startTag(null, "people");
                serializer.text(people.people);
                serializer.endTag(null, "people");

                serializer.startTag(null, "cardno");
                serializer.text(people.cardno);
                serializer.endTag(null, "cardno");

                serializer.startTag(null, "hjdxz");
                serializer.text(people.hjdxz);
                serializer.endTag(null, "hjdxz");

                serializer.startTag(null, "address");
                serializer.text(people.address);
                serializer.endTag(null, "address");

                serializer.startTag(null, "birthday");
                serializer.text(people.birthday);
                serializer.endTag(null, "birthday");

                serializer.startTag(null, "picture");
                serializer.text(people.picture);
                serializer.endTag(null, "picture");

                serializer.startTag(null, "is_jzz");
                serializer.text(people.is_jzz);
                serializer.endTag(null, "is_jzz");

                serializer.startTag(null, "height");
                serializer.text(people.height);
                serializer.endTag(null, "height");

                serializer.startTag(null, "education");
                serializer.text(people.education);
                serializer.endTag(null, "education");

                serializer.startTag(null, "marriage");
                serializer.text(people.marriage);
                serializer.endTag(null, "marriage");

                serializer.startTag(null, "landscape");
                serializer.text(people.landscape);
                serializer.endTag(null, "landscape");

                serializer.startTag(null, "homecode");
                serializer.text(people.homecode);
                serializer.endTag(null, "homecode");

                serializer.startTag(null, "ResidentAddress");
                serializer.text(people.ResidentAddress);
                serializer.endTag(null, "ResidentAddress");

                serializer.startTag(null, "Roomcode");
                serializer.text(people.Roomcode);
                serializer.endTag(null, "Roomcode");

                serializer.startTag(null, "RoomType");
                serializer.text(people.RoomType);
                serializer.endTag(null, "RoomType");

                serializer.startTag(null, "ResidentReason");
                serializer.text(people.ResidentReason);
                serializer.endTag(null, "ResidentReason");

                serializer.startTag(null, "SeviceAddress");
                serializer.text(people.SeviceAddress);
                serializer.endTag(null, "SeviceAddress");

                serializer.startTag(null, "Note");
                serializer.text(people.Note);
                serializer.endTag(null, "Note");

                serializer.startTag(null, "PositionName");
                serializer.text(people.PositionName);
                serializer.endTag(null, "PositionName");

                serializer.startTag(null, "IsInsured");
                serializer.text(people.IsInsured);
                serializer.endTag(null, "IsInsured");

                serializer.startTag(null, "ChanBaoShiJian");
                serializer.text(people.ChanBaoShiJian );
                serializer.endTag(null, "ChanBaoShiJian");

                serializer.startTag(null, "Phone");
                serializer.text(people.Phone);
                serializer.endTag(null, "Phone");

                serializer.startTag(null, "FuBingYi");
                serializer.text(people.FuBingYi);
                serializer.endTag(null, "FuBingYi");

                serializer.startTag(null, "JuZhuLeiBie");
                serializer.text(people.JuZhuLeiBie);
                serializer.endTag(null, "JuZhuLeiBie");

                serializer.startTag(null, "JuZhuFangShi");
                serializer.text(people.JuZhuFangShi);
                serializer.endTag(null, "JuZhuFangShi");

                serializer.startTag(null, "FangDongGuanXi");
                serializer.text(people.FangDongGuanXi);
                serializer.endTag(null, "FangDongGuanXi");

                serializer.startTag(null, "FuQiTongXing");
                serializer.text(people.FuQiTongXing);
                serializer.endTag(null, "FuQiTongXing");

                serializer.startTag(null, "ShengYuQingKuang");
                serializer.text(people.ShengYuQingKuang);
                serializer.endTag(null, "ShengYuQingKuang");

                serializer.startTag(null, "childNum");
                serializer.text(people.childNum);
                serializer.endTag(null, "childNum");

                serializer.startTag(null, "gravidity");
                serializer.text(people.gravidity);
                serializer.endTag(null, "gravidity");

                serializer.startTag(null, "BirthCtl");
                serializer.text(people.BirthCtl);
                serializer.endTag(null, "BirthCtl");

                serializer.startTag(null, "ShiFouLingZheng");
                serializer.text(people.ShiFouLingZheng);
                serializer.endTag(null, "ShiFouLingZheng");

                serializer.startTag(null, "carType");
                serializer.text(people.carType);
                serializer.endTag(null, "carType");

                serializer.startTag(null, "carNo");
                serializer.text(people.carNo );
                serializer.endTag(null, "carNo");

                serializer.startTag(null, "Rent");
                serializer.text(people.Rent);
                serializer.endTag(null, "Rent");

                serializer.startTag(null, "QQ");
                serializer.text(people.QQ);
                serializer.endTag(null, "QQ");

                serializer.startTag(null, "MAC_id");
                serializer.text(people.MAC_id);
                serializer.endTag(null, "MAC_id");


                serializer.startTag(null, "JieYuCuoShi");
                serializer.text(people.JieYuCuoShi);
                serializer.endTag(null, "JieYuCuoShi");

                serializer.startTag(null, "sq_name");
                serializer.text(people.sq_name);
                serializer.endTag(null, "sq_name");

                serializer.startTag(null, "LingQuFangShi");
                serializer.text(people.LingQuFangShi);
                serializer.endTag(null, "LingQuFangShi");

                serializer.startTag(null, "JuZhuShiJian");
                serializer.text(people.JuZhuShiJian);
                serializer.endTag(null, "JuZhuShiJian");

                serializer.startTag(null, "ShenQingLeiBie");
                serializer.text(people.ShenQingLeiBie);
                serializer.endTag(null, "ShenQingLeiBie");

                serializer.startTag(null, "ShouJiXingHao");
                serializer.text(people.ShouJiXingHao);
                serializer.endTag(null, "ShouJiXingHao");

                serializer.startTag(null, "ShouJiChuanHao");
                serializer.text(people.ShouJiChuanHao);
                serializer.endTag(null, "ShouJiChuanHao");

                serializer.startTag(null, "BeiYong");
                serializer.text(people.BeiYong);
                serializer.endTag(null, "BeiYong");

                serializer.startTag(null, "phototype");
                serializer.text("1");
                serializer.endTag(null, "phototype");
                //新增xml部分
                serializer.startTag(null, "MSN");
                serializer.text(people.MSN);
                serializer.endTag(null, "MSN");

                serializer.startTag(null, "Email");
                serializer.text(people.Email);
                serializer.endTag(null, "Email");

                serializer.startTag(null, "czwxz");
                serializer.text(people.czwxz);
                serializer.endTag(null, "czwxz");

                serializer.startTag(null, "lsrq");
                serializer.text(people.lsrq);
                serializer.endTag(null, "lsrq");

                serializer.startTag(null, "djrq");
                serializer.text(people.djrq);
                serializer.endTag(null, "djrq");

                serializer.startTag(null, "fzxm");
                serializer.text(people.fzxm);
                serializer.endTag(null, "fzxm");

                serializer.startTag(null, "fzdh");
                serializer.text(people.fzdh);
                serializer.endTag(null, "fzdh");

                serializer.startTag(null, "fzsfz");
                serializer.text(people.fzsfz);
                serializer.endTag(null, "fzsfz");

                serializer.startTag(null, "dwlxdh");
                serializer.text(people.dwlxdh);
                serializer.endTag(null, "dwlxdh");

                serializer.startTag(null, "zymc");
                serializer.text(people.zymc);
                serializer.endTag(null, "zymc");

                serializer.startTag(null, "ldhtqj");
                serializer.text(people.ldhtqj);
                serializer.endTag(null, "ldhtqj");

                serializer.startTag(null, "sbbh");
                serializer.text(people.sbbh);
                serializer.endTag(null, "sbbh");

                serializer.startTag(null, "jyrq");
                serializer.text(people.jyrq);
                serializer.endTag(null, "jyrq");

                serializer.startTag(null, "jkzbh");
                serializer.text(people.jkzbh);
                serializer.endTag(null, "jkzbh");

                serializer.startTag(null, "jkzbh");
                serializer.text(people.jkzbh);
                serializer.endTag(null, "jkzbh");

                serializer.startTag(null, "dwfzr");
                serializer.text(people.dwfzr);
                serializer.endTag(null, "dwfzr");

                serializer.startTag(null, "sfjy");
                serializer.text("1");
                serializer.endTag(null, "sfjy");

                serializer.startTag(null, "hyzmzl");
                serializer.text(people.hyzmzl);
                serializer.endTag(null, "hyzmzl");

                serializer.startTag(null, "hyzmbh");
                serializer.text(people.hyzmbh);
                serializer.endTag(null, "hyzmbh");

                serializer.startTag(null, "hyqfrq");
                serializer.text(people.hyqfrq);
                serializer.endTag(null, "hyqfrq");

                serializer.startTag(null, "jqjzym");
                serializer.text("0");
                serializer.endTag(null, "jqjzym");

                serializer.startTag(null, "jhrq");
                serializer.text(people.jhrq);
                serializer.endTag(null, "jhrq");

                serializer.startTag(null, "yfjzzh");
                serializer.text(people.yfjzzh);
                serializer.endTag(null, "yfjzzh");

                serializer.startTag(null, "dsznz");
                serializer.text(people.dsznz);
                serializer.endTag(null, "dsznz");

                serializer.startTag(null, "fwkh");
                serializer.text(people.fwkh);
                serializer.endTag(null, "fwkh");

                serializer.startTag(null, "bycsrq");
                serializer.text(people.bycsrq);
                serializer.endTag(null, "bycsrq");

                serializer.startTag(null, "czqx1");
                serializer.text(people.czqx1);
                serializer.endTag(null, "czqx1");

                serializer.startTag(null, "czqx2");
                serializer.text(people.czqx2);
                serializer.endTag(null, "czqx2");

                serializer.startTag(null, "zjdq");
                serializer.text("20161101");
                serializer.endTag(null, "zjdq");

                serializer.startTag(null, "beizhu2");
                serializer.text(people.beizhu2 );
                serializer.endTag(null, "beizhu2");

                serializer.startTag(null, "sbsbh");
                serializer.text( CommonUtils.getIMEI(getActivity()));
                serializer.endTag(null, "sbsbh");

                serializer.startTag(null, "xdr1");
                serializer.text(people.xdr1);
                serializer.endTag(null, "xdr1");

                serializer.startTag(null, "xdxm1");
                serializer.text(people.xdxm1);
                serializer.endTag(null, "xdxm1");

                serializer.startTag(null, "xdxb1");
                serializer.text(people.xdxb1);
                serializer.endTag(null, "xdxb1");

                serializer.startTag(null, "xdrq1");
                serializer.text(people.xdrq1);
                serializer.endTag(null, "xdrq1");

                serializer.startTag(null, "xdsfz1");
                serializer.text(people.xdsfz1);
                serializer.endTag(null, "xdsfz1");

                serializer.startTag(null, "xdr2");
                serializer.text(people.xdr2);
                serializer.endTag(null, "xdr2");

                serializer.startTag(null, "xdxm2");
                serializer.text(people.xdxm2);
                serializer.endTag(null, "xdxm2");

                serializer.startTag(null, "xdxb2");
                serializer.text(people.xdxb2);
                serializer.endTag(null, "xdxb2");

                serializer.startTag(null, "xdrq2");
                serializer.text(people.xdrq2);
                serializer.endTag(null, "xdrq2");

                serializer.startTag(null, "xdsfz2");
                serializer.text(people.xdsfz2);
                serializer.endTag(null, "xdsfz2");

                serializer.startTag(null, "xdr3");
                serializer.text(people.xdr3);
                serializer.endTag(null, "xdr3");

                serializer.startTag(null, "xdxm3");
                serializer.text(people.xdxm3);
                serializer.endTag(null, "xdxm3");

                serializer.startTag(null, "xdxb3");
                serializer.text(people.xdxb3);
                serializer.endTag(null, "xdxb3");

                serializer.startTag(null, "xdrq3");
                serializer.text(people.xdrq3);
                serializer.endTag(null, "xdrq3");

                serializer.startTag(null, "xdsfz3");
                serializer.text(people.xdsfz3);
                serializer.endTag(null, "xdsfz3");

                serializer.startTag(null, "xdr4");
                serializer.text(people.xdr4);
                serializer.endTag(null, "xdr4");

                serializer.startTag(null, "xdxm4");
                serializer.text(people.xdxm4);
                serializer.endTag(null, "xdxm4");

                serializer.startTag(null, "xdxb4");
                serializer.text(people.xdxb4);
                serializer.endTag(null, "xdxb4");

                serializer.startTag(null, "xdrq4");
                serializer.text(people.xdrq4);
                serializer.endTag(null, "xdrq4");

                serializer.startTag(null, "xdsfz4");
                serializer.text(people.xdsfz4);
                serializer.endTag(null, "xdsfz4");

                serializer.startTag(null, "gxr1");
                serializer.text(people.gxr1);
                serializer.endTag(null, "gxr1");

                serializer.startTag(null, "gxxm1");
                serializer.text(people.gxxm1);
                serializer.endTag(null, "gxxm1");

                serializer.startTag(null, "gxxb1");
                serializer.text(people.gxxb1);
                serializer.endTag(null, "gxxb1");

                serializer.startTag(null, "gxrq1");
                serializer.text(people.gxrq1);
                serializer.endTag(null, "gxrq1");

                serializer.startTag(null, "gxsfz1");
                serializer.text(people.gxsfz1);
                serializer.endTag(null, "gxsfz1");

                serializer.startTag(null, "gxrjzk1");
                serializer.text(people.gxrjzk1);
                serializer.endTag(null, "gxrjzk1");

                serializer.startTag(null, "gxr2");
                serializer.text(people.gxr2);
                serializer.endTag(null, "gxr2");

                serializer.startTag(null, "gxxm2");
                serializer.text(people.gxxm2);
                serializer.endTag(null, "gxxm2");

                serializer.startTag(null, "gxxb2");
                serializer.text(people.gxxb2);
                serializer.endTag(null, "gxxb2");

                serializer.startTag(null, "gxrq2");
                serializer.text(people.gxrq2);
                serializer.endTag(null, "gxrq2");

                serializer.startTag(null, "gxsfz2");
                serializer.text(people.gxsfz2);
                serializer.endTag(null, "gxsfz2");

                serializer.startTag(null, "gxrjzk2");
                serializer.text(people.gxrjzk2);
                serializer.endTag(null, "gxrjzk2");

                serializer.startTag(null, "gxr3");
                serializer.text(people.gxr3);
                serializer.endTag(null, "gxr3");

                serializer.startTag(null, "gxxm3");
                serializer.text(people.gxxm3);
                serializer.endTag(null, "gxxm3");

                serializer.startTag(null, "gxxb3");
                serializer.text(people.gxxb3);
                serializer.endTag(null, "gxxb3");

                serializer.startTag(null, "gxrq3");
                serializer.text(people.gxrq3);
                serializer.endTag(null, "gxrq3");

                serializer.startTag(null, "gxsfz3");
                serializer.text(people.gxsfz3);
                serializer.endTag(null, "gxsfz3");

                serializer.startTag(null, "gxrjzk3");
                serializer.text(people.gxrjzk3);
                serializer.endTag(null, "gxrjzk3");

                serializer.startTag(null, "gxr4");
                serializer.text(people.gxr4);
                serializer.endTag(null, "gxr4");

                serializer.startTag(null, "gxxm4");
                serializer.text(people.gxxm4);
                serializer.endTag(null, "gxxm4");

                serializer.startTag(null, "gxxb4");
                serializer.text(people.gxxb4);
                serializer.endTag(null, "gxxb4");

                serializer.startTag(null, "gxrq4");
                serializer.text(people.gxrq4);
                serializer.endTag(null, "gxrq4");

                serializer.startTag(null, "gxsfz4");
                serializer.text(people.gxsfz4);
                serializer.endTag(null, "gxsfz4");

                serializer.startTag(null, "gxrjzk4");
                serializer.text(people.gxrjzk4);
                serializer.endTag(null, "gxrjzk4");
            }

            serializer.endTag(null, "xml_reco");// 结束标签
            serializer.endDocument();// 结束xml文档
        } catch (Exception e) {
            Toast.makeText(getActivity(), "XML生成失败！", Toast.LENGTH_SHORT);
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


//	private void upload(File file,People people){
//	RequestParams params=new RequestParams("http://222.92.144.66:91/IDcollect/Import.aspx");
//	params.addBodyParameter("ActionType", people.actiontype);
//	params.addBodyParameter("CollID", MyInfomationManager.getUserName(UpLoadActivity.this));
//	params.addBodyParameter("IdCard", people.cardno);
//	params.addBodyParameter("XmlFileName", people.uuid);
//	params.addBodyParameter("XmlFileNameExt", "xml");
//	params.addBodyParameter("XmlBody", file);
//	x.http().post(params, new CommonCallback<String>() {
//
//		@Override
//		public void onSuccess(String result) {
//			Log.d("bbb", result+"succ");
//		}
//		@Override
//		public void onError(Throwable ex, boolean isOnCallback) {
//			Log.d("bbb", "error");
//		}
//		@Override
//		public void onCancelled(CancelledException cex) {
//			// TODO Auto-generated method stub
//		}
//		@Override
//		public void onFinished() {
//			Log.d("bbb", "ffff");
//
//		}
//	});
//}
}
