package com.jinchao.population.alienPeople;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.jinchao.population.R;
import com.jinchao.population.adapter.HouseOperationAdapter;
import com.jinchao.population.alienPeople.housemanagement.NfcTagWriterFragment;
import com.jinchao.population.alienPeople.housemanagement.RentalHousingDataEditorFragment;
import com.jinchao.population.alienPeople.housemanagement.RentalHousingDeleteFragment;
import com.jinchao.population.alienPeople.housemanagement.RentalHousingManagementFragment;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.fragment.SettingFragment;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.viewpagerindicator.TabPageIndicator;
import com.jinchao.population.widget.viewpagerindicator.UnderlinePageIndicatorEx;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/2/27.
 */
@ContentView(R.layout.activity_houseoperation)
public class HouseOperationActivity extends BaseActiviy {
    @ViewInject(R.id.message_viewpager) private ViewPager mPager;
    @ViewInject(R.id.tab_indicator) private TabPageIndicator mTabPageIndicator;
    @ViewInject(R.id.underline_indicator) private UnderlinePageIndicatorEx mUnderlinePageIndicator;
    String[] titles=new String[]{"出租屋\n查  询","出租屋\n资料编辑","出租屋\n删  除","NFC标签\n制  作"};
    NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    public IntentFilter[] nfcIfs;
    public String[][] techLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("出租屋管理");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        HouseOperationAdapter adapter=new HouseOperationAdapter(getSupportFragmentManager());
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new RentalHousingManagementFragment());
        fragmentList.add(new RentalHousingDataEditorFragment());
        fragmentList.add(new RentalHousingDeleteFragment());
        fragmentList.add(new NfcTagWriterFragment());
        adapter.setTitleList(titles);
        adapter.setFragmentList(fragmentList);
        mPager.setOffscreenPageLimit(0);
        mPager.setAdapter(adapter);
        mTabPageIndicator.setViewPager(mPager);
        mUnderlinePageIndicator.setViewPager(mPager);
        mUnderlinePageIndicator.setFades(false);
        mTabPageIndicator.setOnPageChangeListener(mUnderlinePageIndicator);
        nfcAdapter=NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "该设备不支持NFC", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nfcAdapter.isEnabled() == false) {
            Toast.makeText(this, "NFC没有打开", Toast.LENGTH_SHORT).show();
            return;
        }
//        mPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
//        nfcIfs = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };
//        techLists = new String[][] { new String[] { NfcB.class.getName() }, new String[] { IsoDep.class.getName() } };
//        nfcAdapter.enableForegroundDispatch(getActivity(), mPendingIntent, nfcIfs, techLists);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter!=null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        List<Fragment> fragmentList=getSupportFragmentManager().getFragments();
        for (int i=0;i<fragmentList.size();i++){
            if (fragmentList.get(i).isVisible()) {
                ((BaseFragment) fragmentList.get(i)).onNewIntent(intent);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter!=null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
}
