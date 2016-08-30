package com.jinchao.population.main;

import java.util.ArrayList;
import java.util.List;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.UserBean;
import com.jinchao.population.entity.UserBean.AccountOne;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.FileUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.PopupUser;
import com.jinchao.population.view.PopupUser.OnEnsureClickListener;





@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActiviy{
	
	private PopupUser popupUser;
	@ViewInject(R.id.edt_user)
	private EditText edt_user;
	@ViewInject(R.id.edt_password)
	private EditText edt_password;
	@ViewInject(R.id.edt_user2)
	private EditText edt_user2;
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
	}
	@Event(value=R.id.edt_user)
	private void showPopClick(View view){
		popupUser.showAtLocation(root, Gravity.BOTTOM, 0, 0);
	}
	@Event(value=R.id.btn_login)
	private void loginClick(View view){
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
		final DialogLoading dialogLoading =new DialogLoading(LoginActivity.this, "登录中...");
		dialogLoading.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String users="";
					if (SharePrefUtil.getString(LoginActivity.this, Constants.USER_DB, "").trim().equals("")) {
						users=FileUtils.getAssetsTxt(LoginActivity.this, "user.txt");
					}else{
						users=SharePrefUtil.getString(LoginActivity.this, Constants.USER_DB, "");
					}
					UserBean userBean =GsonTools.changeGsonToBean(users, UserBean.class);
					for (int i = 0; i < userBean.data.size(); i++) {
						for (int j = 0; j <userBean.data.get(i).account.size(); j++) {
							if (username.equals(userBean.data.get(i).account.get(j).userName.trim())) {
								dialogLoading.dismiss();
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
								dialogLoading.dismiss();
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
	private void toggleUserClick(View view){
		if (isToggle) {
			ll_default.setVisibility(View.VISIBLE);
			ll_toggle.setVisibility(View.GONE);
			btn_toggle.setText("切换用户");
			isToggle=false;
		}else{
			ll_default.setVisibility(View.GONE);
			ll_toggle.setVisibility(View.VISIBLE);
			edt_user2.requestFocus();
			btn_toggle.setText("取消切换");
			isToggle=true;
		}
		
		
	}
}
