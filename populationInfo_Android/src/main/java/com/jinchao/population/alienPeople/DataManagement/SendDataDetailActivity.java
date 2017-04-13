package com.jinchao.population.alienPeople.DataManagement;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.view.NavigationLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by user on 2017/4/13.
 */

@ContentView(R.layout.activity_senddatadetail)
public class SendDataDetailActivity extends BaseActiviy{
    @ViewInject(R.id.tv_shihao)TextView tv_shihao;
    @ViewInject(R.id.tv_name) TextView tv_name;
    @ViewInject(R.id.tv_card) TextView tv_card;
    @ViewInject(R.id.tv_phone) TextView tv_phone;
    @ViewInject(R.id.tv_status) TextView tv_status;
    @ViewInject(R.id.tv_bianhao) TextView tv_bianhao;
    @ViewInject(R.id.tv_address) TextView tv_address;
    @ViewInject(R.id.tv_time) TextView tv_time;
    @ViewInject(R.id.tv_fwcs)TextView tv_fwcs;
    @ViewInject(R.id.tv_dwdz)TextView tv_dwdz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("人员信息详情");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        People people=(People) getIntent().getSerializableExtra("people");
        if (people!=null){
            tv_address.setText(people.getResidentAddress());
            tv_shihao.setText(people.getRoomcode());
            tv_bianhao.setText(people.getHomecode());
            tv_card.setText(people.getCardno());
            tv_time.setText(people.getCollect_datetime());
            tv_fwcs.setText(people.getSeviceAddress());
            tv_status.setText(people.module);
            tv_name.setText(people.getName());
            tv_phone.setText(people.getPhone());
            tv_dwdz.setText(people.getNote());
        }
    }
}
