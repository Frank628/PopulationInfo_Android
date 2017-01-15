package com.jinchao.population.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.UserHistory;
import com.jinchao.population.dbentity.UserPKDataBase;
import com.jinchao.population.entity.UserBean;
import com.jinchao.population.entity.UserBean.AccountOne;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.PopupUser;
import com.jinchao.population.view.PopupUser.OnEnsureClickListener;
import com.jinchao.population.view.UserDropDownPop;
import com.jinchao.population.widget.DrawableClickEditText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;


@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActiviy{
	private PopupUser popupUser;
	@ViewInject(R.id.edt_user)
	private EditText edt_user;
	@ViewInject(R.id.edt_password)
	private EditText edt_password;
	@ViewInject(R.id.edt_user2)
	private DrawableClickEditText edt_user2;
	@ViewInject(R.id.edt_password2)
	private EditText edt_password2;
	@ViewInject(R.id.ll_default)
	private LinearLayout ll_default;
	@ViewInject(R.id.ll_toggle)
	private LinearLayout ll_toggle;
	@ViewInject(R.id.root)
	private RelativeLayout root;
	@ViewInject(R.id.btn_toggle)
	private Button btn_toggle;
	@ViewInject(R.id.tv_version)
	private TextView tv_version;
	private boolean isToggle=false;//是否（在切换用页）手动输入用户
	private String password="";
	private String username="";
	String users="";
	UserDropDownPop userDropDownPop;
	DbUtils dbUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		popupUser = new PopupUser(this, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(AccountOne accountOne) {
				edt_user.setText(accountOne.userName);
				edt_password.setText("123456");
			}
		});
		if(!MyInfomationManager.getUserName(this).equals("")) {
			edt_user.setText(MyInfomationManager.getUserName(this));
			edt_password.setText(MyInfomationManager.getPassWord(this));
			edt_user.setFocusable(false);
			edt_user.setOnClickListener(null);
		}
		tv_version.setText("版本号：V"+CommonUtils.getVersionName(LoginActivity.this));
		dbUtils= DeviceUtils.getDbUtils(this);
		userDropDownPop=new UserDropDownPop(LoginActivity.this,new UserDropDownPop.OnItemClickListener() {
			@Override
			public void onClick(String username) {
				edt_user2.setText(username);
				edt_password2.setText("123456");
				userDropDownPop.dismiss();
			}
		});
		edt_user2.setDrawableRightListener(new DrawableClickEditText.DrawableRightListener() {
			@Override
			public void onDrawableRightClick(View view) {
				userDropDownPop.showPopupWindow(edt_user2);
			}
		});
        if(!CommonUtils.isNotificationEnabled(this)){
            Dialog.showRadioDialog(this, "请在该手机“权限管理”应用中将（人口信息采集仪）的“通知”权限打开！否则将无法正常接收提示！", new Dialog.DialogClickListener() {
                @Override
                public void confirm() {

                }
                @Override
                public void cancel() {

                }
            });
        }
	}
	@Event(value=R.id.edt_user)
	private void showPopClick(View view){
		popupUser.showAtLocation(root, Gravity.BOTTOM, 0, 0);
	}
	@Event(value=R.id.btn_login)
	private void loginClick(View view){

		if (SharePrefUtil.getString(LoginActivity.this, Constants.USER_DB, "").trim().equals("")) {
			users=FileUtils.getAssetsTxt(LoginActivity.this, "user.txt");
		}else{
			users=SharePrefUtil.getString(LoginActivity.this, Constants.USER_DB, "");
		}
		username = "";
		password="";
		if (isToggle) {
			username=edt_user2.getText().toString().trim();
			password=edt_password2.getText().toString().trim();
			if (username.equals("")) {
				Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
				return;
			}
		}else{
			username=edt_user.getText().toString().trim();
			password="123456";
			if (username.equals("")) {
				Toast.makeText(LoginActivity.this, "请先选择账号", Toast.LENGTH_SHORT).show();
				return;
			}	
		}
		showProgressDialog("","登录中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					UserBean userBean =GsonTools.changeGsonToBean(users, UserBean.class);
					for (int i = 0; i < userBean.data.size(); i++) {
						for (int j = 0; j <userBean.data.get(i).account.size(); j++) {
							if (username.equals(userBean.data.get(i).account.get(j).userName.trim())) {
								dismissProgressDialog();
								if (isToggle) {
									if (!password.equals("123456")) {
										runOnUiThread(new Runnable() {
											public void run() {
												Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
											}
										});
										return;
									}
								}
								MyInfomationManager.setPCSId(LoginActivity.this, userBean.data.get(i).pcsId);
								MyInfomationManager.setUserName(LoginActivity.this, username);
								MyInfomationManager.setPassWord(LoginActivity.this, password);
								MyInfomationManager.setUserID(LoginActivity.this, userBean.data.get(i).account.get(j).userId);
								MyInfomationManager.setSQID(LoginActivity.this, userBean.data.get(i).account.get(j).sqId);
								MyInfomationManager.setSQNAME(LoginActivity.this, userBean.data.get(i).account.get(j).sqName);
                                SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time =sDateFormat.format(new Date(System.currentTimeMillis()));
								try {
									UserHistory userHistory=dbUtils.findFirst(Selector.from(UserHistory.class).where("user_name","=",username));
									List<UserHistory> listbytime =null;
									if(dbUtils.tableIsExist(UserHistory.class)) {
										listbytime = dbUtils.findAll(Selector.from(UserHistory.class).orderBy("time", true));
                                        if (userHistory!=null){
                                            userHistory.setTime(time);
                                            dbUtils.update(userHistory,"time");
                                        }else{
                                            if (listbytime.size()==5){
                                                dbUtils.deleteById(UserHistory.class,listbytime.get(4).getId());
                                                UserPKDataBase userPKDataBase_d=dbUtils.findFirst(Selector.from(UserPKDataBase.class).where("sq_name","=",listbytime.get(4).getSq_name()));
                                                if(userPKDataBase_d!=null){
                                                    userPKDataBase_d.setIs_used("1");
                                                    dbUtils.saveOrUpdate(userPKDataBase_d);
                                                }
                                            }
                                            if (userHistory==null) {
                                                dbUtils.save(new UserHistory(username, userBean.data.get(i).account.get(j).userId, userBean.data.get(i).account.get(j).sqName, userBean.data.get(i).account.get(j).sqId, userBean.data.get(i).pcsName, userBean.data.get(i).pcsId,time));
                                            }
                                        }

									}
									((MyApplication)getApplication()).setDataBaseTableNo(DatabaseUtil.getSQ_DataBase_No(LoginActivity.this));
								} catch (DbException e) {
									e.printStackTrace();
								}
								List<String> list =new ArrayList<String>();
								List<AccountOne> listacount=new ArrayList<UserBean.AccountOne>();
								for (int k = 0; k < userBean.data.get(i).account.size(); k++) {
									if (!list.contains( userBean.data.get(i).account.get(k).sqName)) {
										list.add(userBean.data.get(i).account.get(k).sqName);
										listacount.add(userBean.data.get(i).account.get(k));
									}
								}
								SharePrefUtil.saveObj(LoginActivity.this, Constants.Accountlist, listacount);
								loginSuccess();
								return;
							}
							if (i==(userBean.data.size()-1)&&j==(userBean.data.get(i).account.size()-1)) {
								dismissProgressDialog();
								runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
									}
								});
								return;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			private void loginSuccess() {
				Intent intent =new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
//				LoginActivity.this.finish();
			}
		}).start();
	}
	
	@Event(value={R.id.btn_toggle})
	private void toggleUserClick(View view) {
		if (isToggle) {
			ll_default.setVisibility(View.VISIBLE);
			ll_toggle.setVisibility(View.GONE);
			btn_toggle.setText("切换用户");
			userDropDownPop.dismiss();
			isToggle = false;
		} else {
			ll_default.setVisibility(View.GONE);
			ll_toggle.setVisibility(View.VISIBLE);
			edt_user2.requestFocus();
			btn_toggle.setText("取消切换");
			isToggle = true;
		}
	}

}
