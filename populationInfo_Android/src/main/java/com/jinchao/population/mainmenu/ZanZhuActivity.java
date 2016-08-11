package com.jinchao.population.mainmenu;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.entity.RealHouseBean.RealHouseOne;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.DialogLoading;
import com.jinchao.population.view.NavigationLayout;
@ContentView(R.layout.activity_zanzhuinfo)
public class ZanZhuActivity extends BaseActiviy{
	@ViewInject(R.id.tv_content)
	private TextView tv_content;
	private People people;
	private DialogLoading dialogLoading;
	private RealHouseOne realHouseOne;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("暂住信息验证");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		people=(People) getIntent().getSerializableExtra("people");
		realHouseOne=(RealHouseOne) getIntent().getSerializableExtra("house");
		dialogLoading=new DialogLoading(this, "信息验证中...",true);
	}
	
	private void requestYanZheng(String idcard){
		dialogLoading.show();
		RequestParams params=new RequestParams(Constants.URL+"quePeople.aspx");
		params.addBodyParameter("type", "get_people");
		params.addBodyParameter("idcard", idcard);
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.d("quePeople", result);
				try {
					ResultBeanAndList<YanZhengBean> yanzhengxml = XmlUtils.getBeanByParseXml(result,"",YanZhengBean.class, "xml_people", YanZhengBean.class);
					YanZhengBean yanZhengBean =(YanZhengBean) yanzhengxml.bean;
					dialogLoading.dismiss();
					if (yanZhengBean.result.equals("1")) {
						tv_content.setText(yanZhengBean.toString());
					}else if (yanZhengBean.result.equals("0")) {
						handleID(true);
					}
					Log.d("quePeople", yanzhengxml.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				dialogLoading.dismiss();
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished() {}
		});
	}
	private void handleID(boolean isHandle) {
		Intent intent = new Intent(ZanZhuActivity.this,HandleIDActivity.class);
		intent.putExtra("people", people);
		intent.putExtra("isHandle", isHandle);
		if (realHouseOne!=null) {
			intent.putExtra("house", realHouseOne);
		}
		startActivity(intent);
		finish();
	}
	@Event(value={R.id.ib_yanzheng})
	private void yaNZheng(View view){
		requestYanZheng(people.cardno);
	}
	@Event(value={R.id.ib_banzheng})
	private void handleId(View view){
		handleID(true);
	}
	@Event(value={R.id.ib_biangeng})
	private void changeId(View view){
		handleID(false);
	}
	@Event(value={R.id.ib_tiaozhuan})
	private void passit(View view){
		handleID(false);
	}
}
