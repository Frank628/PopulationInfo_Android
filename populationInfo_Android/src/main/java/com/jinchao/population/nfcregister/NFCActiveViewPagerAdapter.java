package com.jinchao.population.nfcregister;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.caihua.cloud.common.entity.NFCRegister.ErrorStatus;
import com.caihua.cloud.common.util.NFCHttpUtil;
import com.caihua.cloud.common.util.NFCHttpUtil.RegisterCallback;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by gaofeng on 2016-07-26.
 */
public class NFCActiveViewPagerAdapter extends FragmentStatePagerAdapter implements UnActiveFrag.UnActiveFragListener, ActiveFrag.ActiveFragListener {
    public static final int FORMAL_EXPIRE_FRAG = 0;
    public static final int FORMAL_USER_FRAG = 1;
    public static final int UNACTIVE_FRAG = 2;
    public static final int ACTIVE_FRAG = 3;
    public static final int WAIT_FRAG=4;
    FormalExpireFrag formalExpireFrag;
    FormalUserFrag formalUserFrag;
    UnActiveFrag unActiveFrag;
    ActiveFrag activeFrag;
    WaitFragment waitFragment;
    HashMap<Integer, Fragment> fragMap;
    ViewPager viewPager;
    Context context;
    Handler handler;
    public NFCActiveViewPagerAdapter(Context context, FragmentManager fm, ViewPager viewPager) {
        super(fm);
        fragMap = new HashMap<>();
        this.viewPager = viewPager;
        this.context = context;
        handler=new Handler(context.getMainLooper());
        formalExpireFrag = FormalExpireFrag.newInstance();
        formalUserFrag = FormalUserFrag.newInstance();
        unActiveFrag = UnActiveFrag.newInstance();
        activeFrag = ActiveFrag.newInstance();
        waitFragment=WaitFragment.newInstance();
        fragMap.put(FORMAL_EXPIRE_FRAG, formalExpireFrag);
        fragMap.put(FORMAL_USER_FRAG, formalUserFrag);
        fragMap.put(UNACTIVE_FRAG, unActiveFrag);
        fragMap.put(ACTIVE_FRAG, activeFrag);
        fragMap.put(WAIT_FRAG,waitFragment);
        unActiveFrag.setListener(this);
        activeFrag.setListener(this);
    }

    @Override
    public int getCount() {
        return fragMap.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragMap.get(position);
    }

    @Override
    public void onActiveButtonClicked(final String activeCode) {
        viewPager.setCurrentItem(WAIT_FRAG,false);
        NFCHttpUtil.registerFormal(activeCode,new RegisterCallback() {
			
			@Override
			public void onNetworkError(IOException e) {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "激活失败，请确认网络连接是否正常", Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(UNACTIVE_FRAG,false);
                    }
                });
			}
			
			@Override
			public void onActiveSuccess(String activeCode, long startTime, long endTime) {
				 final Bundle args = new Bundle();
                 final ActiveInfo activeInfo=new ActiveInfo();
                 activeInfo.setActCode(activeCode);
                 activeInfo.setStartTime(startTime);
                 activeInfo.setEndTime(endTime);
                 args.putParcelable(ActiveInfo.class.getName(), activeInfo);
                 handler.post(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						 formalUserFrag.setArguments(args);
		                 viewPager.setCurrentItem(FORMAL_USER_FRAG, false);
					}
                 });
			}
			
			@Override
			public void onActiveError(ErrorStatus status, String errorMsg) {
				final ErrorStatus currentStatus=status;
				final String currentErrorMsg=errorMsg;
				handler.post(new Runnable(){

					@Override
					public void run() {
						Toast.makeText(context, currentErrorMsg, Toast.LENGTH_LONG).show();
						switch(currentStatus){
						case PERMISSION_EXPIRE:
							 viewPager.setCurrentItem(ACTIVE_FRAG,false);
							break;
						case REQUEST_ERR:
							 viewPager.setCurrentItem(UNACTIVE_FRAG,false);
							break;
						case RESPONSE_BODY_ERR:
	                         viewPager.setCurrentItem(UNACTIVE_FRAG,false);
							break;
						}
					}
					
				});
			}
		});
    }

    @Override
    public void onActiveFormalButtonClicked() {
        viewPager.setCurrentItem(ACTIVE_FRAG, false);
    }
}
