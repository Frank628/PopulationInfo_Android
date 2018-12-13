package com.jinchao.population.mainmenu;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;
import org.xutils.common.Callback;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.alienPeople.DataManagement.SendDataDetailActivity;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.NavigationLayout;
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



@ContentView(R.layout.activity_upload)
public class UpLoadActivity extends BaseActiviy{
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("数据发送");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		navigationLayout.setRightText("发送", new OnClickListener() {
			@Override
			public void onClick(View v) {
				succCount=0;failCount=0;
				if (list.size()>0) {
					showProgressDialog();
					rb.setProgress(0);
					total=list.size();
					text.setText(succCount+"/"+list.size()+",共"+list.size()+"条");
				}
                for (int i = 0; i < list.size(); i++) {
                    XmlUtils.createXml(list.get(i),UpLoadActivity.this);
                    String str=FileUtils.getStringFromFile(new File(Constants.DB_PATH+list.get(i).uuid+".xml"));
                    upload2(str, list.get(i),i);
                    if (list.get(i).isReal.equals("1")){
//                        uploadReal(list.get(i).realId);
//                        save(list.get(i));
                    }
					if (list.get(i).module.equals("补")){
						uploadpic(list.get(i));
					}
                }
			}
		});
		dbUtils=DeviceUtils.getDbUtils(this);
		initData();
				
	}
	private void initData() {
		try {
			list = dbUtils.findAll(Selector.from(People.class));
			if (list==null) {
				list=new ArrayList<People>();
			}
			tv_content.setText("共找到"+list.size()+"条待发送数据！");
			adapter = new CommonAdapter<People>(this,list,R.layout.item_daifasong) {
				@Override
				public void convert(ViewHolder helper, final People item, int position) {
					helper.setText(R.id.tv_cardno,"身份证： " +item.cardno);
					helper.setText(R.id.tv_status,"【"+item.module+"】");
					helper.setText(R.id.tv_name,"采集者： "+item.user_id);
					helper.setText(R.id.tv_time,item.collect_datetime);
					helper.getView(R.id.iv).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
								Dialog.showSelectDialog(UpLoadActivity.this, "确认删除该条数据吗？", new DialogClickListener() {
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
					helper.getView(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent =new Intent(UpLoadActivity.this, SendDataDetailActivity.class);
							intent.putExtra("people",item);
							startActivity(intent);
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
		LayoutInflater inflaterDl = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout)inflaterDl.inflate(R.layout.dialog_upload, null );
		dialog = new android.app.Dialog(UpLoadActivity.this,R.style.LoginDialogTheme);
		dialog.getWindow().setContentView(layout);
		dialog.getWindow().setGravity(Gravity.CENTER);
		rb = (RoundProgressBar) layout.findViewById(R.id.rp);
		text=(TextView) layout.findViewById(R.id.text);
		dialog.setCancelable(false);
		dialog.show();
	}
	private void uploadpic(final People people){
		RequestParams params = new RequestParams(Constants.URL + "GdPeople.aspx");
		params.addBodyParameter("type", "insertPhoto");
		params.addBodyParameter("idcard", people.cardno);
		params.addBodyParameter("sqdm", MyInfomationManager.getSQCODE(UpLoadActivity.this));
		params.addBodyParameter("sname", people.name);
		params.addBodyParameter("photo", people.picture);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
//                System.out.println(result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				uploadpic(people);
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {
			}
		});
	}
	private void upload2(String str,final People people,final int i){
		com.lidroid.xutils.http.RequestParams params =new com.lidroid.xutils.http.RequestParams();
		params.addBodyParameter("ActionType", people.actiontype);
		params.addBodyParameter("CollID", MyInfomationManager.getUserName(UpLoadActivity.this));
		params.addBodyParameter("IdCard", people.cardno);
		params.addBodyParameter("XmlFileName", people.uuid);
		params.addBodyParameter("XmlFileNameExt", ".xml");
		params.addBodyParameter("XmlBody", str);
		HttpUtils http=new HttpUtils();
		http.configRequestRetryCount(0);
		http.send(HttpMethod.POST, "http://222.92.144.66:90/IDcollect/Import.aspx",params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.d("upload", arg1+"："+arg0.getMessage());
				failCount++;
				if ((succCount+failCount)==total) {
					dialog.dismiss();
					if (failCount>0) {
						Dialog.showSelectDialog(UpLoadActivity.this, "发送失败"+failCount+"条,现在重新发送吗？", new DialogClickListener() {
							@Override
							public void confirm() {
							}
							@Override
							public void cancel() {}
						});
					}
				}
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
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
						Dialog.showSelectDialog(UpLoadActivity.this, "有"+failCount+"条发送失败,现在重新发送吗？", new DialogClickListener() {
							@Override
							public void confirm() {
								
							}
							@Override
							public void cancel() {}
						});
					}else{
						Dialog.showSelectDialog(UpLoadActivity.this, "全部上传成功！", new DialogClickListener() {
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
        params.addBodyParameter("pcs", MyInfomationManager.getSQNAME(UpLoadActivity.this));
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
