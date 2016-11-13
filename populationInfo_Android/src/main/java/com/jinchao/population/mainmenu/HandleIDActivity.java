package com.jinchao.population.mainmenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.jinchao.population.MyApplication;
import com.jinchao.population.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.jinchao.population.base.BaseHandleIDActivity;
import com.jinchao.population.dbentity.HouseJLX;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.entity.HouseAddressOldBean;
import com.jinchao.population.entity.RenyuanInHouseBean;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.view.PicRepacePop;
import com.jinchao.population.view.PopBianzheng.OnbEnsureClickListener;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RealHouseBean.RealHouseOne;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.view.StringWheel.OnEnsureClickListener;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.PopBianzheng;
import com.jinchao.population.view.PopCanBaoQingKuang;
import com.jinchao.population.view.PopHouse;
import com.jinchao.population.view.PopHouse.OnHouseEnsureClickListener;
import com.jinchao.population.view.StringWheel;
import com.jinchao.population.widget.MonPickerDialog;
import com.jinchao.population.widget.RightButtonCheckBox;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.soundcloud.android.crop.Crop;

import de.greenrobot.event.EventBus;


@ContentView(R.layout.activity_handleid)
public class HandleIDActivity extends BaseHandleIDActivity{
	public static final String TAG="HANDLEID";
	@ViewInject(R.id.rg_tab)private RadioGroup rg_tab;
	@ViewInject(R.id.edt_stature)private ValidateEidtText edt_stature;
	@ViewInject(R.id.edt_shihao)private ValidateEidtText edt_shihao;
	@ViewInject(R.id.edt_fuwuchusuo)private EditText edt_fuwuchusuo;
	@ViewInject(R.id.edt_danweidizhi)private EditText edt_danweidizhi;
	@ViewInject(R.id.edt_dianhua)private ValidateEidtText edt_dianhua;
	@ViewInject(R.id.edt_chepaihao)private ValidateEidtText edt_chepaihao;
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
	@ViewInject(R.id.rg_shenyuqingkuang)private RadioGroup rg_shenyuqingkuang;
	@ViewInject(R.id.rg_jieyu)private RadioGroup rg_jieyu;
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
//新增A类
	@ViewInject(R.id.edt_fzxm)private EditText edt_fzxm;
	@ViewInject(R.id.rb_shiyimiao)private RadioButton rb_shiyimiao;
	@ViewInject(R.id.tv_djrq)private TextView tv_djrq;
	@ViewInject(R.id.tv_cbqk)private TextView tv_cbqk;
//B类项
	@ViewInject(R.id.edt_jyrq)private TextView edt_jyrq;
	@ViewInject(R.id.edt_hyqfrq)private TextView edt_hyqfrq;
	@ViewInject(R.id.edt_jhrq)private TextView edt_jhrq;
	@ViewInject(R.id.edt_bycsrq)private TextView edt_bycsrq;
	@ViewInject(R.id.edt_czqx1)private TextView edt_czqx1;
	@ViewInject(R.id.edt_czqx2)private TextView edt_czqx2;
	@ViewInject(R.id.tv_lsrq)private TextView tv_lsrq;
	@ViewInject(R.id.tv_zjdq)private TextView tv_zjdq;

	@ViewInject(R.id.edt_msn)private ValidateEidtText edt_msn;
	@ViewInject(R.id.edt_email)private ValidateEidtText edt_email;
	@ViewInject(R.id.edt_addition)private EditText edt_addition;
	@ViewInject(R.id.edt_fzdh)private ValidateEidtText edt_fzdh;
	@ViewInject(R.id.edt_fzsfz)private ValidateEidtText edt_fzsfz;
	@ViewInject(R.id.edt_dwlxdh)private ValidateEidtText edt_dwlxdh;
	@ViewInject(R.id.edt_zwmc)private ValidateEidtText edt_zwmc;
	@ViewInject(R.id.edt_sbbh)private EditText edt_sbbh;
	@ViewInject(R.id.edt_jkzbh)private EditText edt_jkzbh;
	@ViewInject(R.id.edt_dwfzr)private EditText edt_dwfzr;
	@ViewInject(R.id.edt_hyzmbh)private EditText edt_hyzmbh;
	@ViewInject(R.id.edt_yfjzzh)private EditText edt_yfjzzh;
	@ViewInject(R.id.edt_fwkh)private EditText edt_fwkh;
	@ViewInject(R.id.edt_beizhu2)private EditText edt_beizhu2;

	@ViewInject(R.id.tv_ldhtqj)private TextView tv_ldhtqj;
	@ViewInject(R.id.tv_hyzmzl)private TextView tv_hyzmzl;
	@ViewInject(R.id.ll_guanxi)private LinearLayout ll_guanxi;
	@ViewInject(R.id.ll_member)private LinearLayout ll_member;
	@ViewInject(R.id.layout_b)private ScrollView sv_b;
	private People people,people2;
	private Calendar c;
	private boolean isHandleID;
	private PopHouse popHouse;
    String pic="";
	private String height="",wenhua="",hunyin="",zhengzhi="",fangwubiaohao="",
			zanzhudizhi="",shihao="",chusuoleixing="",zanzhushiyou="",fuwuchusuo="",danweidizhi="",
			canyeleixing="",chanyeleixing="",shifoucanbao="",canbaoshijian="",dianhua="",fubingyi="",
			juzhuleibie="",juzhufangshi="",fangdongguanxi="",fuqitongxing="",shengyuzhuangkuang="",
			zinvgeshu="",jieyucuoshi="",xianyunnianyue="",shifoulingzheng="",jiaotonggongju="",
			chepaihao="",zujin="",qq="",macaddress="",shoujixinghao="",shoujichuanhao="",beiyong1="",currentTime="";

