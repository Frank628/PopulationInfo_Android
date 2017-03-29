package com.jinchao.population.utils.nfcutil;

import android.nfc.Tag;
import android.nfc.tech.NfcA;

import java.io.IOException;

/**
 * Created by user on 2017/3/27.
 * use nfca to setting password for ntag216
 *
 */

public class NfcNfcAOperation {
    public final static byte CMD_READ = (byte)0x30;
    public final static byte CMD_FAST_READ = (byte)0x3A;
    public final static byte CMD_WRITE = (byte)0xA2;
    public final static byte CMD_PWD_AUTH = (byte)0x1B;
    public final static byte PWD_ADDRESS_216 = (byte)0xE5;
    public final static byte PACK_ADDRESS_216 = (byte)0xE6;
    public final static byte PROT2_ADDRESS_216 = (byte)0xE3;
    public final static byte PROT_ADDRESS_216 = (byte)0xE4;
    public final static byte STATIC_LOCK_BITS_ADDRESS = (byte)0x02;
    public static byte[] DEFALT_PACK = new byte[] { (byte)0xFF, (byte)0xFF };
    public final static byte[] DEFALT_PASSWORD = new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    public final static byte[] PACK = new byte[] {(byte) 0x22, (byte) 0x22};
    public interface OnSetPasswordCallBack{
        void success();
        void error(String error);
    }

    public static boolean isPasswordProtected(NfcA nfcA){
        try {
            byte[] pwd_auth_result=nfcA.transceive((new byte[] {//默认密码密码认证
                    CMD_PWD_AUTH, // PWD_AUTH
                    DEFALT_PASSWORD[0], DEFALT_PASSWORD[1], DEFALT_PASSWORD[2], DEFALT_PASSWORD[3]}));
            return pwd_auth_result[0]==(byte) 0xff&&pwd_auth_result[1]==(byte) 0xff&&pwd_auth_result[2]==(byte) 0xff&&pwd_auth_result[3]==(byte) 0xff;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean removePasswordFromNtag216(NfcA nfcA,String password){
        byte[] pwd=new byte[]{password.getBytes()[0],password.getBytes()[1],password.getBytes()[2],password.getBytes()[3]};
        try {
            byte[] pwd_auth_result=nfcA.transceive((new byte[] {//默认密码密码认证
                    CMD_PWD_AUTH, // PWD_AUTH
                    pwd[0], pwd[1], pwd[2], pwd[3]}));
            byte[] set_pwd_result=nfcA.transceive((new byte[] {
                    CMD_WRITE, // 写入密码
                    PWD_ADDRESS_216,
                    DEFALT_PASSWORD[0], DEFALT_PASSWORD[1], DEFALT_PASSWORD[2], DEFALT_PASSWORD[3]}));
            byte[] set_pack_result=nfcA.transceive((new byte[] {
                    CMD_WRITE, // 设置pack值
                    PACK_ADDRESS_216,
                    DEFALT_PACK[0], DEFALT_PACK[1], 0, 0}));
            byte[] response = nfcA.transceive(new byte[] {
                    CMD_READ, // READ
                    PROT_ADDRESS_216  // page address
            });

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static void setPasswordForNtag216(Tag tag,String password ,OnSetPasswordCallBack onSetPasswordCallBack){
        byte[] pwd=new byte[]{password.getBytes()[0],password.getBytes()[1],password.getBytes()[2],password.getBytes()[3]};
        NfcA nfcA= NfcA.get(tag);
        try {
            nfcA.connect();
                byte[] set_pwd_result=nfcA.transceive((new byte[] {
                        CMD_WRITE, // 写入密码
                        PWD_ADDRESS_216,
                        pwd[0], pwd[1], pwd[2], pwd[3]}));
                byte[] set_pack_result=nfcA.transceive((new byte[] {
                        CMD_WRITE, // 设置pack值
                        PACK_ADDRESS_216,
                        PACK[0], PACK[1], 0, 0}));
                byte[] response = nfcA.transceive(new byte[] {
                        CMD_READ, // READ
                        PROT_ADDRESS_216  // page address
                });
                if ((response != null) && (response.length >= 16)) {  // read always returns 4 pages
                    boolean prot = false;  // false = PWD_AUTH for write only, true = PWD_AUTH for read and write
                    int authlim = 0; // value between 0 and 7
                    response = nfcA.transceive(new byte[] {
                            CMD_WRITE, // WRITE
                            PROT_ADDRESS_216,   // page address
                            (byte) ((response[0] & 0x078) | (prot ? 0x080 : 0x000) | (authlim & 0x007)),
                            response[1], response[2], response[3]  // keep old value for bytes 1-3, you could also simply set them to 0 as they are currently RFU and must always be written as 0 (response[1], response[2], response[3] will contain 0 too as they contain the read RFU value)
                    });
                }
                byte[] response_pageprot = nfcA.transceive(new byte[] {
                        CMD_READ, // READ
                        PROT2_ADDRESS_216     // page address
                });
                if ((response_pageprot != null) && (response_pageprot.length >= 16)) {  // read always returns 4 pages
                    boolean prot1 = false;  // false = PWD_AUTH for write only, true = PWD_AUTH for read and write
                    int auth0 = 0; // first page to be protected, set to a value between 0 and 37 for NTAG212
                    response_pageprot = nfcA.transceive(new byte[] {
                            CMD_READ, // WRITE
                            PROT2_ADDRESS_216 ,   // page address
                            response_pageprot[0], // keep old value for byte 0
                            response_pageprot[1], // keep old value for byte 1
                            response_pageprot[2], // keep old value for byte 2
                            (byte) (auth0 & 0x0ff)
                    });
                }
                nfcA.close();
                onSetPasswordCallBack.success();
        } catch (IOException e) {
        onSetPasswordCallBack.error(e.getMessage());
        }


    }


}
