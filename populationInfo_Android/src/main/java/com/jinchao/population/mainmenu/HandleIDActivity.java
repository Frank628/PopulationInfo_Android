	package com.jinchao.population.mainmenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.jinchao.population.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.jinchao.population.base.BaseHandleIDActivity;
import com.jinchao.population.entity.HouseAddressOldBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.view.PopBianzheng.OnbEnsureClickListener;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RealHouseBean.RealHouseOne;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.NationPop.OnEnsureClickListener;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.PopBianzheng;
import com.jinchao.population.view.PopHouse;
import com.jinchao.population.view.PopHouse.OnHouseEnsureClickListener;
import com.jinchao.population.view.StringWheel;
import com.jinchao.population.widget.MonPickerDialog;
import com.jinchao.population.widget.RightButtonCheckBox;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;


@ContentView(R.layout.activity_handleid)
public class HandleIDActivity extends BaseHandleIDActivity{
	public static final String TAG="HANDLEID";
	@ViewInject(R.id.rg_tab)private RadioGroup rg_tab;
	@ViewInject(R.id.edt_stature)private EditText edt_stature;
	@ViewInject(R.id.edt_shihao)private EditText edt_shihao;
	@ViewInject(R.id.edt_fuwuchusuo)private EditText edt_fuwuchusuo;
	@ViewInject(R.id.edt_danweidizhi)private EditText edt_danweidizhi;
	@ViewInject(R.id.edt_dianhua)private EditText edt_dianhua;
	@ViewInject(R.id.edt_chepaihao)private EditText edt_chepaihao;
	@ViewInject(R.id.edt_zujin)private EditText edt_zujin;
	@ViewInject(R.id.edt_qq)private EditText edt_qq;
	@ViewInject(R.id.edt_mac)private EditText edt_mac;
	@ViewInject(R.id.edt_shoujixinghao)private EditText edt_shoujixinghao;
	@ViewInject(R.id.edt_shoujichuanhao)private EditText edt_shoujichuanhao;
	@ViewInject(R.id.rg_hunyin)private RadioGroup rg_hunyin;
	@ViewInject(R.id.rg_zhengzhi)private RadioGroup rg_zhengzhi;
	@ViewInject(R.id.rg_iscanbao)private RadioGroup rg_iscanbao;
	@ViewInject(R.id.rg_isfubingyi)private RadioGroup rg_isfubingyi;
	@ViewInject(R.id.rg_isfuqitongxing)private RadioGroup rg_isfuqitongxing;
	@ViewInject(R.id.rg_isjunzhu)private RadioGroup rg_isjunzhu;
	@ViewInject(R.id.rg_isbanzheng)private RadioGroup rg_isbanzheng;
	@ViewInject(R.id.rb_banzhengshi)private RadioButton rb_banzhengshi;
	@ViewInject(R.id.rb_banzhengfou)private RadioButton rb_banzhengfou;
	@ViewInject(R.id.rl_canbaoshijian)private RelativeLayout rl_canbaoshijian;
	@ViewInject(R.id.rl_shifoubanzheng)private RelativeLayout rl_shifoubanzheng;
	@ViewInject(R.id.tv_canbaoshijian)private TextView tv_canbaoshijian;
	@ViewInject(R.id.tv_xianyunnianyue)private TextView tv_xianyunnianyue;
	@ViewInject(R.id.tv_degree)private TextView tv_degree;
	@ViewInject(R.id.tv_chusuoleixing)private TextView tv_chusuoleixing;
	@ViewInject(R.id.tv_zanzhushiyou)private TextView tv_zanzhushiyou;
	@ViewInject(R.id.tv_chanyeleixing)private TextView tv_chanyeleixing;
	@ViewInject(R.id.tv_juzhufangshi)private TextView tv_juzhufangshi;
	@ViewInject(R.id.tv_fangdongguanxi)private TextView tv_fangdongguanxi;
	@ViewInject(R.id.tv_jieyucuoshi)private TextView tv_jieyucuoshi;
	@ViewInject(R.id.tv_jiaotonggongju)private TextView tv_jiaotonggongju;
	@ViewInject(R.id.edt_zinvgeshu)private EditText edt_zinvgeshu;
	@ViewInject(R.id.ll_hunhou)private LinearLayout ll_hunhou;
	@ViewInject(R.id.edt_bianhao)private EditText edt_bianhao;
	@ViewInject(R.id.edt_dizhi)private EditText edt_dizhi;
	@ViewInject(R.id.rc_bianhao)private RightButtonCheckBox rc_bianhao;
	@ViewInject(R.id.rc_dizhi)private RightButtonCheckBox rc_dizhi;
	@ViewInject(R.id.rc_shihao)private RightButtonCheckBox rc_shihao;
	@ViewInject(R.id.rc_chusuoleixing)private RightButtonCheckBox rc_chusuoleixing;
	@ViewInject(R.id.rc_fuwuchusuo)private RightButtonCheckBox rc_fuwuchusuo;
	@ViewInject(R.id.rc_danweidizhi)private RightButtonCheckBox rc_danweidizhi;

