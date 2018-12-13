package com.jinchao.population.alienPeople.sys;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.WarnMsg;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.XMLParserUtil;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.ValidateEidtText;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Create by FrankFan on 2018/5/17
 * Description:
 */
@ContentView(R.layout.activity_warnmsg)
public class WarnMsgActivity extends BaseReaderActiviy implements IDReader.IDReaderListener{
    @ViewInject(R.id.edt_idcard)ValidateEidtText edt_idcard;
    @ViewInject(R.id.btn_ensure)Button btn_ensure;
    @ViewInject(R.id.tv_content)TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("到期短信提醒查询");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        idReader.setListener(this);
        getPermission();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (link != null)
            return;
        Tag nfcTag = null;
        try {
            nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        } catch (Exception e) {

        }
        if (nfcTag != null) {
            link = LinkFactory.createExNFCLink(nfcTag);
            try {
                link.connect();
            } catch (Exception e) {
                showError(e.getMessage());
                try {
                    link.disconnect();
                } catch (Exception ex) {
                } finally {
                    link = null;
                }
                return;
            }
            idReader.setLink(link);
            showIDCardInfo(true, null,null);
            showProcessDialog("正在读卡中，请稍后");
            idReader.startReadCard();
        }
    }
    private void showIDCardInfo(boolean isClear, PersonInfo user, String msg) {//显示身份证数据到界面
        hideProcessDialog();
        if (isClear){
            return;
        }
        if (user==null&&msg!=null){
            showError(msg);
        }else{
            edt_idcard.setText(user.getIdNumber().trim());

        }
    }
    private void getPermission(){
        tv_content.setText("");
        showProcessDialog("数据提交中...");
        RequestParams params=new RequestParams(Constants.URL+"HousePeople.aspx");
        params.addBodyParameter("type","messageSq");
        params.addBodyParameter("sq_id",MyInfomationManager.getSQID(this));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("resultPeople", result);
                if(!result.equals("1")){
                    Dialog.showForceDialog(WarnMsgActivity.this,"提示","你所在派出所未开通此功能！", new Dialog.DialogClickListener() {
                        @Override
                        public void confirm() {
                            WarnMsgActivity.this.finish();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
                hideProcessDialog();

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {hideProcessDialog();}
        });
    }

    @Event(value={R.id.btn_ensure})
    private void search(View view){
        String idcard=edt_idcard.getText().toString().trim();
        if(!CommonIdcard.validateIdCard18(idcard)){
            Toast.makeText(this,"请输入合法的身份证",Toast.LENGTH_SHORT).show();
            return;
        }
        getMsgReult(idcard);
    }

    private void getMsgReult(String idcard){
        tv_content.setText("");
        showProcessDialog("数据提交中...");
        RequestParams params=new RequestParams(Constants.URL+"HousePeople.aspx");
        params.addBodyParameter("type","resultPeople");
        params.addBodyParameter("idcard",idcard);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("resultPeople", result);
                parseJson(result);
                hideProcessDialog();

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {hideProcessDialog();}
        });
    }

    private  void parseJson(String result){
        try {
            WarnMsg warnMsg= GsonTools.changeGsonToBean(result,WarnMsg.class);
            if(warnMsg.data.size()==0){
                String str ="<font color='#1E90FF'>未查询到此人的短信记录！</font>";
                tv_content.setText(Html.fromHtml(str));
                return;
            }
            if(warnMsg.data.get(0).resultCode.equals("0")){
                String str ="<font color='#1E90FF'>已成功</font>于<font color='#1E90FF'>"+warnMsg.data.get(0).write_time+"</font>发送短信：<br><font color='red'>【胥口派出所】友情提醒：您的居住证即将到期，请在20天内前往社区警务站年审签注，如未在规定期限内进行签注将影响您的积分累计！</font><br>至<font color='#1E90FF'>"+warnMsg.data.get(0).sname+"</font>的手机：<font color='#1E90FF'>"+warnMsg.data.get(0).tel+"</font>";
                tv_content.setText(Html.fromHtml(str));

            }else{
                String str ="<font color='#1E90FF'>短信发送失败；</font><br><font color='red'>失败原因:"+warnMsg.data.get(0).resultMsg+"！</font><br>失败对象:<font color='#1E90FF'>"+warnMsg.data.get(0).sname+"</font>的手机：<font color='#1E90FF'>"+warnMsg.data.get(0).tel+"</font>";
                tv_content.setText(Html.fromHtml(str));
            }
            hidenSoftKeyBoard(edt_idcard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onReadCardSuccess(final PersonInfo personInfo) {
        try {
            link.disconnect();
        } catch (Exception e) {
            Log.e("readcard", e.getMessage());
        } finally {
            link = null;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProcessDialog();
                showIDCardInfo(false,personInfo,null);
            }
        });
    }

    @Override
    public void onReadCardFailed(final String s) {
        try {
            link.disconnect();
        } catch (Exception e) {
            Log.e("readcard", e.getMessage());
        } finally {
            link = null;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProcessDialog();
                showError(s);
            }
        });

    }
}
