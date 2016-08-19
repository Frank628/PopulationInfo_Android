package com.jinchao.population.mainmenu;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.entity.HouseRegistBean;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.ShequWheel;
import com.jinchao.population.view.ShequWheel.OnEnsureClickListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

@ContentView(R.layout.activity_addrentalhouse)
public class AddRentalHouseActivity extends BaseActiviy{
	@ViewInject(R.id.tv_shequ)private TextView tv_shequ;
	@ViewInject(R.id.tv_bianji)private TextView tv_bianji;
	@ViewInject(R.id.edt_bianhao)private EditText edt_bianhao;
	@ViewInject(R.id.edt_fangdongxingming)private EditText edt_fangdongxingming;
	@ViewInject(R.id.edt_dianhua)private EditText edt_dianhua;
	@ViewInject(R.id.edt_shenfenzheng)private EditText edt_shenfenzheng;
	private final static int ADDRESS_EDIT=1;
	private String sqid="",sqname="",address ="",mph="",jieluxiang="",menpaihao="",fuhao="",
			louhao="",shihao="",menpaihaodanwei="",fuhaodanwei="",loudanwei="",louhaodanwei="",
			danyuandanwei="",shihaodanwei="";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("出租屋登记");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		navigationLayout.setRightText("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				String shequ=tv_shequ.getText().toString().trim();
				String fangwubianhao=edt_bianhao.getText().toString().trim();
				String fangdongxingming=edt_fangdongxingming.getText().toString().trim();
				String dianhua =edt_dianhua.getText().toString().trim();
				String cardno=edt_shenfenzheng.getText().toString().trim();
				if (fangwubianhao.equals("")) {
					Toast.makeText(AddRentalHouseActivity.this, "请输入房屋编号~", 0).show();
					return;
				}
				if (fangwubianhao.length()!=6) {
					Toast.makeText(AddRentalHouseActivity.this, "请输入6位的房屋编号~", 0).show();
					return;
				}
				DbUtils dbUtils=DeviceUtils.getDbUtils(AddRentalHouseActivity.this);
				List<HouseAddress> list=new ArrayList<HouseAddress>();
				try {
					list = dbUtils.findAll(Selector.from(HouseAddress.class).where("id","=",fangwubianhao));
				} catch (DbException e) {
					e.printStackTrace();
				}
				if (list==null) {
					Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", 0).show();
					return;
				}
				if (list.size()>0) {
					Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", 0).show();
					return;
				}
				if (address.equals("")) {
					Toast.makeText(AddRentalHouseActivity.this, "请点击编辑房屋地址~", 0).show();
					return;
				}
				if (fangdongxingming.equals("")) {
					Toast.makeText(AddRentalHouseActivity.this, "请输入房东姓名~", 0).show();
					return;
				}
				if (CommonIdcard.validateCard(cardno)) {
					if (cardno.length() == 15) {
						cardno = CommonIdcard.conver15CardTo18(cardno);
                        edt_shenfenzheng.setText(cardno);
                        Toast.makeText(AddRentalHouseActivity.this, "15位转18位证件号成功", 0).show();
                    } else if (cardno.length() == 17) {
                    	cardno = CommonIdcard.conver17CardTo18(cardno);
                    	edt_shenfenzheng.setText(cardno);
                        Toast.makeText(AddRentalHouseActivity.this, "17位转18位证件号成功", 0).show();
                    } 
				}else{
					Toast.makeText(AddRentalHouseActivity.this, "请输入合法的身份证号~", 0).show();
					return;
				}
				if (dianhua.equals("")) {
					Toast.makeText(AddRentalHouseActivity.this, "请输入房东电话~", 0).show();
					return;
				}
				if (!CommonUtils.isTEL(dianhua)) {
					Toast.makeText(AddRentalHouseActivity.this, "联系电话格式错误~", 0).show();
					return;
				}
				save(shequ, fangwubianhao, address, fangdongxingming, dianhua,cardno);
			}
		});
		sqname=MyInfomationManager.getSQNAME(this);
		sqid=MyInfomationManager.getSQID(this);
		tv_shequ.setText(sqname);
	}
	private void save(String shequ,String bianhao,String fangwuaddress,String name,String dianhua,String cardno){
		RequestParams params=new RequestParams(Constants.URL+"HouseSave.aspx");
		params.addBodyParameter("type", "save");

		params.addBodyParameter("user_name", MyInfomationManager.getUserName(AddRentalHouseActivity.this));
		params.addBodyParameter("house_code", bianhao);
		params.addBodyParameter("house_addr", fangwuaddress);
		params.addBodyParameter("house_mph", mph);
		params.addBodyParameter("house_pname", name);
		params.addBodyParameter("house_ptel", dianhua);
		params.addBodyParameter("house_pidcard", cardno);
		params.addBodyParameter("jlx", jieluxiang);
		params.addBodyParameter("mph", mph);
		params.addBodyParameter("mph_flag",menpaihaodanwei);
		params.addBodyParameter("fh", fuhao);
		params.addBodyParameter("fh_flag", fuhaodanwei);
		params.addBodyParameter("zhl_head", loudanwei);
		params.addBodyParameter("zhl", louhao);
		params.addBodyParameter("zhl_flag", louhaodanwei);
		params.addBodyParameter("dy", danyuandanwei);
		params.addBodyParameter("sh", shihao);
		params.addBodyParameter("sh_flag", shihaodanwei);
		params.addBodyParameter("gps", "定位");

		Log.i("house_addr",fangwuaddress);
		Log.i("house_code",bianhao);
		Log.i("house_pname",name);
		Log.i("house_pidcard",cardno);
		x.http().get(params, new CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.d("bbb", result);
				try {
					if (result.trim().contains("该出租屋编号已存在")) {
						Toast.makeText(AddRentalHouseActivity.this, "该出租屋编号已存在", 0).show();
					}else if (result.trim().contains("用户名不存在")) {
						Toast.makeText(AddRentalHouseActivity.this, "用户名不存在", 0).show();
					}else if (result.trim().contains("社区不存在")) {
						Toast.makeText(AddRentalHouseActivity.this, "社区不存在", 0).show();
					}else if (result.trim().contains("出租屋已保存")) {
						Toast.makeText(AddRentalHouseActivity.this, "出租屋已保存", 0).show();
						AddRentalHouseActivity.this.finish();
					}else{
						Toast.makeText(AddRentalHouseActivity.this, "出租屋保存失败", 0).show();
					}
				} catch (Exception e) {
					Toast.makeText(AddRentalHouseActivity.this, "服务器返回数据有误", 0).show();
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(AddRentalHouseActivity.this, "服务器请求异常", 0).show();
			}

			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode ==RESULT_OK && data != null) {
			switch (requestCode) {
			case ADDRESS_EDIT:
				tv_bianji.setText(data.getStringExtra("address"));
				address=data.getStringExtra("address");
				mph=data.getStringExtra("mph");
				jieluxiang=data.getStringExtra("jieluxiang");
				menpaihao=data.getStringExtra("mph");
				fuhao=data.getStringExtra("fuhao");
				louhao=data.getStringExtra("louhao");
				shihao=data.getStringExtra("shihao");
				menpaihaodanwei=data.getStringExtra("mphdanwei");
				fuhaodanwei=data.getStringExtra("fuhaodanwei");
				loudanwei=data.getStringExtra("loudanwei");
				louhaodanwei=data.getStringExtra("louhaodanwei");
				danyuandanwei=data.getStringExtra("danyuan");
				shihaodanwei=data.getStringExtra("shihaodanwei");
				Log.d("params", jieluxiang+menpaihao+menpaihaodanwei+fuhao+fuhaodanwei+loudanwei+louhao+louhaodanwei+danyuandanwei+shihao+shihaodanwei);
				break;
			default:
				break;
			}
		}
	}
	@Event(value={R.id.rl_dizhibianji})
	private void fangwudizhibianjiClick(View v){
		Intent intent =new Intent(this, EidtHouseAddressActivity.class);
		startActivityForResult(intent, ADDRESS_EDIT);
	}
	
	@Event(value={R.id.rl_shequ})
	private void shequClick(View v){
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		ShequWheel shequWheel =new ShequWheel(this, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String sqid1, String sqname1) {
				sqname=sqname1;
				sqid=sqid1;
				tv_shequ.setText(sqname1);
				Dialog.showRadioDialog(AddRentalHouseActivity.this, "确认选择的社区是否正确，已选择-"+sqname1, new DialogClickListener() {
					
					@Override
					public void confirm() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void cancel() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		shequWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0); 
	}
}
