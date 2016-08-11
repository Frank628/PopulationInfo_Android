package com.jinchao.population.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.config.Constants;
import com.jinchao.population.utils.SharePrefUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by OfferJiShu01 on 2016/8/9.
 */
@ContentView(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment{
    private IndexRadioOnClick indexRadioOnClick;
    @ViewInject(R.id.tv_index)
    TextView tv_index;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }
    private void initData(){
        String default_index=SharePrefUtil.getString(getActivity(),Constants.DEFAULT_INDEX,"外来人口信息采集");
        tv_index.setText(default_index);
        switch (default_index){
            case "外来人口信息采集" :
                indexRadioOnClick=new IndexRadioOnClick(0);
                break;
            case "实有人口调查" :
                indexRadioOnClick=new IndexRadioOnClick(1);
                break;
            case "巡逻盘查" :
                indexRadioOnClick=new IndexRadioOnClick(2);
                break;
            case "电动车检查" :
                indexRadioOnClick=new IndexRadioOnClick(3);
                break;
            case "房屋位置信息录入" :
                indexRadioOnClick=new IndexRadioOnClick(4);
                break;
            default:
                break;
        }
    }
    @Event(value = R.id.rl_defaultindex)
    private void defaultindexClick(View view){
        AlertDialog ad =new AlertDialog.Builder(getActivity()).setTitle("选择默认主页").setSingleChoiceItems(Constants.DEFAULT_INDEX_AREA,indexRadioOnClick.getIndex(),indexRadioOnClick).create();
        ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                initData();
            }
        });
        ad.show();
    }
    class IndexRadioOnClick implements DialogInterface.OnClickListener {
        private int index;
        public IndexRadioOnClick(int index) {
            this.index = index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
        public int getIndex() {
            return index;
        }
        public void onClick(DialogInterface dialog, int whichButton) {
            setIndex(whichButton);
            SharePrefUtil.saveString(getActivity(), Constants.DEFAULT_INDEX,Constants.DEFAULT_INDEX_AREA[index]);
            dialog.dismiss();
        }
    }
}
