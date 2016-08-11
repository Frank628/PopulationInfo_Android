package com.jinchao.population.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.mainmenu.RegistRentalHouseActivity;
import com.jinchao.population.mainmenu.RegisterActivity;
import com.jinchao.population.mainmenu.ReshootActivity;
import com.jinchao.population.mainmenu.SearchPeopleActivity;
import com.jinchao.population.mainmenu.SearchRentalHouseActivity;
import com.jinchao.population.mainmenu.SysActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * Created by OfferJiShu01 on 2016/8/8.
 */
@ContentView(R.layout.fragment_alienpopulation)
public class AlienFragment extends BaseFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Event(value={R.id.iv_register})
    private void onRegistClick(View view){
        Intent intent =new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }
    @Event(value={R.id.iv_rentalhouseregist})
    private void rentalhouseregistClick(View view){
        Intent intent =new Intent(getActivity(), RegistRentalHouseActivity.class);
        startActivity(intent);
    }
    @Event(value={R.id.iv_personinfocheck})
    private void personinfocheckClick(View view){
        Intent intent =new Intent(getActivity(), SearchPeopleActivity.class);
        startActivity(intent);
    }
    @Event(value={R.id.iv_rentalhousecheck})
    private void rentalhousecheckClick(View view){
        Intent intent =new Intent(getActivity(), SearchRentalHouseActivity.class);
        startActivity(intent);
    }
    @Event(value={R.id.iv_photo})
    private void reshootClick(View view){
        Intent intent =new Intent(getActivity(), ReshootActivity.class);
        startActivity(intent);
    }
    @Event(value={R.id.iv_sys})
    private void sysClick(View view){
        Intent intent =new Intent(getActivity(), SysActivity.class);
        startActivity(intent);
    }
}
