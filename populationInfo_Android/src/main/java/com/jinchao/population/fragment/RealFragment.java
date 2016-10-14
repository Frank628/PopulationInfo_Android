package com.jinchao.population.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.RealHouseBean;
import com.jinchao.population.mainmenu.RegistRentalHouseActivity;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.mainmenu.SearchPeopleActivity;
import com.jinchao.population.mainmenu.SearchRentalHouseActivity;
import com.jinchao.population.realpopulation.RealPeopleSearchActivity;
import com.jinchao.population.utils.GsonTools;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by OfferJiShu01 on 2016/8/9.
 */
@ContentView(R.layout.fragment_realpopulation)
public class RealFragment extends BaseFragment{
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Event(value={R.id.iv_rentalhouseregist})
    private void rentalhouseregistClick(View view){
        Intent intent =new Intent(getActivity(), RegistRentalHouseActivity.class);
        intent.putExtra(Constants.IS_FROM_REALPOPULATION,true);
        startActivity(intent);
    }
    @Event(value={R.id.iv_personinfocheck})
    private void personinfocheckClick(View view){
        Intent intent =new Intent(getActivity(), RealPeopleSearchActivity.class);
        startActivity(intent);
    }
    @Event(value={R.id.iv_rentalhousecheck})
    private void rentalhousecheckClick(View view){
        Intent intent =new Intent(getActivity(), SearchRentalHouseActivity.class);
        intent.putExtra(Constants.IS_FROM_REALPOPULATION,true);
        startActivity(intent);
    }
}