	@ViewInject(R.id.ll_member)private LinearLayout ll_member;
	@ViewInject(R.id.layout_b)private ScrollView sv_b;
	private People people,people2;
	private Calendar c;
	private boolean isHandleID;
	private String height="",wenhua="",hunyin="",zhengzhi="",fangwubiaohao="",
			zanzhudizhi="",shihao="",chusuoleixing="",zanzhushiyou="",fuwuchusuo="",danweidizhi="",
			canyeleixing="",chanyeleixing="",shifoucanbao="",canbaoshijian="",dianhua="",fubingyi="",
			juzhuleibie="",juzhufangshi="",fangdongguanxi="",fuqitongxing="",shengyuzhuangkuang="",
			zinvgeshu="",jieyucuoshi="",xianyunnianyue="",shifoulingzheng="",jiaotonggongju="",
			chepaihao="",zujin="",qq="",macaddress="",shoujixinghao="",shoujichuanhao="",beiyong1="";
	private DbUtils dbUtils;
	private RealHouseOne realHouseOne;
	private RenyuanInHouseBean.RenyuanInhouseOne renyuanInhouseOne;
	String right_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		isHandleID = getIntent().getBooleanExtra("isHandle", false);
		navigationLayout.setCenterText("暂住信息"+(isHandleID?"登记":"变更"));
		people=(People) getIntent().getSerializableExtra("people");
		realHouseOne=(RealHouseOne) getIntent().getSerializableExtra("house");
		renyuanInhouseOne =(RenyuanInHouseBean.RenyuanInhouseOne) getIntent().getSerializableExtra("renYuanXinXiBean");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		right_name=isHandleID?"办证":"变更";
		navigationLayout.setRightText(right_name, new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftKeyBord();
				save();
			}
		});
		dbUtils=DeviceUtils.getDbUtils(this);
		c = Calendar.getInstance();
		rg_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId){
					case R.id.rb_a:
						findViewById(R.id.layout_a).setVisibility(View.VISIBLE);
						findViewById(R.id.layout_b).setVisibility(View.GONE);
						break;
					case R.id.rb_b:
						findViewById(R.id.layout_b).setVisibility(View.VISIBLE);
						findViewById(R.id.layout_a).setVisibility(View.GONE);
						break;
				}
			}
		});
		rg_hunyin.setOnCheckedChangeListener(onCheckedChangeListener);
		rg_zhengzhi.setOnCheckedChangeListener(onCheckedChangeListener);
		rg_iscanbao.setOnCheckedChangeListener(onCheckedChangeListener);
		rg_isfubingyi.setOnCheckedChangeListener(onCheckedChangeListener);
		rg_isfuqitongxing.setOnCheckedChangeListener(onCheckedChangeListener);
		rg_isjunzhu.setOnCheckedChangeListener(onCheckedChangeListener);
		if (isHandleID){
			rl_shifoubanzheng.setVisibility(View.VISIBLE);
		}else{
			rb_banzhengfou.setChecked(true);
			rb_banzhengshi.setChecked(false);
			rl_shifoubanzheng.setVisibility(View.GONE);

		}
		rg_isbanzheng.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				switch (i){
					case R.id.rb_banzhengshi:
						navigationLayout.setRightText(right_name, new OnClickListener() {
							@Override
							public void onClick(View view) {
								hideSoftKeyBord();
								save();
							}
						});
						break;
					case R.id.rb_banzhengfou:
						navigationLayout.setRightText(isHandleID?"登记":"变更", new OnClickListener() {
							@Override
							public void onClick(View view) {
								hideSoftKeyBord();
								save();
							}
						});
						break;
				}
			}
		});
		if (isHandleID) {
			hunyin="未婚";zhengzhi="群众";shifoucanbao="否";fubingyi="否";juzhuleibie="否";shifoulingzheng="是";fuqitongxing="否";shengyuzhuangkuang="未育";
		}else{
//			tv_degree.setText("");tv_chusuoleixing.setText("");tv_zanzhushiyou.setText("");
		}

		rc_bianhao.setChecked(SharePrefUtil.getBoolean(HandleIDActivity.this,Constants.IS_BIANHAO_STR,false));
		rc_dizhi.setChecked(SharePrefUtil.getBoolean(HandleIDActivity.this,Constants.IS_ZANZHUDIZHI_STR,false));
		rc_shihao.setChecked(SharePrefUtil.getBoolean(HandleIDActivity.this,Constants.IS_SHIHAO_STR,false));
		rc_chusuoleixing.setChecked(SharePrefUtil.getBoolean(HandleIDActivity.this,Constants.IS_CHUSUOLEIXING_STR,false));
		rc_fuwuchusuo.setChecked(SharePrefUtil.getBoolean(HandleIDActivity.this,Constants.IS_FUWUCHUSUO_STR,false));
		rc_danweidizhi.setChecked(SharePrefUtil.getBoolean(HandleIDActivity.this,Constants.IS_DANWEIDIZHI_STR,false));
		if (realHouseOne==null){
			if (rc_bianhao.isChecked()){
				edt_bianhao.setText(SharePrefUtil.getString(HandleIDActivity.this,Constants.BIANHAO_STR,fangwubiaohao));
				fangwubiaohao=SharePrefUtil.getString(HandleIDActivity.this,Constants.BIANHAO_STR,fangwubiaohao);
			}
			if (rc_dizhi.isChecked()){
				zanzhudizhi=SharePrefUtil.getString(HandleIDActivity.this,Constants.ZANZHUDIZHI_STR,zanzhudizhi);
				edt_dizhi.setText(SharePrefUtil.getString(HandleIDActivity.this,Constants.ZANZHUDIZHI_STR,zanzhudizhi));
			}
		}
		if (rc_shihao.isChecked()){
			shihao=SharePrefUtil.getString(HandleIDActivity.this,Constants.SHIHAO_STR,shihao);
			edt_shihao.setText(SharePrefUtil.getString(HandleIDActivity.this,Constants.SHIHAO_STR,shihao));
		}
		if (rc_chusuoleixing.isChecked()){
			chusuoleixing=SharePrefUtil.getString(HandleIDActivity.this,Constants.CHUSUOLEIXING_STR,chusuoleixing);
			tv_chusuoleixing.setText(SharePrefUtil.getString(HandleIDActivity.this,Constants.CHUSUOLEIXING_STR,chusuoleixing));
		}
		if (rc_fuwuchusuo.isChecked()){
			fuwuchusuo=SharePrefUtil.getString(HandleIDActivity.this,Constants.FUWUCHUSUO_STR,fuwuchusuo);
			edt_fuwuchusuo.setText(SharePrefUtil.getString(HandleIDActivity.this,Constants.FUWUCHUSUO_STR,fuwuchusuo));
		}
		if (rc_danweidizhi.isChecked()){
			danweidizhi=SharePrefUtil.getString(HandleIDActivity.this,Constants.DANWEIDIZHI_STR,danweidizhi);
			edt_danweidizhi.setText(SharePrefUtil.getString(HandleIDActivity.this,Constants.DANWEIDIZHI_STR,danweidizhi));
		}
		rc_bianhao.setOnCheckedChangeListener(new RightButtonCheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharePrefUtil.saveBoolean(HandleIDActivity.this,Constants.IS_BIANHAO_STR,b);
			}
		});
		rc_dizhi.setOnCheckedChangeListener(new RightButtonCheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharePrefUtil.saveBoolean(HandleIDActivity.this,Constants.IS_ZANZHUDIZHI_STR,b);
			}
		});
		rc_shihao.setOnCheckedChangeListener(new RightButtonCheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharePrefUtil.saveBoolean(HandleIDActivity.this,Constants.IS_SHIHAO_STR,b);
			}
		});
		rc_chusuoleixing.setOnCheckedChangeListener(new RightButtonCheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharePrefUtil.saveBoolean(HandleIDActivity.this,Constants.IS_CHUSUOLEIXING_STR,b);
			}
		});
		rc_fuwuchusuo.setOnCheckedChangeListener(new RightButtonCheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharePrefUtil.saveBoolean(HandleIDActivity.this,Constants.IS_FUWUCHUSUO_STR,b);
			}
		});
		rc_danweidizhi.setOnCheckedChangeListener(new RightButtonCheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharePrefUtil.saveBoolean(HandleIDActivity.this,Constants.IS_DANWEIDIZHI_STR,b);
			}
		});

		if (realHouseOne!=null) {
			edt_bianhao.setText(realHouseOne.scode.trim());
			edt_dizhi.setText(realHouseOne.houseAdress.trim());
			fangwubiaohao=realHouseOne.scode.trim();
			zanzhudizhi=realHouseOne.houseAdress.trim();
		}
		if(renyuanInhouseOne!=null){
			edt_bianhao.setText(renyuanInhouseOne.house_code.trim());
			edt_dizhi.setText(renyuanInhouseOne.house_addr.trim());
			fangwubiaohao=renyuanInhouseOne.house_code.trim();
			zanzhudizhi=renyuanInhouseOne.house_addr.trim();
		}
	}
	private void save(){
		height=edt_stature.getText().toString().trim();
		shihao=edt_shihao.getText().toString().trim();
		wenhua=tv_degree.getText().toString().trim();
		chusuoleixing=tv_chusuoleixing.getText().toString().trim();
		zanzhushiyou=tv_zanzhushiyou.getText().toString().trim();
		chanyeleixing=tv_chanyeleixing.getText().toString().trim();
		juzhufangshi=tv_juzhufangshi.getText().toString().trim();
		fangdongguanxi=tv_fangdongguanxi.getText().toString().trim();
		jieyucuoshi=tv_jieyucuoshi.getText().toString().trim();
		xianyunnianyue=tv_xianyunnianyue.getText().toString().trim();
		jiaotonggongju=tv_jiaotonggongju.getText().toString().trim();
		fuwuchusuo=edt_fuwuchusuo.getText().toString().trim();
		danweidizhi=edt_danweidizhi.getText().toString().trim();
		dianhua=edt_dianhua.getText().toString().trim();
		chepaihao=edt_chepaihao.getText().toString().trim();
		zujin=edt_zujin.getText().toString().trim();
		qq=edt_qq.getText().toString().trim();
		macaddress=edt_mac.getText().toString().trim();
		shoujichuanhao=edt_shoujichuanhao.getText().toString().trim();
		shoujixinghao=edt_shoujixinghao.getText().toString().trim();
		beiyong1=rb_banzhengshi.isChecked()?"是":"否";
		zinvgeshu=edt_zinvgeshu.getText().toString().trim();
		if (isHandleID) {
			if (height.equals("")) {
				Toast.makeText(this, "请填写身高~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!CommonUtils.isTrueHigh(height)) {
				Toast.makeText(this, "身高填写有误~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (wenhua.equals("")) {
				Toast.makeText(this, "请选择文化程度~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (fangwubiaohao.equals("")) {
				Toast.makeText(this, "请选择房屋地址~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (shihao.equals("")) {
				Toast.makeText(this, "请输入室号~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (shihao.length()!=4) {
				Toast.makeText(this, "请输入4位的室号~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (fuwuchusuo.equals("")) {
				Toast.makeText(this, "请填写服务处所~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (danweidizhi.equals("")) {
				Toast.makeText(this, "请填写单位地址~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (canbaoshijian.equals("")) {
				if (shifoucanbao.equals("是")) {
					Toast.makeText(this, "请选择参保时间~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (dianhua.equals("")) {
				Toast.makeText(this, "请填写电话号码~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!CommonUtils.isTEL(dianhua)) {
				Toast.makeText(this, "电话号码格式错误，请核实~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!hunyin.equals("未婚")) {
				if (shengyuzhuangkuang.equals("已育")) {
					if (zinvgeshu.equals("")||zinvgeshu.equals("0")) {
						Toast.makeText(this, "请填写子女个数~", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}
			if (hunyin.equals("已婚")) {
				if (jieyucuoshi.equals("")) {
					Toast.makeText(this, "请填选择节育措施~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(chepaihao)) {
				if (!CommonUtils.isCarNo(chepaihao)) {
					Toast.makeText(this, "车牌号格式有误~", Toast.LENGTH_SHORT).show();
					return;
				}	
			}
			SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
			String date =sDateFormat.format(new java.util.Date());
			SharePrefUtil.saveString(HandleIDActivity.this, "realroomcode", shihao);
			if (rc_bianhao.isChecked())
				SharePrefUtil.saveString(HandleIDActivity.this, Constants.BIANHAO_STR, fangwubiaohao);
			if (rc_dizhi.isChecked())
				SharePrefUtil.saveString(HandleIDActivity.this, Constants.ZANZHUDIZHI_STR, zanzhudizhi);
			if (rc_shihao.isChecked())
				SharePrefUtil.saveString(HandleIDActivity.this, Constants.SHIHAO_STR, shihao);
			if (rc_chusuoleixing.isChecked())
				SharePrefUtil.saveString(HandleIDActivity.this, Constants.CHUSUOLEIXING_STR, chusuoleixing);
			if (rc_fuwuchusuo.isChecked())
				SharePrefUtil.saveString(HandleIDActivity.this, Constants.FUWUCHUSUO_STR, fuwuchusuo);
			if (rc_danweidizhi.isChecked())
				SharePrefUtil.saveString(HandleIDActivity.this, Constants.DANWEIDIZHI_STR, danweidizhi);
			people2=new People(people.name, people.cardno, people.people, people.sex, people.birthday, people.address, people.picture, people.hjdxz, 
					isHandleID?"登记":"变更", CommonUtils.GenerateGUID(), isHandleID?"0":"1", people.card_type, people.user_id, "1", height, wenhua, hunyin, zhengzhi,
					fangwubiaohao, zanzhudizhi,shihao , chusuoleixing, zanzhushiyou, fuwuchusuo, danweidizhi, chanyeleixing, shifoucanbao, canbaoshijian, dianhua, fubingyi,
					juzhuleibie, juzhufangshi, fangdongguanxi, fuqitongxing, shengyuzhuangkuang, zinvgeshu, jieyucuoshi, xianyunnianyue, shifoulingzheng, jiaotonggongju, 
					chepaihao, zujin, qq, macaddress, jieyucuoshi, people.sq_name, "", "", "", shoujixinghao, shoujichuanhao, beiyong1,date);
			if (rb_banzhengshi.isChecked()) {
				PopBianzheng popBianzheng = new PopBianzheng(HandleIDActivity.this, new OnbEnsureClickListener() {
					@Override
					public void onEnsureClick(String juzhu, String lingqu, String shenq) {
						people2.setJuZhuShiJian(juzhu);
						people2.setLingQuFangShi(lingqu);
						people2.setShenQingLeiBie(shenq);
						try {
							dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", people2.cardno));
							dbUtils.save(people2);
							Toast.makeText(HandleIDActivity.this, "登记成功！", Toast.LENGTH_SHORT).show();
							HandleIDActivity.this.finish();
						} catch (DbException e) {
							Toast.makeText(HandleIDActivity.this, "数据库操作失败！请发送掉全部数据,并下载全库地址！", Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
					}
				});
				popBianzheng.showAtLocation(findViewById(R.id.root), Gravity.CENTER, 0, 0);
			}else{
				try {
					dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", people2.cardno));
					dbUtils.save(people2);
					Toast.makeText(HandleIDActivity.this, "登记成功！", Toast.LENGTH_SHORT).show();
					HandleIDActivity.this.finish();
				} catch (DbException e) {
					Toast.makeText(HandleIDActivity.this, "数据库操作失败！请发送掉全部数据,并下载全库地址！", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}else{
			if (fangwubiaohao.equals("")) {
				Toast.makeText(this, "请选择房屋地址~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (shihao.equals("")) {
				Toast.makeText(this, "请输入室号~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (shihao.length()!=4) {
				Toast.makeText(this, "请输入4位的室号~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!height.equals("")) {
				if (!CommonUtils.isTrueHigh(height)) {
					Toast.makeText(this, "身高填写有误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!dianhua.equals("")) {
				if (!CommonUtils.isTEL(dianhua)) {
					Toast.makeText(this, "电话号码格式错误，请核实~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			SharePrefUtil.saveString(HandleIDActivity.this, "realroomcode", shihao);
			SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
			String date =sDateFormat.format(new java.util.Date());
			if (!TextUtils.isEmpty(chepaihao)) {
				if (!CommonUtils.isCarNo(chepaihao)) {
					Toast.makeText(this, "车牌号格式有误~", Toast.LENGTH_SHORT).show();
					return;
				}	
			}
			if (realHouseOne!=null||renyuanInhouseOne!=null) {
				if (shihao.length()!=4) {
					Toast.makeText(this, "请输入4位的室号~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			try {
				List<People> tlist=dbUtils.findAll(Selector.from(People.class));
				if (tlist==null){
					dbUtils.dropTable(People.class);
				}else if (tlist.size()==0){
					dbUtils.dropTable(People.class);
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
			if (rc_bianhao.isChecked())SharePrefUtil.saveString(HandleIDActivity.this,Constants.BIANHAO_STR,fangwubiaohao);
			if (rc_dizhi.isChecked())SharePrefUtil.saveString(HandleIDActivity.this,Constants.ZANZHUDIZHI_STR,zanzhudizhi);
			if (rc_shihao.isChecked())SharePrefUtil.saveString(HandleIDActivity.this,Constants.SHIHAO_STR,shihao);
			if (rc_chusuoleixing.isChecked())SharePrefUtil.saveString(HandleIDActivity.this,Constants.CHUSUOLEIXING_STR,chusuoleixing);
			if (rc_fuwuchusuo.isChecked())SharePrefUtil.saveString(HandleIDActivity.this,Constants.FUWUCHUSUO_STR,fuwuchusuo);
			if (rc_danweidizhi.isChecked())SharePrefUtil.saveString(HandleIDActivity.this,Constants.DANWEIDIZHI_STR,danweidizhi);
			people2=new People(people.name, people.cardno, people.people, people.sex, people.birthday, people.address, people.picture, people.hjdxz,
					isHandleID?"登记":"变更", CommonUtils.GenerateGUID(), isHandleID?"0":"1", people.card_type, people.user_id, "1", height, wenhua, hunyin, zhengzhi,
					fangwubiaohao, zanzhudizhi,shihao , chusuoleixing, zanzhushiyou, fuwuchusuo, danweidizhi, chanyeleixing, shifoucanbao, canbaoshijian, dianhua, fubingyi,
					juzhuleibie, juzhufangshi, fangdongguanxi, fuqitongxing, shengyuzhuangkuang, zinvgeshu, jieyucuoshi, xianyunnianyue, shifoulingzheng, jiaotonggongju, 
					chepaihao, zujin, qq, macaddress, jieyucuoshi, people.sq_name, "", "", "", shoujixinghao, shoujichuanhao, beiyong1,date,getIntent().getBooleanExtra("isAdd",false)?"":"1",people.realId);
			try {
				dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", people2.cardno));
				dbUtils.save(people2);
				Toast.makeText(this, "变更成功！", Toast.LENGTH_SHORT).show();
				HandleIDActivity.this.finish();
			} catch (DbException e) {
				Toast.makeText(HandleIDActivity.this, "数据库需更新,请先发送所有待发送数据再重试！", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
		}
	}
	@Event(value={R.id.edt_bianhao,R.id.edt_dizhi})
	private void fangwubianhaoClick(View view){
		hideSoftKeyBord();
		try {
			List<HouseAddressOldBean> list=dbUtils.findAll(HouseAddressOldBean.class);
			if (list==null) {
				Dialog.showSelectDialog(HandleIDActivity.this, "未下载地址库,请先下载全库地址~", new DialogClickListener() {
					@Override
					public void confirm() {}
					@Override
					public void cancel() {}
				});
				return;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (popHouse==null) {
			popHouse = new PopHouse(this, new OnHouseEnsureClickListener() {
				@Override
				public void OnHouseEnSureClick(String bianhao, String dizhi) {
					edt_bianhao.setText(bianhao);
					edt_dizhi.setText(dizhi);
					fangwubiaohao=bianhao;
					zanzhudizhi=dizhi;
				}
			});
		}
		popHouse.showAtLocation(findViewById(R.id.root), Gravity.CENTER_VERTICAL, 0, 0);
	}
	@Event(value={R.id.rl_degree})
	private void degreeClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.DEGREE, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_degree.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	
	@Event(value={R.id.rl_chusuoleixing})//处所类型
	private void chushuoleixingClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.CHUSHUOLEIXING, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_chusuoleixing.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.rl_zanzhushiyou})//暂住事由
	private void zanzhushiyouClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.ZANZHUSHIYOU, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_zanzhushiyou.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.rl_chanyeleixing})//产业类型
	private void chanyeleixingClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.CHANYELEIXING, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_chanyeleixing.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.rl_juzhufangshi})//居住方式
	private void juzhufangshiClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.JUZHUFANGSHI, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_juzhufangshi.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.rl_fangdongguanxi})//房东关系
	private void fangdongguanxiClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.FANGDONGGUANXI, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_fangdongguanxi.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.rl_jieyucuoshi})//节育措施
	private void jieyucuoshiClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.JIEYUCUOSHI, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_jieyucuoshi.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.rl_jiaotonggongju})//交通工具
	private void jiaotonggongjuClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.JIAOTONGGONGJU, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_jiaotonggongju.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.ib_addmember})
	private void addMemberClick(View view){
		View member=getMemberView();
		switch (ll_member.getChildCount()){
			case 0:
				memberView1=member;
				break;
			case 1:
				memberView2=member;
				break;
			case 2:
				memberView3=member;
				break;
			case 3:
				memberView4=member;
				break;
		}
		((TextView)member.findViewById(R.id.tv_number)).setText((ll_member.getChildCount()+1)+"");
		if (ll_member.getChildCount()>=4){
			Toast.makeText(HandleIDActivity.this,"最多添加四名子女！",Toast.LENGTH_SHORT).show();
			return;
		}
		ll_member.addView(member);
		sv_b.post(new Runnable() {
			@Override
			public void run() {
				sv_b.smoothScrollTo(0,CommonUtils.dip2px(HandleIDActivity.this,232)+sv_b.getScrollY());
			}
		});

	}
	@Event(value={R.id.rl_canbaoshijian})//参保时间
	private void canbaoshijianClick(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenercanbao,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	@Event(value={R.id.rl_xianyunnianyue})//现孕年月
	private void xianyunnianyueClick(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListeneryun,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenercanbao =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			tv_canbaoshijian.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			canbaoshijian=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
	private OnDateSetListener onDateSetListeneryun =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			tv_xianyunnianyue.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1))));
			xianyunnianyue=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)));
		}
	};
	OnCheckedChangeListener onCheckedChangeListener=new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (group.getId()) {
			case R.id.rg_hunyin://婚姻
				switch (checkedId) {
				case R.id.rb_weihun:
					Log.d(TAG, "未婚");
					ll_hunhou.setVisibility(View.GONE);
					edt_zinvgeshu.setText("0");
					zinvgeshu="0";
					hunyin="未婚";
					break;
				case R.id.rb_yihun:
					Log.d(TAG, "已婚");
					ll_hunhou.setVisibility(View.VISIBLE);
					hunyin="己婚";
					break;
				case R.id.rb_lihun:
					Log.d(TAG, "离婚");
					ll_hunhou.setVisibility(View.VISIBLE);
					hunyin="离婚";
					break;
				default:
					break;
				}
				break;
			case R.id.rg_zhengzhi://政治面貌
				switch (checkedId) {
				case R.id.rb_qunzhong:
					Log.d(TAG, "群众");
					zhengzhi="群众";
					break;
				case R.id.rb_tuanyuan:
					Log.d(TAG, "团员");
					zhengzhi="团员";
					break;
				case R.id.rb_dangyuan:
					Log.d(TAG, "党员");
					zhengzhi="党员";
					break;
				default:
					break;
				}
				break;
			case R.id.rg_iscanbao://是否参保
				switch (checkedId) {
				case R.id.rb_foucanbao:
//					rl_canbaoshijian.setFocusable(false);
//					rl_canbaoshijian.setFocusableInTouchMode(false);
					rl_canbaoshijian.setVisibility(View.GONE);
					tv_canbaoshijian.setText("");
					shifoucanbao="否";
					break;
				case R.id.rb_shicanbao:
//					rl_canbaoshijian.setFocusable(true);
//					rl_canbaoshijian.setFocusableInTouchMode(true);
					rl_canbaoshijian.setVisibility(View.VISIBLE);
					shifoucanbao="是";
					break;
				default:
					break;
				}
				break;
			case R.id.rg_isfubingyi://是否服兵役
				switch (checkedId) {
				case R.id.rb_foufubingyi:
					fubingyi="否";
					break;
				case R.id.rb_shifubingyi:
					fubingyi="是";
					break;
				default:
					break;
				}
				break;
			case R.id.rg_isjunzhu://居住类别
				switch (checkedId) {
				case R.id.rb_foujuzhu:
					juzhuleibie="否";
					break;
				case R.id.rb_shijuzhuleibie:
					juzhuleibie="是";
					break;
				default:
					break;
				}
				break;
			case R.id.rg_islingzheng://是否领证
				switch (checkedId) {
				case R.id.rb_foulingzheng:
					shifoulingzheng="否";
					break;
				case R.id.rb_shilingzheng:
					shifoulingzheng="是";
					break;
				default:
					break;
				}
				break;
			case R.id.rg_isfuqitongxing://夫妻同行
				switch (checkedId) {
				case R.id.rb_foufuqingtongxing:
					fuqitongxing="否";
					break;
				case R.id.rb_shifuqitongxing:
					fuqitongxing="是";
					break;
				default:
					break;
				}
				break;
			case R.id.rg_shenyuqingkuang://生育状况
				switch (checkedId) {
				case R.id.rb_yiyu:
					shengyuzhuangkuang="已育";
					break;
				case R.id.rb_weiyu:
					shengyuzhuangkuang="未育";
					edt_zinvgeshu.setText("0");
					zinvgeshu="0";
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	};
	private PopHouse popHouse;
	private void hideSoftKeyBord(){
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
