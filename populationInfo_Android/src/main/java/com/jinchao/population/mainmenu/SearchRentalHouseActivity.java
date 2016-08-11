package com.jinchao.population.mainmenu;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.entity.HouseAddressOldBean;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.view.NavigationLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
@ContentView(R.layout.activity_searchrentalhouse)
public class SearchRentalHouseActivity extends BaseActiviy{
	@ViewInject(R.id.edt_content)EditText edt_content;
	@ViewInject(R.id.tv_content)TextView tv_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("出租屋查询");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	@Event(value={R.id.btn_seach})
	private void searchClick(View view){
		String code=edt_content.getText().toString().trim();
		if (edt_content.equals("")) {
			Toast.makeText(this, "请输入房屋编号~", 0).show();
			return;
		}
		DbUtils dbUtils=DeviceUtils.getDbUtils(this);
		HouseAddressOldBean houseAddressOldBean;
		try {
			houseAddressOldBean = dbUtils.findFirst(Selector.from(HouseAddressOldBean.class).where("scode", "=", code));
			if (houseAddressOldBean!=null) {
				tv_content.setText(houseAddressOldBean.toString());
			}else{
				tv_content.setText("查无此房屋");
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		
	}
}
