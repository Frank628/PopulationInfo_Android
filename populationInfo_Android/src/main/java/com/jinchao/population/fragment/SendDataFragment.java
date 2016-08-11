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
import com.jinchao.population.dbentity.People;
import com.jinchao.population.main.MainActivity;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.FileUtils;
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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileOutputStream;
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
                    String str=FileUtils.getStringFromFile(new File(Constants.DB_PATH+list.get(i).uuid+".xml"));
                    upload2(str, list.get(i),i);
                    if (list.get(i).isReal.equals("1")){
                        uploadReal(list.get(i).realId);
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
                    helper.setText(R.id.tv_cardno,"身份证： " +item.cardno);
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
                                        FileUtils.deleteFile(Constants.DB_PATH+item.uuid+".xml");
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
