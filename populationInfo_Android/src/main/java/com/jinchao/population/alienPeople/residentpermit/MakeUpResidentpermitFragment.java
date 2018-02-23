package com.jinchao.population.alienPeople.residentpermit;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.Link;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.SQBean;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.GlobalPref;
import com.jinchao.population.utils.XMLParserUtil;
import com.jinchao.population.utils.XmlUtils;
import com.jinchao.population.view.Dialog;
import com.jinchao.population.view.PopBianzheng;
import com.jinchao.population.widget.ValidateEidtText;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2017/2/28.
 */
@ContentView(R.layout.fragment_re_makeup)
public class MakeUpResidentpermitFragment extends BaseFragment{
    public IDReader idReader;
    public Link link = null;
    @ViewInject(R.id.edt_idcard)ValidateEidtText edt_idcard;
    @ViewInject(R.id.root)LinearLayout root;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        idReader = new IDReader(getActivity());
        idReader.setUseSpecificServer(!GlobalPref.getUseAuto());
        idReader.addSpecificServer(GlobalPref.getAddress(), GlobalPref.getPort());
        idReader.setListener(new IDReader.IDReaderListener() {
            @Override
            public void onReadCardSuccess(final PersonInfo personInfo) {
                try {
                    link.disconnect();
                } catch (Exception e) {
                    Log.e("readcard", e.getMessage());
                } finally {
                    link = null;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                        showcard(personInfo.getIdNumber());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                        showError(s);
                    }
                });
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
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
            showcard("");
            showProcessDialog("正在读卡中，请稍后");
            idReader.startReadCard();
        }
    }
    @Event(value = R.id.btn_remakeup)
    private void reportloss(View view){
        hidenSoftKeyBoard(edt_idcard);
        final String idcard=edt_idcard.getText().toString().trim();
        if (TextUtils.isEmpty(idcard)){
            Toast.makeText(getActivity(),"请先输入需补办的身份证号！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CommonIdcard.validateCard(idcard)){
            Toast.makeText(getActivity(),"请先输入合法的身份证号！",Toast.LENGTH_SHORT).show();
            return;
        }
        requestYanZheng(idcard);

    }

    private void showError(String error){
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }
    private void showcard(String card){
        edt_idcard.setText(card);
        edt_idcard.setSelection(card.length());
    }

    private void makeupResidentPermit(String idcard,String sblb,String njsj){
        showProcessDialog("数据提交中...");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx");
        params.addBodyParameter("type","jzzgh");
        params.addBodyParameter("idcard",idcard);
        params.addBodyParameter("sqdm", MyInfomationManager.getSQCODE(getActivity()));
        params.addBodyParameter("sblb",sblb);
        params.addBodyParameter("njsj",njsj);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("jzzgs", result);
                hideProcessDialog();
                XMLParserUtil.parseXMLforReportLoss(result, new XMLParserUtil.OnXmlParserListener() {
                    @Override
                    public void success(String result) {
                        Toast.makeText(getActivity(),"补证成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail(String error) {
                        Dialog.showForceDialog(getActivity(), "提示", error, new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {}
                            @Override
                            public void cancel() {}
                        });
                    }
                });
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
    private void requestYanZheng(final String idcard){
        showProcessDialog("正在查询该人员所在社区...");
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=get_people&idcard="+idcard);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hideProcessDialog();
                SQBean code= XmlUtils.parseXMLshequCode(result);
                if (!TextUtils.isEmpty(code.getSqcode())){
                    if (code.getSqcode().equals(MyInfomationManager.getSQCODE(getActivity()))){
                        PopBianzheng popBianzheng=new PopBianzheng(getActivity(), new PopBianzheng.OnbEnsureClickListener() {
                            @Override
                            public void onEnsureClick(String juzhu, String lingqu, String shenq,String shijiancode,String leibiecode) {
                                makeupResidentPermit(idcard,leibiecode,shijiancode);
                            }
                        });
                        popBianzheng.showAtLocation(root, Gravity.CENTER, 0, 0);
                    }else{
                        Dialog.showForceDialog(getActivity(),"提示","请到所在社区‘"+code.getSqname()+"’进行挂失！", new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {
                            }
                            @Override
                            public void cancel() {
                            }
                        });
                    }
                }else{
                    Toast.makeText(getActivity(),"人员所在社区查询失败！",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hideProcessDialog();
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {
                hideProcessDialog();
            }
        });
    }
}
