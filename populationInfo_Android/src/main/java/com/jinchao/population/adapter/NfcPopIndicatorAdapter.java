package com.jinchao.population.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinchao.population.R;
import com.jinchao.population.alienPeople.nfcpop.AllPeopleFragment;
import com.jinchao.population.alienPeople.nfcpop.HistoryPeopleFragment;
import com.jinchao.population.alienPeople.nfcpop.PeopleRoomFragment;
import com.jinchao.population.entity.NFCJsonBean;
import com.jinchao.population.entity.RoomBean;
import com.jinchao.population.utils.CommonUtils;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

/**
 * Created by user on 2017/3/21.
 */

public class NfcPopIndicatorAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private FragmentManager fragmentManager;
    NFCJsonBean nfcJsonBean;
    List<String> listRoom;
    RoomBean.BianhaoOne bhone;
    int tag;
    LayoutInflater inflate;

    public NfcPopIndicatorAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    public void initAdpater(Context context, NFCJsonBean nfcJsonBean, List<String> listRoom,  RoomBean.BianhaoOne bhone,  int tag){
        this.nfcJsonBean=nfcJsonBean;
        this.listRoom=listRoom;
        this.bhone=bhone;
        this.tag=tag;
        inflate = LayoutInflater.from(context.getApplicationContext());
    }


    @Override
    public int getCount() {
        return listRoom.size();
    }


    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.tab_top, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(listRoom.get(position));
        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        if (position==1){
            return AllPeopleFragment.newInstance(nfcJsonBean,bhone,tag);
        }else if(position==0){
            return HistoryPeopleFragment.newInstance(nfcJsonBean);
        }else {
            return PeopleRoomFragment.newInstance(nfcJsonBean,listRoom.get(position));
        }
    }



    @Override
    public int getItemPosition(Object object) {
        //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
        // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
        return PagerAdapter.POSITION_NONE;
    }
}
