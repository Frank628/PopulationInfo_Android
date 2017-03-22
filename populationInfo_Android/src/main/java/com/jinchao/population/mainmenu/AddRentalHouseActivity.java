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

import com.Common;
import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.dbentity.HouseAddressOldBean;
import com.jinchao.population.dbentity.HouseAddressOldBean10;
import com.jinchao.population.dbentity.HouseAddressOldBean2;
import com.jinchao.population.dbentity.HouseAddressOldBean3;
import com.jinchao.population.dbentity.HouseAddressOldBean4;
import com.jinchao.population.dbentity.HouseAddressOldBean5;
import com.jinchao.population.dbentity.HouseAddressOldBean6;
import com.jinchao.population.dbentity.HouseAddressOldBean7;
import com.jinchao.population.dbentity.HouseAddressOldBean8;
import com.jinchao.population.dbentity.HouseAddressOldBean9;
import com.jinchao.population.entity.HouseRegistBean;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.ShequWheel;
import com.jinchao.population.view.ShequWheel.OnEnsureClickListener;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

@ContentView(R.layout.activity_addrentalhouse)
public class AddRentalHouseActivity extends BaseActiviy{
	@ViewInject(R.id.tv_shequ)private TextView tv_shequ;
	@ViewInject(R.id.tv_bianji)private TextView tv_bianji;
	@ViewInject(R.id.edt_bianhao)private ValidateEidtText edt_bianhao;
	@ViewInject(R.id.edt_fangdongxingming)private EditText edt_fangdongxingming;
	@ViewInject(R.id.edt_dianhua)private ValidateEidtText edt_dianhua;
	@ViewInject(R.id.edt_shenfenzheng)private ValidateEidtText edt_shenfenzheng;
	private final static int ADDRESS_EDIT=1;
	private String sqid="",sqname="",address ="",mph="",jieluxiang="",menpaihao="",fuhao="",
			louhao="",shihao="",menpaihaodanwei="",fuhaodanwei="",loudanwei="",louhaodanwei="",
			danyuandanwei="",shihaodanwei="";
	public int database_tableNo=0;//当前登录账号使用的哪个数据库，0:未下载的地址库，1：表1,2：表2.。。。
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
		database_tableNo=((MyApplication)getApplication()).database_tableNo;
		navigationLayout.setRightText("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				String shequ=tv_shequ.getText().toString().trim();
				String fangwubianhao=edt_bianhao.getText().toString().trim();
				String fangdongxingming=edt_fangdongxingming.getText().toString().trim();
				String dianhua =edt_dianhua.getText().toString().trim();
				String cardno=edt_shenfenzheng.getText().toString().trim();
				if (fangwubianhao.equals("")) {
					Toast.makeText(AddRentalHouseActivity.this, "请输入房屋编号~", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!CommonUtils.isFangwuBianHao(fangwubianhao)) {
					Toast.makeText(AddRentalHouseActivity.this, "请输入6位数字或字母组成的房屋编号~", Toast.LENGTH_SHORT).show();
					return;
				}
				DbUtils dbUtils=DeviceUtils.getDbUtils(AddRentalHouseActivity.this);
				switch (database_tableNo){
					case 0:
						Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
						break;
					case 1:
						List<HouseAddressOldBean> list=new ArrayList<HouseAddressOldBean>();
						try {
							list = dbUtils.findAll(Selector.from(HouseAddressOldBean.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 2:
						List<HouseAddressOldBean2> list2=new ArrayList<HouseAddressOldBean2>();
						try {
							list2 = dbUtils.findAll(Selector.from(HouseAddressOldBean2.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list2==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list2.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 3:
						List<HouseAddressOldBean3> list3=new ArrayList<>();
						try {
							list3 = dbUtils.findAll(Selector.from(HouseAddressOldBean3.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list3==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list3.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 4:
						List<HouseAddressOldBean4> list4=new ArrayList<HouseAddressOldBean4>();
						try {
							list4 = dbUtils.findAll(Selector.from(HouseAddressOldBean4.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list4==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list4.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 5:
						List<HouseAddressOldBean5> list5=new ArrayList<HouseAddressOldBean5>();
						try {
							list5 = dbUtils.findAll(Selector.from(HouseAddressOldBean5.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list5==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list5.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 6:
						List<HouseAddressOldBean6> list6=new ArrayList<HouseAddressOldBean6>();
						try {
							list6 = dbUtils.findAll(Selector.from(HouseAddressOldBean6.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list6==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list6.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 7:
						List<HouseAddressOldBean7> list7=new ArrayList<HouseAddressOldBean7>();
						try {
							list7 = dbUtils.findAll(Selector.from(HouseAddressOldBean7.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list7==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list7.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 8:
						List<HouseAddressOldBean8> list8=new ArrayList<HouseAddressOldBean8>();
						try {
							list8 = dbUtils.findAll(Selector.from(HouseAddressOldBean8.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list8==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list8.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 9:
						List<HouseAddressOldBean9> list9=new ArrayList<HouseAddressOldBean9>();
						try {
							list9 = dbUtils.findAll(Selector.from(HouseAddressOldBean9.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list9==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list9.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 10:
						List<HouseAddressOldBean10> list10=new ArrayList<HouseAddressOldBean10>();
						try {
							list10 = dbUtils.findAll(Selector.from(HouseAddressOldBean10.class).where("scode","=",fangwubianhao));
						} catch (DbException e) {
							e.printStackTrace();
						}
						if (list10==null) {
							Toast.makeText(AddRentalHouseActivity.this, "未下载地址库，无法校验房屋编号~", Toast.LENGTH_SHORT).show();
							return;
						}
						if (list10.size()>0) {
							Toast.makeText(AddRentalHouseActivity.this, "该房屋编号已存在~", Toast.LENGTH_SHORT).show();
							return;
						}
						break;
				}


				if (address.equals("")) {
					Toast.makeText(AddRentalHouseActivity.this, "请点击编辑房屋地址~", Toast.LENGTH_SHORT).show();
					return;
				}
				if (fangdongxingming.equals("")) {
					Toast.makeText(AddRentalHouseActivity.this, "请输入房东姓名~", Toast.LENGTH_SHORT).show();
					return;
				}
				if (CommonUtils.isContainSp(fangdongxingming)){
					Toast.makeText(AddRentalHouseActivity.this, "房东姓名不可包含单引号和&符号", Toast.LENGTH_SHORT).show();
					return;
				}
				if (CommonIdcard.validateCard(cardno)) {
					if (cardno.length() == 15) {
						cardno = CommonIdcard.conver15CardTo18(cardno);
                        edt_shenfenzheng.setText(cardno);
                        Toast.makeText(AddRentalHouseActivity.this, "15位转18位证件号成功", Toast.LENGTH_SHORT).show();
                    } else if (cardno.length() == 17) {
                    	cardno = CommonIdcard.conver17CardTo18(cardno);
                    	edt_shenfenzheng.setText(cardno);
                        Toast.makeText(AddRentalHouseActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
                    } 
				}else{
					Toast.makeText(AddRentalHouseActivity.this, "请输入合法的身份证号~", Toast.LENGTH_SHORT).show();
					return;
				}
				if (dianhua.equals("")) {
					Toast.makeText(AddRentalHouseActivity.this, "请输入房东电话~", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!CommonUtils.isGuangdaTel(dianhua)) {
					Toast.makeText(AddRentalHouseActivity.this, "联系电话格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
				save(shequ, fangwubianhao, address, fangdongxingming, dianhua,cardno,getIntent().getBooleanExtra(Constants.IS_FROM_REALPOPULATION,false));
			}
		});
		sqname=MyInfomationManager.getSQNAME(this);
		sqid=MyInfomationManager.getSQID(this);
		tv_shequ.setText(sqname);
	}
	private void save(String shequ, String bianhao, String fangwuaddress, String name, String dianhua, String cardno, final boolean isfrom_real){
		RequestParams params=new RequestParams(Constants.URL+"HouseSave.aspx");
		if (isfrom_real){
			params=new RequestParams(Constants.URL+"syrkHouse.aspx");
			params.addBodyParameter("type", "saveHouse");
		}else{
			params=new RequestParams(Constants.URL+"HouseSave.aspx");
			params.addBodyParameter("type", "save");
			params.addBodyParameter("house_mph", mph);
			params.addBodyParameter("gps", "定位");
		}
		params.addBodyParameter("user_name", MyInfomationManager.getUserName(AddRentalHouseActivity.this));
		params.addBodyParameter("house_code", bianhao);
		params.addBodyParameter("house_addr", fangwuaddress);
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


		Log.i("house_addr",fangwuaddress);
		Log.i("house_code",bianhao);
		Log.i("house_pname",name);
		Log.i("house_pidcard",cardno);
		x.http().get(params, new CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.d("bbb", result);
				if (isfrom_real){
					if (result.trim().equals("0")){
						Toast.makeText(AddRentalHouseActivity.this, "出租屋已保存", Toast.LENGTH_SHORT).show();
						AddRentalHouseActivity.this.finish();
					}else if(result.trim().equals("-1")){
						Toast.makeText(AddRentalHouseActivity.this, "该出租屋编号已存在", Toast.LENGTH_SHORT).show();
					}else if(result.trim().equals("-2")){
						Toast.makeText(AddRentalHouseActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
					}else if(result.trim().equals("-3")){
						Toast.makeText(AddRentalHouseActivity.this, "社区不存在", Toast.LENGTH_SHORT).show();
					}

				}else{
					try {
						if (result.trim().contains("该出租屋编号已存在")) {
							Toast.makeText(AddRentalHouseActivity.this, "该出租屋编号已存在", Toast.LENGTH_SHORT).show();
						}else if (result.trim().contains("用户名不存在")) {
							Toast.makeText(AddRentalHouseActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
						}else if (result.trim().contains("社区不存在")) {
							Toast.makeText(AddRentalHouseActivity.this, "社区不存在", Toast.LENGTH_SHORT).show();
						}else if (result.trim().contains("出租屋已保存")) {
							Toast.makeText(AddRentalHouseActivity.this, "出租屋已保存", Toast.LENGTH_SHORT).show();
							AddRentalHouseActivity.this.finish();
						}else{
							Toast.makeText(AddRentalHouseActivity.this, "出租屋保存失败", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						Toast.makeText(AddRentalHouseActivity.this, "服务器返回数据有误", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(AddRentalHouseActivity.this, "服务器请求异常", Toast.LENGTH_SHORT).show();
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
