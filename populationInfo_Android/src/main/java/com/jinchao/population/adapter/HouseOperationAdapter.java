package com.jinchao.population.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/2/27.
 */

public class HouseOperationAdapter extends FragmentPagerAdapter {
    private String [] titles;
    private List<Fragment> fragments;
    public HouseOperationAdapter(FragmentManager fm) {
        super(fm);
    }
    public void setTitleList(String[] titles){
        this.titles=titles;
    }
    public void setFragmentList(List<Fragment> fragments){
        this.fragments=fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        if (fragments!=null)
            return  fragments.size();
        else
         return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles[position];
        }
        return super.getPageTitle(position);
    }
}
