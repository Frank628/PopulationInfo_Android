package com.jinchao.population.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.ActionSheetDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by OfferJiShu01 on 2016/8/12.
 */
@ContentView(R.layout.activity_aboutus)
public class AboutActivity extends BaseActiviy {
    @ViewInject(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("关于我们");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_version.setText(getVersion());
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "版本号：v" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    @Event(value = R.id.rl_tel)
    private void telClick(View view) {
        ActionSheetDialog dialog = new ActionSheetDialog(this);
        dialog.builder();
        dialog.setTitle("拨打联系电话");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.addSheetItem("0512-65288818", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "051265288818");
                intent.setData(data);
                AboutActivity.this.startActivity(intent);
            }
        });

        dialog.show();
    }
    @Event(value = R.id.rl_baoxiu)
    private void baoxiuClick(View view) {
        ActionSheetDialog dialog = new ActionSheetDialog(this);
        dialog.builder();
        dialog.setTitle("拨打服务热线");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.addSheetItem("17706219286", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "17706219286");
                intent.setData(data);
                AboutActivity.this.startActivity(intent);
            }
        });
        dialog.addSheetItem("18913509193", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "18913509193");
                intent.setData(data);
                AboutActivity.this.startActivity(intent);
            }
        });
        dialog.show();
    }


}
