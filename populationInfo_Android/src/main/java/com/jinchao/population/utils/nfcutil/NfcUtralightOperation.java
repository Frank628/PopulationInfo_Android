package com.jinchao.population.utils.nfcutil;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.text.TextUtils;

import com.jinchao.population.config.Constants;
import com.nxp.nfclib.classic.MFClassic;
import com.nxp.nfclib.exceptions.SmartCardException;
import com.nxp.nfclib.icode.ICodeSLI;
import com.nxp.nfclib.icode.ICodeSLIL;
import com.nxp.nfclib.icode.ICodeSLIS;
import com.nxp.nfclib.icode.ICodeSLIX;
import com.nxp.nfclib.icode.ICodeSLIX2;
import com.nxp.nfclib.icode.ICodeSLIXL;
import com.nxp.nfclib.icode.ICodeSLIXS;
import com.nxp.nfclib.ntag.NTag203x;
import com.nxp.nfclib.ntag.NTag210;
import com.nxp.nfclib.ntag.NTag213215216;
import com.nxp.nfclib.ntag.NTag213F216F;
import com.nxp.nfclib.ntag.NTagI2C;
import com.nxp.nfclib.plus.PlusSL1;
import com.nxp.nfclib.ultralight.Ultralight;
import com.nxp.nfclib.ultralight.UltralightC;
import com.nxp.nfclib.ultralight.UltralightEV1;
import com.nxp.nfcliblite.Interface.Inxpnfcliblitecallback;
import com.nxp.nfcliblite.Interface.NxpNfcLibLite;
import com.nxp.nfcliblite.cards.DESFire;
import com.nxp.nfcliblite.cards.Plus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by user on 2017/3/27.
 */

public class NfcUtralightOperation {
    public static byte[] PACK_PWD = new byte[] { 0x22, 0x22 };
    private static boolean isNtag21x=false;//如果没有读到21x类型，最后返回error信息
    /**
     * 设置写入密码保护
     * @param nxpNfcLib
     * @param intent
     * @param password
     * @param text
     * @param nfcWriteCallBackListener
     */
    public static void writeNDEFwithPassword(NxpNfcLibLite nxpNfcLib, Intent intent, String password, final String text, final NfcOperation.NFCWriteCallBackListener nfcWriteCallBackListener){
        if (password==null)return;
        if (TextUtils.isEmpty(password))return;
        final byte[] pwd=NfcDecoder.hexToBytes(password);
        if (pwd.length!=4){
            nfcWriteCallBackListener.error("密码必须为四位！");
            return;
        }
        isNtag21x=false;
        nxpNfcLib.filterIntent(intent, new Inxpnfcliblitecallback() {
            @Override
            public void onNTag213215216CardDetected(NTag213215216 tag) {
                isNtag21x=true;
                if (tag instanceof  NTag213215216){
                    try {
                        tag.connect();
                        tag.programPWDPack(pwd,PACK_PWD);
                        tag.enablePasswordProtection(false,tag.getFirstUserpage());
                    } catch (SmartCardException e) {
                        nfcWriteCallBackListener.error("此芯片已加密或锁死");
                    } catch (IOException e) {
                        nfcWriteCallBackListener.error("此芯片已加密或锁死");
                    }
                    try {
                        tag.authenticatePwd(pwd,PACK_PWD);
                        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] { NdefGeneration.createTextRecord(DataEncryptDecrypt.encrypt(text, Constants.KAISA_OFFSET)) });
                        tag.writeNDEF(ndefMessage);
                        nfcWriteCallBackListener.success(NfcDecoder.toHexString(tag.getUID()));
                    } catch (SmartCardException e) {
                        nfcWriteCallBackListener.error("此芯片已加密或锁死");
                    } catch (IOException e) {
                        nfcWriteCallBackListener.error("此芯片已加密或锁死");
                    }finally {
                        try {
                            tag.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    nfcWriteCallBackListener.error("非NXP的NTAG21x系列芯片");
                }
            }
            @Override
            public void onUltraLightCardDetected(Ultralight ultralight) {}
            @Override
            public void onClassicCardDetected(MFClassic mfClassic) {}
            @Override
            public void onDESFireCardDetected(DESFire desFire) {}
            @Override
            public void onUltraLightEV1CardDetected(UltralightEV1 ultralightEV1) {}
            @Override
            public void onUltraLightCCardDetected(UltralightC ultralightC) {}
            @Override
            public void onPlusCardDetected(Plus plus) {}
            @Override
            public void onNTag203xCardDetected(NTag203x nTag203x) {}
            @Override
            public void onNTag210CardDetected(NTag210 nTag210) {}
            @Override
            public void onNTag213F216FCardDetected(NTag213F216F nTag213F216F) {}
            @Override
            public void onNTagI2CCardDetected(NTagI2C nTagI2C) {}
            @Override
            public void onICodeSLIDetected(ICodeSLI iCodeSLI) {}
            @Override
            public void onICodeSLISDetected(ICodeSLIS iCodeSLIS) {}
            @Override
            public void onICodeSLILDetected(ICodeSLIL iCodeSLIL) {}
            @Override
            public void onICodeSLIXDetected(ICodeSLIX iCodeSLIX) {}
            @Override
            public void onICodeSLIXSDetected(ICodeSLIXS iCodeSLIXS) {}
            @Override
            public void onICodeSLIXLDetected(ICodeSLIXL iCodeSLIXL) {}
            @Override
            public void onPlusSL1CardDetected(PlusSL1 plusSL1) {}
            @Override
            public void onICodeSLIX2Detected(ICodeSLIX2 iCodeSLIX2) {}
        });
        if (!isNtag21x){
            nfcWriteCallBackListener.error("非正规Ntag21x系列芯片");
        }
    }

}
