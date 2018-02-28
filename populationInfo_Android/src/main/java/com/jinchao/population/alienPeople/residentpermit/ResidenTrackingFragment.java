package com.jinchao.population.alienPeople.residentpermit;

import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.Link;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.SQBean;
import com.jinchao.population.entity.TrackingBean;
import com.jinchao.population.utils.CommonIdcard;
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
@ContentView(R.layout.fragment_track)
public class ResidenTrackingFragment extends BaseFragment{
    public IDReader idReader;
    public Link link = null;
    @ViewInject(R.id.edt_idcard)ValidateEidtText edt_idcard;
    @ViewInject(R.id.root)LinearLayout root;
    @ViewInject(R.id.tv_shenpi)TextView tv_shenpi;
    @ViewInject(R.id.tv_zhizheng)TextView tv_zhizheng;
    @ViewInject(R.id.tv_xiafa)TextView tv_xiafa;
    @ViewInject(R.id.tv_lingzheng)TextView tv_lingzheng;
    @ViewInject(R.id.tv_kastatus)TextView tv_kastatus;
    @ViewInject(R.id.tv_caozuo)TextView tv_caozuo;
    @ViewInject(R.id.ll_result)LinearLayout ll_result;
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
            Toast.makeText(getActivity(),"请先输入身份证号！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CommonIdcard.validateCard(idcard)){
            Toast.makeText(getActivity(),"请先输入合法的身份证号！",Toast.LENGTH_SHORT).show();
            return;
        }
        getData(idcard);

    }

    private void showError(String error){
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }
    private void showcard(String card){
        ll_result.setVisibility(View.GONE);
        edt_idcard.setText(card);
        edt_idcard.setSelection(card.length());
    }

    private void getData(String idcard){
        showProcessDialog("数据提交中...");
        ll_result.setVisibility(View.GONE);
        RequestParams params=new RequestParams(Constants.URL+"GdPeople.aspx?type=get_jzz&sfz="+idcard);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("get_jzz", result);
                hideProcessDialog();

                XMLParserUtil.parseXMLtoTRACK(result, new XMLParserUtil.OnXmlParserToTrackListener() {
                    @Override
                    public void success(TrackingBean result) {
                        ll_result.setVisibility(View.VISIBLE);
                        tv_shenpi.setText("●  是否审批：");
                        tv_zhizheng.setText("●  是否制证：");
                        tv_xiafa.setText("●  是否下发：");
                        tv_lingzheng.setText("●  是否领证：");
                        tv_shenpi.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tv_shenpi.setTextColor(getResources().getColor(R.color.title3));
                        tv_zhizheng.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tv_zhizheng.setTextColor(getResources().getColor(R.color.title3));
                        tv_xiafa.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tv_xiafa.setTextColor(getResources().getColor(R.color.title3));
                        tv_lingzheng.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tv_lingzheng.setTextColor(getResources().getColor(R.color.title3));
                        tv_caozuo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tv_caozuo.setTextColor(getResources().getColor(R.color.title3));
                        if(result.lingzheng.equals("1")) {
                            tv_kastatus.setVisibility(View.VISIBLE);
                            if (result.kastatus.equals("1")) {
                                tv_kastatus.setText("居住证卡状态：激活");
                            } else {
                                tv_kastatus.setText("居住证卡状态：休眠");
                            }
                        }else{
                            if(result.caozuo.equals("1")) {
                                tv_kastatus.setVisibility(View.INVISIBLE);
                            }else{
                                tv_kastatus.setVisibility(View.VISIBLE);
                                if (result.kastatus.equals("1")) {
                                    tv_kastatus.setText("居住证卡状态：激活");
                                } else {
                                    tv_kastatus.setText("居住证卡状态：休眠");
                                }
                            }
                        }
                        tv_caozuo.setText(result.caozuo.equals("2")?"●  操作类型：变更":(result.caozuo.equals("3")?"●  操作类型：换证":(result.caozuo.equals("4")?"●  操作类型：注销":(result.caozuo.equals("5")?"●  操作类型：挂失":(result.caozuo.equals("6")?"●  操作类型：解挂":(result.caozuo.equals("7")?"●  操作类型：补卡":(result.caozuo.equals("8")?"●  操作类型：签注延期":(result.caozuo.equals("9")?"●  操作类型：证件临转":(result.caozuo.equals("10")?"●  操作类型：休眠激活":"●  操作类型：初次办证")))))))));
                        if(result.shenpi.equals("1")){
                            tv_shenpi.setText("●  是否审批：审批通过");
                            if(result.zhizheng.equals("1")){
                                tv_zhizheng.setText("●  是否制证：已制证");
                                if(result.xiafa.equals("1")){
                                    tv_xiafa.setText("●  是否下发：已下发");
                                    if(result.lingzheng.equals("1")){
                                        if(result.caozuoshijian.compareTo(result.lingzhengriqi)<0){
                                            tv_lingzheng.setText("●  是否领证：已领证");
                                            tv_lingzheng.setTextColor(getResources().getColor(R.color.title1));
                                            tv_lingzheng.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                        }else{
                                            tv_lingzheng.setText("●  是否领证：已领证");
                                            tv_lingzheng.setTextColor(getResources().getColor(R.color.title1));
                                            tv_lingzheng.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                                            tv_caozuo.setTextColor(getResources().getColor(R.color.title1));
//                                            tv_caozuo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                        }
                                    }else{
                                        tv_lingzheng.setText("●  是否领证：未领证");
                                        tv_lingzheng.setTextColor(getResources().getColor(R.color.title1));
                                        tv_lingzheng.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    }
                                }else{
                                    tv_xiafa.setText("●  是否下发：未下发");
                                    tv_xiafa.setTextColor(getResources().getColor(R.color.title1));
                                    tv_xiafa.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                }
                            }else{
                                tv_zhizheng.setText(result.zhizheng.equals("0")?"●  是否制证：未制证":(result.zhizheng.equals("2")?"●  是否制证：进入制证流程，正在制证中":"●  是否制证：制证失败"));
                                tv_zhizheng.setTextColor(getResources().getColor(R.color.title1));
                                tv_zhizheng.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                            }
                        }else{
                            tv_shenpi.setText(result.shenpi.equals("0")?"●  是否审批：未审批":"●  是否审批：审批未通过");
                            tv_shenpi.setTextColor(getResources().getColor(R.color.title1));
                            tv_shenpi.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        }
                    }

                    @Override
                    public void fail(String error) {
                        Dialog.showForceDialog(getActivity(),"提示",error, new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {
                            }
                            @Override
                            public void cancel() {
                            }
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

}
