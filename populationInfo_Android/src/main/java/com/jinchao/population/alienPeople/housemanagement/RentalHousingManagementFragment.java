package com.jinchao.population.alienPeople.housemanagement;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyApplication;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
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
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by OfferJiShu01 on 2017/2/28.
 */
@ContentView(R.layout.fragment_rentalhousing_inquiries)
public class RentalHousingManagementFragment extends BaseFragment{

    @ViewInject(R.id.tv_content)TextView tv_content;
    @ViewInject(R.id.btn_search)Button btn_search;
    @ViewInject(R.id.edt_content)ValidateEidtText edt_content;
    @ViewInject(R.id.tv_noresult)TextView tv_noresult;
    private DbUtils dbUtils;
    public int database_tableNo=0;//当前登录账号使用的哪个数据库，0:未下载的地址库，1：表1,2：表2.。。。
    @Override
    public void onStart() {
        super.onStart();
        if (((MyApplication)getActivity().getApplication()).database_tableNo==0){
            database_tableNo= DatabaseUtil.getNullDB(getActivity());
        }else{
            database_tableNo=((MyApplication)getActivity().getApplication()).database_tableNo;
        }
    }
    @Event(R.id.btn_seach)
    private void search(View view){
        String code =edt_content.getText().toString().trim();
        if (TextUtils.isEmpty(code)){
            Toast.makeText(getActivity(),"请输入6位的房屋编号",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            dbUtils= DeviceUtils.getDbUtils(getActivity());
            switch (database_tableNo){
                case 1:
                    HouseAddressOldBean houseAddressOldBean = dbUtils.findFirst(Selector.from(HouseAddressOldBean.class).where("scode", "=", code));
                    if (houseAddressOldBean!=null) {
                        tv_content.setText(houseAddressOldBean.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 2:
                    HouseAddressOldBean2 houseAddressOldBean2 = dbUtils.findFirst(Selector.from(HouseAddressOldBean2.class).where("scode", "=", code));
                    if (houseAddressOldBean2!=null) {
                        tv_content.setText(houseAddressOldBean2.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 3:
                    HouseAddressOldBean3 houseAddressOldBean3 = dbUtils.findFirst(Selector.from(HouseAddressOldBean3.class).where("scode", "=", code));
                    if (houseAddressOldBean3!=null) {
                        tv_content.setText(houseAddressOldBean3.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 4:
                    HouseAddressOldBean4 houseAddressOldBean4 = dbUtils.findFirst(Selector.from(HouseAddressOldBean4.class).where("scode", "=", code));
                    if (houseAddressOldBean4!=null) {
                        tv_content.setText(houseAddressOldBean4.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 5:
                    HouseAddressOldBean5 houseAddressOldBean5 = dbUtils.findFirst(Selector.from(HouseAddressOldBean5.class).where("scode", "=", code));
                    if (houseAddressOldBean5!=null) {
                        tv_content.setText(houseAddressOldBean5.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 6:
                    HouseAddressOldBean6 houseAddressOldBean6 = dbUtils.findFirst(Selector.from(HouseAddressOldBean6.class).where("scode", "=", code));
                    if (houseAddressOldBean6!=null) {
                        tv_content.setText(houseAddressOldBean6.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 7:
                    HouseAddressOldBean7 houseAddressOldBean7 = dbUtils.findFirst(Selector.from(HouseAddressOldBean7.class).where("scode", "=", code));
                    if (houseAddressOldBean7!=null) {
                        tv_content.setText(houseAddressOldBean7.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 8:
                    HouseAddressOldBean8 houseAddressOldBean8 = dbUtils.findFirst(Selector.from(HouseAddressOldBean8.class).where("scode", "=", code));
                    if (houseAddressOldBean8!=null) {
                        tv_content.setText(houseAddressOldBean8.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 9:
                    HouseAddressOldBean9 houseAddressOldBean9 = dbUtils.findFirst(Selector.from(HouseAddressOldBean9.class).where("scode", "=", code));
                    if (houseAddressOldBean9!=null) {
                        tv_content.setText(houseAddressOldBean9.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
                case 10:
                    HouseAddressOldBean10 houseAddressOldBean10 = dbUtils.findFirst(Selector.from(HouseAddressOldBean10.class).where("scode", "=", code));
                    if (houseAddressOldBean10!=null) {
                        tv_content.setText(houseAddressOldBean10.toString());
//
                    }else{
                        tv_content.setText("查无此房屋");
                    }
                    break;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