	private RealHouseOne realHouseOne;
	private RenyuanInHouseBean.RenyuanInhouseOne renyuanInhouseOne;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new InitData().execute();
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
		navigationLayout.setRightText(isHandleID?"办证":"变更", new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftKeyBord();
				save();
			}
		});

		c = Calendar.getInstance();
        currentTime=c.get(Calendar.YEAR)+""+ ((c.get(Calendar.MONTH)+1)>9?((c.get(Calendar.MONTH)+1)+""):("0"+(c.get(Calendar.MONTH)+1)))+(c.get(Calendar.DAY_OF_MONTH)>9?(c.get(Calendar.DAY_OF_MONTH)+""):("0"+c.get(Calendar.DAY_OF_MONTH)));
		tv_djrq.setText(c.get(Calendar.YEAR)+""+ ((c.get(Calendar.MONTH)+1)>9?((c.get(Calendar.MONTH)+1)+""):("0"+(c.get(Calendar.MONTH)+1)))+(c.get(Calendar.DAY_OF_MONTH)>9?(c.get(Calendar.DAY_OF_MONTH)+""):("0"+c.get(Calendar.DAY_OF_MONTH))));
        djrq=c.get(Calendar.YEAR)+""+ ((c.get(Calendar.MONTH)+1)>9?((c.get(Calendar.MONTH)+1)+""):("0"+(c.get(Calendar.MONTH)+1)))+(c.get(Calendar.DAY_OF_MONTH)>9?(c.get(Calendar.DAY_OF_MONTH)+""):("0"+c.get(Calendar.DAY_OF_MONTH)));
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
		rg_shenyuqingkuang.setOnCheckedChangeListener(onCheckedChangeListener);
		rg_jieyu.setOnCheckedChangeListener(onCheckedChangeListener);
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
						navigationLayout.setRightText(isHandleID?"登记":"变更", new OnClickListener() {
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
			tv_cbqk.setText("没有参保");
			hunyin="未婚";sfjy="2";cbqk="00000010";zhengzhi="群众";shifoucanbao="否";fubingyi="否";juzhuleibie="否";shifoulingzheng="是";fuqitongxing="否";shengyuzhuangkuang="未育";
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
		fzxm=edt_fzxm.getText().toString().trim();
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
		zinvgeshu=zinvgeshu.length()==1?"0"+zinvgeshu:zinvgeshu;
		//A类
		jqjzym=rb_shiyimiao.isChecked()?"1":"0";
		//B类
		MSN=edt_msn.getText().toString().trim();
		Email=edt_email.getText().toString().trim();
		czwxz=edt_addition.getText().toString().trim();
		fzdh=edt_fzdh.getText().toString().trim();
		fzsfz=edt_fzsfz.getText().toString().trim();
		dwlxdh=edt_dwlxdh.getText().toString().trim();
		zymc=edt_zwmc.getText().toString().trim();
		sbbh=edt_sbbh.getText().toString().trim();
		jkzbh=edt_jkzbh.getText().toString().trim();
		dwfzr=edt_dwfzr.getText().toString().trim();
		hyzmbh=edt_hyzmbh.getText().toString().trim();
		yfjzzh=edt_yfjzzh.getText().toString().trim();
		fwkh=edt_fwkh.getText().toString().trim();
		beizhu2=edt_beizhu2.getText().toString().trim();
		if(memberView1!=null){
			xdr1="子女";
			xdxm1=memberName1.getText().toString().trim();
			xdxb1=memberSex1.isChecked()?"1":"2";
			xdrq1=memberBirth1.getText().toString().trim();
			xdsfz1=memberSfz1.getText().toString().trim();
		}
		if(memberView2!=null){
			xdr2="子女";
			xdxm2=memberName2.getText().toString().trim();
			xdxb2=memberSex2.isChecked()?"1":"2";
			xdrq2=memberBirth2.getText().toString().trim();
			xdsfz2=memberSfz2.getText().toString().trim();
		}
		if(memberView3!=null){
			xdr3="子女";
			xdxm3=memberName3.getText().toString().trim();
			xdxb3=memberSex3.isChecked()?"1":"2";
			xdrq3=memberBirth3.getText().toString().trim();
			xdsfz3=memberSfz3.getText().toString().trim();
		}
		if(memberView4!=null){
			xdr4="子女";
			xdxm4=memberName4.getText().toString().trim();
			xdxb4=memberSex4.isChecked()?"1":"2";
			xdrq4=memberBirth4.getText().toString().trim();
			xdsfz4=memberSfz4.getText().toString().trim();
		}
		if(relationView1!=null){
			gxxm1=zinvName1.getText().toString().trim();
			gxxb1=zinvSex1.isChecked()?"1":"2";
			gxrq1=zinvBirth1.getText().toString().trim();
			gxsfz1=zinvSfz1.getText().toString().trim();
			gxrjzk1=zinvJiezhong1.isChecked()?"1":"2";
		}
		if(relationView2!=null){
			gxxm2=zinvName2.getText().toString().trim();
			gxxb2=zinvSex2.isChecked()?"1":"2";
			gxrq2=zinvBirth2.getText().toString().trim();
			gxsfz2=zinvSfz2.getText().toString().trim();
			gxrjzk2=zinvJiezhong2.isChecked()?"1":"2";
		}
		if(relationView3!=null){
			gxxm3=zinvName3.getText().toString().trim();
			gxxb3=zinvSex3.isChecked()?"1":"2";
			gxrq3=zinvBirth3.getText().toString().trim();
			gxsfz3=zinvSfz3.getText().toString().trim();
			gxrjzk3=zinvJiezhong3.isChecked()?"1":"2";
		}
		if(relationView4!=null){
			gxxm4=zinvName4.getText().toString().trim();
			gxxb4=zinvSex4.isChecked()?"1":"2";
			gxrq4=zinvBirth4.getText().toString().trim();
			gxsfz4=zinvSfz4.getText().toString().trim();
			gxrjzk4=zinvJiezhong4.isChecked()?"1":"2";

		}
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
			if (shifoucanbao.equals("是")){
				if (TextUtils.isEmpty(cbqk)){
					Toast.makeText(this, "请选择参保情况~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (dianhua.equals("")) {
				Toast.makeText(this, "请填写电话号码~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!CommonUtils.isGuangdaTel(dianhua)) {
				Toast.makeText(this, "电话号码格式错误，请核实~", Toast.LENGTH_SHORT).show();
				return;
			}

            if (zinvgeshu.equals("")) {
                Toast.makeText(this, "请填写子女个数~", Toast.LENGTH_SHORT).show();
                return;
            }
            if (shengyuzhuangkuang.equals("已育")) {
					if (zinvgeshu.equals("")||zinvgeshu.equals("0")) {
						Toast.makeText(this, "请填写子女个数~", Toast.LENGTH_SHORT).show();
						return;
					}
            }
			if (shengyuzhuangkuang.equals("未育")&&(!zinvgeshu.equals("00"))){
				Toast.makeText(this, "未育，子女个数不可大于0~", Toast.LENGTH_SHORT).show();
				return;
			}
			if (hunyin.equals("己婚")) {
				if (sfjy.equals("1")) {
					if (jieyucuoshi.equals("")) {
						Toast.makeText(this, "请填选择节育措施~", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}
            if (jiaotonggongju.equals("汽车")||jiaotonggongju.equals("摩托车")){
                if(!CommonUtils.isCarAndMotoNo(chepaihao)){
                    Toast.makeText(this, "车牌号格式有误~", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
			if (!TextUtils.isEmpty(chepaihao)) {
				if (!CommonUtils.isCarNo(chepaihao)) {
					Toast.makeText(this, "车牌号格式有误~", Toast.LENGTH_SHORT).show();
					return;
				}	
			}

			if (!TextUtils.isEmpty(MSN)){
				if (!CommonUtils.isEmail(MSN)){
					Toast.makeText(this, "MSN账号格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(Email)){
				if (!CommonUtils.isEmail(Email)){
					Toast.makeText(this, "邮箱格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(fzdh)){
				if (!CommonUtils.isGuangdaTel(fzdh)){
					Toast.makeText(this, "房主电话格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(fzsfz)){
				if (CommonIdcard.validateCard(fzsfz)) {
					if (fzsfz.length() == 15) {
						fzsfz = CommonIdcard.conver15CardTo18(fzdh);
						edt_fzsfz.setText(fzsfz);
						Toast.makeText(HandleIDActivity.this, "15位转18位证件号成功",Toast.LENGTH_SHORT).show();
					} else if (fzsfz.length() == 17) {
						fzsfz = CommonIdcard.conver17CardTo18(fzsfz);
						edt_fzsfz.setText(fzsfz);
						Toast.makeText(HandleIDActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HandleIDActivity.this, "请先输入合法的身份证号", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(dwlxdh)){
				if (!CommonUtils.isGuangdaTel(dwlxdh)){
					Toast.makeText(this, "单位联系电话格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(zymc)){
				if (!(zymc.trim().length()<=5&&CommonUtils.isChinese(zymc))){
					Toast.makeText(this, "职位名称5个汉字以内~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(sbbh)){
				if (!CommonUtils.isSBBH(sbbh)){
					Toast.makeText(this, "社保卡18个字符或9个汉字~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(jkzbh)){
				if (!CommonUtils.isJKZBH(jkzbh)){
					Toast.makeText(this, "健康证10个字符或5个汉字~", Toast.LENGTH_SHORT).show();
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
					chepaihao, zujin, qq, macaddress, jieyucuoshi, people.sq_name, "", "", "", shoujixinghao, shoujichuanhao, beiyong1,date,MSN,Email,czwxz,lsrq,djrq,fzxm,fzdh,fzsfz,dwlxdh,zymc,ldhtqj,sbbh,
					jyrq,jkzbh,dwfzr,sfjy,hyzmzl,hyzmbh,jqjzym,hyqfrq,jhrq,yfjzzh,fwkh,bycsrq,czqx1,czqx2,
					zjdq,beizhu2 ,cbqk,xdr1,xdxm1,xdxb1,xdrq1,xdsfz1,
					xdr2,xdxm2,xdxb2,xdrq2,xdsfz2,
					xdr3,xdxm3,xdxb3,xdrq3,xdsfz3,
					xdr4,xdxm4,xdxb4,xdrq4,xdsfz4,
					gxr1,gxxm1,gxxb1,gxrq1,gxsfz1,gxrjzk1,
					gxr2,gxxm2,gxxb2,gxrq2,gxsfz2,gxrjzk2,
					gxr3,gxxm3,gxxb3,gxrq3,gxsfz3,gxrjzk3,
					gxr4,gxxm4,gxxb4,gxrq4,gxsfz4,gxrjzk4);
			if (rb_banzhengshi.isChecked()) {
				PopBianzheng popBianzheng = new PopBianzheng(HandleIDActivity.this, new OnbEnsureClickListener() {
					@Override
					public void onEnsureClick(String juzhu, String lingqu, String shenq) {
						people2.setJuZhuShiJian(juzhu);
						people2.setLingQuFangShi(lingqu);
						people2.setShenQingLeiBie(shenq);
						if(!people.getIstakephoto().equals("1")){
							Dialog.showSelectDialog(HandleIDActivity.this, "办理居住证必须拍照!", new DialogClickListener() {
								@Override
								public void confirm() {
									Intent intentFromCapture = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									// 判断存储卡是否可以用，可用进行存储
									if (DeviceUtils.hasSDCard()) {
										intentFromCapture.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(new File(Environment
														.getExternalStorageDirectory(),
														IMAGE_FILE_NAME)));
									}
									startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
								}
								@Override
								public void cancel() {
								}
							});
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
                    showToastDialog("数据库操作失败！请发送掉全部数据,并下载全库地址！");
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
			if (shifoucanbao.equals("是")){
				if (TextUtils.isEmpty(cbqk)){
					Toast.makeText(this, "请选择参保情况~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!dianhua.equals("")) {
				if (!CommonUtils.isTEL(dianhua)) {
					Toast.makeText(this, "电话号码格式错误，请核实~", Toast.LENGTH_SHORT).show();
					return;
				}
			}

			if (!TextUtils.isEmpty(MSN)){
				if (!CommonUtils.isEmail(MSN)){
					Toast.makeText(this, "MSN账号格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(Email)){
				if (!CommonUtils.isEmail(Email)){
					Toast.makeText(this, "邮箱格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(fzdh)){
				if (!CommonUtils.isGuangdaTel(fzdh)){
					Toast.makeText(this, "房主电话格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(fzsfz)){
				if (CommonIdcard.validateCard(fzsfz)) {
					if (fzsfz.length() == 15) {
						fzsfz = CommonIdcard.conver15CardTo18(fzdh);
						edt_fzsfz.setText(fzsfz);
						Toast.makeText(HandleIDActivity.this, "15位转18位证件号成功",Toast.LENGTH_SHORT).show();
					} else if (fzsfz.length() == 17) {
						fzsfz = CommonIdcard.conver17CardTo18(fzsfz);
						edt_fzsfz.setText(fzsfz);
						Toast.makeText(HandleIDActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HandleIDActivity.this, "请先输入合法的身份证号", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(dwlxdh)){
				if (!CommonUtils.isGuangdaTel(dwlxdh)){
					Toast.makeText(this, "单位联系电话格式错误~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(zymc)){
				if (!(zymc.trim().length()<=5&&CommonUtils.isChinese(zymc))){
					Toast.makeText(this, "职位名称5个汉字以内~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(sbbh)){
				if (!CommonUtils.isSBBH(sbbh)){
					Toast.makeText(this, "社保卡18个字符或9个汉字~", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (!TextUtils.isEmpty(jkzbh)){
				if (!CommonUtils.isJKZBH(jkzbh)){
					Toast.makeText(this, "健康证10个字符或5个汉字~", Toast.LENGTH_SHORT).show();
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
					chepaihao, zujin, qq, macaddress, jieyucuoshi, people.sq_name, "", "", "", shoujixinghao, shoujichuanhao, beiyong1,date,getIntent().getBooleanExtra("isAdd",false)?"":"1",people.realId,MSN,Email,czwxz,lsrq,djrq,fzxm,fzdh,fzsfz,dwlxdh,zymc,ldhtqj,sbbh,
					jyrq,jkzbh,dwfzr,sfjy,hyzmzl,hyzmbh,jqjzym,hyqfrq,jhrq,yfjzzh,fwkh,bycsrq,czqx1,czqx2,
					zjdq,beizhu2 ,cbqk,xdr1,xdxm1,xdxb1,xdrq1,xdsfz1,
					xdr2,xdxm2,xdxb2,xdrq2,xdsfz2,
					xdr3,xdxm3,xdxb3,xdrq3,xdsfz3,
					xdr4,xdxm4,xdxb4,xdrq4,xdsfz4,
					gxr1,gxxm1,gxxb1,gxrq1,gxsfz1,gxrjzk1,
					gxr2,gxxm2,gxxb2,gxrq2,gxsfz2,gxrjzk2,
					gxr3,gxxm3,gxxb3,gxrq3,gxsfz3,gxrjzk3,
					gxr4,gxxm4,gxxb4,gxrq4,gxsfz4,gxrjzk4);
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
		if (!isHouseAddressInital){
			showProgressDialog("","房屋二级列表初始化中，请稍后...");
			return;
		}
		if (popHouse==null) {
			popHouse = new PopHouse(this,PSCName,USERName,UserId,UserNameMap,UserIdMap, new OnHouseEnsureClickListener() {
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
	@Event(value={R.id.rl_cbqk})//参保情况
	private void cbqkClick(View view){
		if (shifoucanbao.equals("否")){
			return;
		}
		PopCanBaoQingKuang popCanBaoQingKuang=new PopCanBaoQingKuang(this, Constants.CANBAOZHONGLEI, new PopCanBaoQingKuang.OnbEnsureClickListener() {
			@Override
			public void onEnsureClick(String juzhu,String code) {
				tv_cbqk.setText(juzhu);
				cbqk=code;
			}
		});
		popCanBaoQingKuang.showAtLocation(findViewById(R.id.root), Gravity.CENTER, 0, 0);
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

	@Event(value={R.id.rl_ldhtqj})//劳动合同签订情况
	private void ldhtqjClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.LAODONGHETONGQIANDING, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
			tv_ldhtqj.setText(str);
				if (str.equals("一年以下")){
					ldhtqj="1";
				}else if(str.equals("一年及以上")){
					ldhtqj="2";
				}else{
					ldhtqj="0";
				}
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.rl_hyzmzl})//婚育证明种类
	private void hyzmzlClick(View view){
		hideSoftKeyBord();
		StringWheel stringWheel=new StringWheel(this, Constants.HUYUZHENGMING, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				tv_hyzmzl.setText(str);
				if (str.equals("全国婚育证明")){
					hyzmzl="1";
				}else if(str.equals("本地管理服务卡")){
					hyzmzl="2";
				}else{
					hyzmzl="9";
				}
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
	}
	@Event(value={R.id.ib_addmember})
	private void addMemberClick(View view){
		final View member=getMemberView();
		switch (ll_member.getChildCount()){
			case 0://子女1
				memberView1=member;
//				memberGuanxi1=(EditText) member.findViewById(R.id.edt_guanxi);
				memberName1=(EditText) member.findViewById(R.id.edt_name);
				memberBirth1=(TextView) member.findViewById(R.id.tv_birth);
				memberSex1=(RadioButton)member.findViewById(R.id.rb_male);
				memberSfz1=(ValidateEidtText) member.findViewById(R.id.edt_idcard);
//				memberSfz1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//					@Override
//					public void onFocusChange(View v, boolean hasFocus) {
//						if (!hasFocus){
//							if (!TextUtils.isEmpty(memberSfz1.getText().toString().trim())){
//									validateIdCard(memberSfz1,memberSfz1.getText().toString().trim());
//							}
//						}
//					}
//				});
				member.findViewById(R.id.rl_birth).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(HandleIDActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								if (BiggerorEqualCurrent(currentTime,year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)))){
									Toast.makeText(HandleIDActivity.this,"生日必须小于等于当前日期！",Toast.LENGTH_SHORT).show();
									return;
								}
								memberBirth1.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
						monPickerDialogcanbao.show();
					}
				});
				member.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (ll_member.getChildCount()>1){
							Toast.makeText(HandleIDActivity.this,"请先按序号由大到小删除！",Toast.LENGTH_SHORT).show();
							return;
						}
						memberView1=null;
						ll_member.removeViewAt(0);
					}
				});
				break;
			case 1://子女2
				memberView2=member;
//				memberGuanxi2=(EditText) member.findViewById(R.id.edt_guanxi);
				memberName2=(EditText) member.findViewById(R.id.edt_name);
				memberBirth2=(TextView) member.findViewById(R.id.tv_birth);
				memberSex2=(RadioButton)member.findViewById(R.id.rb_male);
				memberSfz2=(ValidateEidtText) member.findViewById(R.id.edt_idcard);

				member.findViewById(R.id.rl_birth).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(HandleIDActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								memberBirth2.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
						monPickerDialogcanbao.show();
					}
				});
				member.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (ll_member.getChildCount()>2){
							Toast.makeText(HandleIDActivity.this,"请先按序号由大到小删除！",Toast.LENGTH_SHORT).show();
							return;
						}
						memberView2=null;
						ll_member.removeViewAt(1);
					}
				});
				break;
			case 2://子女3
				memberView3=member;
				memberName3=(EditText) member.findViewById(R.id.edt_name);
				memberBirth3=(TextView) member.findViewById(R.id.tv_birth);
				memberSex3=(RadioButton)member.findViewById(R.id.rb_male);
				memberSfz3=(ValidateEidtText) member.findViewById(R.id.edt_idcard);
				member.findViewById(R.id.rl_birth).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(HandleIDActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								memberBirth3.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
						monPickerDialogcanbao.show();
					}
				});
				member.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (ll_member.getChildCount()>3){
							Toast.makeText(HandleIDActivity.this,"请先按序号由大到小删除！",Toast.LENGTH_SHORT).show();
							return;
						}
						memberView3=null;
						ll_member.removeViewAt(2);
					}
				});
				break;
			case 3://子女4
				memberView4=member;
				memberName4=(EditText) member.findViewById(R.id.edt_name);
				memberBirth4=(TextView) member.findViewById(R.id.tv_birth);
				memberSex4=(RadioButton)member.findViewById(R.id.rb_male);
				memberSfz4=(ValidateEidtText) member.findViewById(R.id.edt_idcard);
				member.findViewById(R.id.rl_birth).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(HandleIDActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								memberBirth4.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
						monPickerDialogcanbao.show();
					}
				});
				member.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						memberView4=null;
						ll_member.removeViewAt(3);
					}
				});
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
				sv_b.smoothScrollTo(0,CommonUtils.dip2px(HandleIDActivity.this,280)+sv_b.getScrollY());
			}
		});

	}
	@Event(value={R.id.ib_addguanxi})
	private void addGuanxirenClick(View view){
		final View guanxi=getGuanxirenView();
		switch (ll_guanxi.getChildCount()){
			case 0://子女1
				relationView1=guanxi;
				memberGuanxi1=(TextView) guanxi.findViewById(R.id.tv_guanxi);
				zinvName1=(EditText) guanxi.findViewById(R.id.edt_name);
				zinvBirth1=(TextView) guanxi.findViewById(R.id.tv_birth);
				zinvSex1=(RadioButton)guanxi.findViewById(R.id.rb_male);
				zinvSfz1=(ValidateEidtText) guanxi.findViewById(R.id.edt_idcard);
				zinvJiezhong1=(RadioButton) guanxi.findViewById(R.id.rb_you);
				memberGuanxi1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						StringWheel stringWheel=new StringWheel(HandleIDActivity.this, Constants.GUANXIREN, new OnEnsureClickListener() {
							@Override
							public void OnEnSureClick(String str) {
								memberGuanxi1.setText(str);
								gxr1=getGuanXiRenCode(str);
							}
						});
						stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
					}
				});
				guanxi.findViewById(R.id.rl_birth).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(HandleIDActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								zinvBirth1.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
						monPickerDialogcanbao.show();
					}
				});
				guanxi.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (ll_guanxi.getChildCount()>1){
							Toast.makeText(HandleIDActivity.this,"请先按序号由大到小删除！",Toast.LENGTH_SHORT).show();
							return;
						}
						relationView1=null;
						ll_guanxi.removeViewAt(0);
					}
				});
				break;
			case 1://子女2
				relationView2=guanxi;
				memberGuanxi2=(TextView) guanxi.findViewById(R.id.tv_guanxi);
				zinvName2=(EditText) guanxi.findViewById(R.id.edt_name);
				zinvBirth2=(TextView) guanxi.findViewById(R.id.tv_birth);
				zinvSex2=(RadioButton)guanxi.findViewById(R.id.rb_male);
				zinvSfz2=(ValidateEidtText) guanxi.findViewById(R.id.edt_idcard);
				zinvJiezhong2=(RadioButton) guanxi.findViewById(R.id.rb_you);

				memberGuanxi2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						StringWheel stringWheel=new StringWheel(HandleIDActivity.this, Constants.GUANXIREN, new OnEnsureClickListener() {
							@Override
							public void OnEnSureClick(String str) {
								memberGuanxi2.setText(str);
								gxr2=getGuanXiRenCode(str);
							}
						});
						stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
					}
				});
				guanxi.findViewById(R.id.rl_birth).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(HandleIDActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								zinvBirth2.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
						monPickerDialogcanbao.show();
					}
				});
				guanxi.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (ll_guanxi.getChildCount()>2){
							Toast.makeText(HandleIDActivity.this,"请先按序号由大到小删除！",Toast.LENGTH_SHORT).show();
							return;
						}
						relationView2=null;
						ll_guanxi.removeViewAt(1);
					}
				});
				break;
			case 2://子女3
				relationView3=guanxi;
				memberGuanxi3=(TextView) guanxi.findViewById(R.id.tv_guanxi);
				zinvName3=(EditText) guanxi.findViewById(R.id.edt_name);
				zinvBirth3=(TextView) guanxi.findViewById(R.id.tv_birth);
				zinvSex3=(RadioButton)guanxi.findViewById(R.id.rb_male);
				zinvSfz3=(ValidateEidtText) guanxi.findViewById(R.id.edt_idcard);
				zinvJiezhong3=(RadioButton) guanxi.findViewById(R.id.rb_you);

				memberGuanxi3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						StringWheel stringWheel=new StringWheel(HandleIDActivity.this, Constants.GUANXIREN, new OnEnsureClickListener() {
							@Override
							public void OnEnSureClick(String str) {
								memberGuanxi3.setText(str);
								gxr3=getGuanXiRenCode(str);
							}
						});
						stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
					}
				});
				guanxi.findViewById(R.id.rl_birth).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(HandleIDActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								zinvBirth3.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
						monPickerDialogcanbao.show();
					}
				});
				guanxi.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (ll_guanxi.getChildCount()>3){
							Toast.makeText(HandleIDActivity.this,"请先按序号由大到小删除！",Toast.LENGTH_SHORT).show();
							return;
						}
						relationView3=null;
						ll_guanxi.removeViewAt(2);
					}
				});
				break;
			case 3://子女4
				relationView4=guanxi;
				memberGuanxi4=(TextView) guanxi.findViewById(R.id.tv_guanxi);
				zinvName4=(EditText) guanxi.findViewById(R.id.edt_name);
				zinvBirth4=(TextView) guanxi.findViewById(R.id.tv_birth);
				zinvSex4=(RadioButton)guanxi.findViewById(R.id.rb_male);
				zinvSfz4=(ValidateEidtText) guanxi.findViewById(R.id.edt_idcard);
				zinvJiezhong4=(RadioButton) guanxi.findViewById(R.id.rb_you);

				memberGuanxi4.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						StringWheel stringWheel=new StringWheel(HandleIDActivity.this, Constants.GUANXIREN, new OnEnsureClickListener() {
							@Override
							public void OnEnSureClick(String str) {
								memberGuanxi4.setText(str);
								gxr4=getGuanXiRenCode(str);
							}
						});
						stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
					}
				});
				guanxi.findViewById(R.id.rl_birth).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(HandleIDActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								zinvBirth4.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
						monPickerDialogcanbao.show();
					}
				});
				guanxi.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						relationView4=null;
						ll_guanxi.removeViewAt(3);
					}
				});
				break;
		}
		((TextView)guanxi.findViewById(R.id.tv_number)).setText((ll_guanxi.getChildCount()+1)+"");
		if (ll_guanxi.getChildCount()>=4){
			Toast.makeText(HandleIDActivity.this,"最多添加四名成员！",Toast.LENGTH_SHORT).show();
			return;
		}
		ll_guanxi.addView(guanxi);
		sv_b.post(new Runnable() {
			@Override
			public void run() {
				sv_b.smoothScrollTo(0,CommonUtils.dip2px(HandleIDActivity.this,280)+sv_b.getScrollY());
			}
		});

	}
	@Event(value={R.id.rl_jyrq})//就业日期
	private void jyrqnClick(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerjy,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenerjy =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			edt_jyrq.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			jyrq=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
	@Event(value={R.id.rl_huqfrq})//婚育证明签发日期
	private void huqfrqClick(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerhuqfrq,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenerhuqfrq =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			edt_hyqfrq.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			hyqfrq=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
	@Event(value={R.id.rl_jhrq})//结婚日期
	private void jhrqClick(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerjhrq,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenerjhrq =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			edt_jhrq.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			jhrq=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
	@Event(value={R.id.rl_bycsrq})//避孕措施落实日期日期
	private void bycsrqClick(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerbycsrq,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenerbycsrq =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			edt_bycsrq.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			bycsrq=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
	@Event(value={R.id.rl_czqx1})//出租开始日期
	private void czqx1Click(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerczqx1,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenerczqx1 =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			if (!TextUtils.isEmpty(czqx2)) {
				if (!BiggerCurrent(year + ((monthOfYear + 1) > 9 ? ((monthOfYear + 1) + "") : ("0" + (monthOfYear + 1))) + (dayOfMonth > 9 ? (dayOfMonth + "") : ("0" + dayOfMonth)), czqx2)) {
					Toast.makeText(HandleIDActivity.this, "出租开始日期小于出租结束日期！", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			edt_czqx1.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			czqx1=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
	@Event(value={R.id.rl_czqx2})//出租结束日期
	private void czqx2Click(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerczqx2,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenerczqx2 =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			if (!TextUtils.isEmpty(czqx1)) {
				if (BiggerorEqualCurrent(year + ((monthOfYear + 1) > 9 ? ((monthOfYear + 1) + "") : ("0" + (monthOfYear + 1))) + (dayOfMonth > 9 ? (dayOfMonth + "") : ("0" + dayOfMonth)), czqx1)) {
					Toast.makeText(HandleIDActivity.this, "出租结束日期需大于出租开始日期！", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			edt_czqx2.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			czqx2=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
	@Event(value={R.id.rl_lsrq})//来苏日期
	private void lsrqClick(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerlsrq,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenerlsrq =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            if (!BiggerorEqualCurrent(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)),djrq)){
                Toast.makeText(HandleIDActivity.this,"来苏日期必须小于登记日期！",Toast.LENGTH_SHORT).show();
                return;
            }
			tv_lsrq.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			lsrq=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
    @Event(value={R.id.rl_djrq})//登记日期
    private void djrqClick(View view){
        MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerdjrq,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
        monPickerDialogcanbao.show();
    }
    private OnDateSetListener onDateSetListenerdjrq =new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            if (!BiggerorEqualCurrent(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)),currentTime)){
                Toast.makeText(HandleIDActivity.this,"登记日期必须小于等于当前日期！",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(tv_lsrq.getText().toString().trim())){
                if (!BiggerorEqualCurrent(lsrq,year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)))){
                    Toast.makeText(HandleIDActivity.this,"来苏日期必须小于登记日期！",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            tv_djrq.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
            djrq=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
        }
    };
	@Event(value={R.id.rl_zjdq})//租金到期日期
	private void zjdqClick(View view){
		MonPickerDialog monPickerDialogcanbao =new MonPickerDialog(this, onDateSetListenerzjdq,c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		monPickerDialogcanbao.show();
	}
	private OnDateSetListener onDateSetListenerzjdq =new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			tv_zjdq.setText(year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth)));
			zjdq=year+((monthOfYear+1)>9?((monthOfYear+1)+""):("0"+(monthOfYear+1)))+(dayOfMonth>9?(dayOfMonth+""):("0"+dayOfMonth));
		}
	};
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
//					ll_hunhou.setVisibility(View.GONE);
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
					rl_canbaoshijian.setVisibility(View.GONE);
					tv_canbaoshijian.setText("");
					shifoucanbao="否";
					tv_cbqk.setText("没有参保");
					cbqk="00000010";
					break;
				case R.id.rb_shicanbao:
					rl_canbaoshijian.setVisibility(View.VISIBLE);
					shifoucanbao="是";
					tv_cbqk.setText("");
					cbqk="";
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
				case R.id.rg_jieyu://是否节育
					switch (checkedId) {
						case R.id.rb_shijieyu:
							sfjy="1";
							break;
						case R.id.rb_foujieyu:
							sfjy="2";

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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode ==RESULT_OK) {
			switch (requestCode) {
				case CAMERA_REQUEST_CODE:
					if (DeviceUtils.hasSDCard()) {
						File tempFile = new File(Environment.getExternalStorageDirectory()+"/"+ IMAGE_FILE_NAME);
						Crop.of(Uri.fromFile(tempFile), Uri.fromFile(new File(getCacheDir(), "cropped"))).withAspect(4, 5).withMaxSize(160, 200).start(HandleIDActivity.this);
					}else{
						Toast.makeText(HandleIDActivity.this, "未找到存储卡，无法存储照片！",
								Toast.LENGTH_LONG).show();
					}
					break;
				case Crop.REQUEST_CROP:
					if (data != null) {
						getImageToViewFromCrop(data);
					}
					break;
				default:
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void getImageToViewFromCrop(Intent data) {
		Uri mImageCaptureUri = Crop.getOutput(data);
		Bitmap photo = null;
		if (mImageCaptureUri != null) {
			try {
				photo = MediaStore.Images.Media.getBitmap(HandleIDActivity.this.getContentResolver(), mImageCaptureUri);
				Drawable drawable = new BitmapDrawable(photo);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] b = stream.toByteArray();
				pic = new String(Base64Coder.encodeLines(b));
                PicRepacePop picRepacePop=new PicRepacePop(HandleIDActivity.this, drawable, new PicRepacePop.OnEnsureClick() {
                    @Override
                    public void onClick(boolean isEnsure) {
                        if (isEnsure){
                            try {
                                dbUtils.delete(People.class, WhereBuilder.b("cardno", "=", people2.cardno));
                                people2.setIstakephoto("1");
                                people2.setPicture(pic);
                                dbUtils.save(people2);
                                Dialog.showSelectDialog(HandleIDActivity.this, "登记成功!", new DialogClickListener() {
                                    @Override
                                    public void confirm() {
                                        ((MyApplication)getApplication()).setIsSureDengji(true);
                                        HandleIDActivity.this.finish();
                                    }
                                    @Override
                                    public void cancel() {
                                        ((MyApplication)getApplication()).setIsSureDengji(false);
                                        HandleIDActivity.this.finish();

                                    }
                                });

                            } catch (DbException e) {
                                Toast.makeText(HandleIDActivity.this, "数据库操作失败！请发送掉全部数据,并下载全库地址！", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }else{
                            Intent intentFromCapture = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            // 判断存储卡是否可以用，可用进行存储
                            if (DeviceUtils.hasSDCard()) {
                                intentFromCapture.putExtra(
                                        MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(Environment
                                                .getExternalStorageDirectory(),
                                                IMAGE_FILE_NAME)));
                            }
                            startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
                        }
                    }
                });
                picRepacePop.showPopupWindow(findViewById(R.id.root),0,0);
                backgroundAlpha(0.3f);
                picRepacePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	class InitData extends AsyncTask<String ,Integer ,String> {
		@Override
		protected String doInBackground(String... strings) {
			try {
				List<JLX> jlxList=dbUtils.findAll(Selector.from(JLX.class));
				PSCName=new String[jlxList.size()];
				for (int i = 0; i < jlxList.size(); i++) {
					PSCName[i]=jlxList.get(i).jlx_name;
					List<HouseJLX> housejlxList=dbUtils.findAll(Selector.from(HouseJLX.class).where("fk_p","=",jlxList.get(i).id));
					if (housejlxList.size()!=0) {
						USERName=new String[housejlxList.size()];
						UserId=new String[housejlxList.size()];
						for (int j = 0; j <housejlxList.size(); j++) {
							USERName[j]=housejlxList.get(j).hosejlx_name;
							UserId[j]=housejlxList.get(j).id;
						}
						UserNameMap.put(jlxList.get(i).jlx_name, USERName);
						UserIdMap.put(jlxList.get(i).jlx_name, UserId);
					}else{
						USERName=new String[]{""};
						UserId=new String[]{""};
						UserNameMap.put(jlxList.get(i).jlx_name, USERName);
						UserIdMap.put(jlxList.get(i).jlx_name, UserId);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			isHouseAddressInital=true;
			if (progressDialog!=null){
				if (progressDialog.isShowing()){
					progressDialog.dismiss();
					fangwubianhaoClick(null);
				}
				progressDialog.dismiss();
			}
		}
	}
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = HandleIDActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        HandleIDActivity.this.getWindow().setAttributes(lp);
    }
}
