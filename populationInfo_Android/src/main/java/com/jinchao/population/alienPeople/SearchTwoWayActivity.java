package com.jinchao.population.alienPeople;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.caihua.cloud.common.entity.PersonInfo;
import com.caihua.cloud.common.link.LinkFactory;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.population.MyApplication;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseReaderActiviy;
import com.jinchao.population.dbentity.People;
import com.jinchao.population.utils.Base64Coder;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.widget.ValidateEidtText;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.io.ByteArrayOutputStream;

/**
 * Created by OfferJiShu01 on 2017/1/19.
 */
@ContentView(R.layout.activity_search_idcardhousecode)
public class SearchTwoWayActivity extends BaseReaderActiviy implements IDReader.IDReaderListener{
    @ViewInject(R.id.edt_idcard) ValidateEidtText edt_idcard;
    @ViewInject(R.id.edt_housecode) ValidateEidtText edt_housecode;
    People people=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idReader.setListener(this);
        NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
        navigationLayout.setCenterText("登记/变更/注销查询");
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
            ((MyApplication)getApplication()).setIsSureDengji(false);
            idReader.setLink(link);
            showIDCardInfo(true, null,null);
            showProcessDialog("正在读卡中，请稍后");
            idReader.startReadCard();
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
                showIDCardInfo(false, personInfo,null);
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
    private void showIDCardInfo(boolean isClear,PersonInfo user,String msg) {//显示身份证数据到界面
        hideProcessDialog();
        if (isClear){
            return;
        }
        if (user==null&&msg!=null){
            showError(msg);
        }else{
            edt_idcard.setText(user.getIdNumber());
            Bitmap bmp =  BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byte[] b = stream.toByteArray();
            String pic = new String(Base64Coder.encodeLines(b));
            people=new People();
            people.setCardno(user.getIdNumber());
            people.setSex(user.getSex());
            people.setAddress(user.getAddress());
            people.setName(user.getName());
            people.setBirthday(user.getBirthday());
            people.setPeople(user.getNation());
            people.setPicture(pic);
        }
        isReading = false;
    }

    @Event(value={R.id.btn_idcard_search})
    private void onidcardsearchClick(View view){
        try {
            int isSecondCard=1;//0默认值，1，一代证；2，二代证；
            String idcardNO = edt_idcard.getText().toString().trim();
            if (CommonIdcard.validateCard(idcardNO)) {
                if (idcardNO.length() == 15) {
                    idcardNO = CommonIdcard.conver15CardTo18(idcardNO);
                    edt_idcard.setText(idcardNO);
                    Toast.makeText(SearchTwoWayActivity.this, "15位转18位证件号成功",Toast.LENGTH_SHORT).show();
                } else if (idcardNO.length() == 17) {
                    idcardNO = CommonIdcard.conver17CardTo18(idcardNO);
                    edt_idcard.setText(idcardNO);
                    Toast.makeText(SearchTwoWayActivity.this, "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
                } else {
                }
                if (people==null){
                    isSecondCard=1;
                }else if (people.getCardno().equals(idcardNO)){
                    isSecondCard=2;
                }else{
                    people=null;
                    isSecondCard=1;
                }
                Intent intent=new Intent(this,IDCardResultActivity.class);
                intent.putExtra("idcard",idcardNO);
                intent.putExtra("isSecond",isSecondCard);
                intent.putExtra("people",people);
                startActivity(intent);
            } else {
                Toast.makeText(SearchTwoWayActivity.this, "请先输入合法的身份证号", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
