package com.jinchao.population.utils.nfcutil;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.widget.Toast;

import com.jinchao.population.config.Constants;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by OfferJiShu01 on 2017/3/8.
 */

public class NfcOperation {
    public final static byte CMD_READ = (byte)0x30;
    public final static byte CMD_FAST_READ = (byte)0x3A;
    public final static byte CMD_WRITE = (byte)0xA2;
    public final static byte CMD_PWD_AUTH = (byte)0x1B;
    public final static byte PWD_ADDRESS_216 = (byte)0xE5;
    public final static byte PACK_ADDRESS_216 = (byte)0xE6;
    public final static byte AUTH0_ADDRESS_216 = (byte)0xE3;
    public final static byte STATIC_LOCK_BITS_ADDRESS = (byte)0x02;
    public final static byte[] DEFAULT_PASSWORD = new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    public final static byte[] DEFINE_PASSWORD = new byte[] {(byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88};
    public final static byte[] PACK = new byte[] {(byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88};
    public interface NFCWriteCallBackListener{
        void success();
        void error(String error);
    }
    public interface NFCReadCallBackListener{
        void success(String result);
        void error(String error);
    }

    /**
     * 创建NDEF文本数据
     * @param text
     * @return
     */
    public static NdefRecord createTextRecord(String text) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        //将文本转换为UTF-8格式
        byte[] textBytes = text.getBytes(utfEncoding);
        //设置状态字节编码最高位数为0
        int utfBit = 0;
        //定义状态字节
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置第一个状态字节，先将状态码转换成字节
        data[0] = (byte) status;
        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
        //到textBytes.length的位置
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }
    /**
     * 写数据
     * @param text 写入的文本数据
     * @param tag 标签
     * @return
     */
    public static void NfcWriteNDEF(String text, Tag tag,NFCWriteCallBackListener nfcWrite) {
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] { createTextRecord(DataEncryptDecrypt.encrypt(text, Constants.KAISA_OFFSET)) });
        int size = ndefMessage.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef!=null){
                ndef.connect();
                if (!ndef.isWritable()) {
                    nfcWrite.error("此标签加密锁死或不支持");
                    return;
                }
                if (ndef.getMaxSize() < size) {
                    nfcWrite.error("此标签存储空间不足");
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                nfcWrite.success();
            }else{
                //Ndef格式类
                NdefFormatable format = NdefFormatable.get(tag);
                //判断是否获得了NdefFormatable对象，有一些标签是只读的或者不允许格式化的
                if (format != null) {
                    //连接
                    format.connect();
                    //格式化并将信息写入标签
                    format.format(ndefMessage);
                    nfcWrite.success();
                } else {
                    nfcWrite.error("无法格式化此标签");
                }
            }
        } catch (Exception e) {
            nfcWrite.error("无法获取NDEF对象");
        }
    }


    /**
     * 读取NFC标签文本数据
     */
    public static void NfcreadNDEF(Intent intent,NFCReadCallBackListener nfcRead) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    String textRecord = parseTextRecord(record);
                    nfcRead.success(DataEncryptDecrypt.decrypt(textRecord,Constants.KAISA_OFFSET));
                }else{
                    nfcRead.error("此标签中无内容");
                }
            } catch (Exception e) {
                nfcRead.error("此标签类型不匹配！");
            }
        }else{
            nfcRead.error("无效的NFC卡片类型");
        }
    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     * @param ndefRecord
     * @return
     */
    public static String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();

        }
    }

    public static void AuthNFCTag(Tag tag,NFCWriteCallBackListener nfcWrite){
        String error="defaul";
        MifareUltralight  ultralight= MifareUltralight.get(tag);
        try {
            ultralight.connect();
            try {
//                byte[] result=ultralight.transceive((new byte[] {
//                        CMD_PWD_AUTH, // PWD_AUTH
//                        DEFINE_PASSWORD[0], DEFINE_PASSWORD[1], DEFINE_PASSWORD[2], DEFINE_PASSWORD[3]}));

                byte[] result=ultralight.transceive((new byte[] {
                        CMD_WRITE, // PWD_AUTH
                        PWD_ADDRESS_216,
                        DEFINE_PASSWORD[0], DEFINE_PASSWORD[1], DEFINE_PASSWORD[2], DEFINE_PASSWORD[3]}));
                byte[] pack_result=ultralight.transceive((new byte[] {
                        CMD_WRITE, // PWD_AUTH
                        PACK_ADDRESS_216,
                        DEFINE_PASSWORD[0], DEFINE_PASSWORD[1], 0, 0}));
                byte[] response = ultralight.transceive(new byte[] {
                        (byte) 0x30, // READ
                        (byte) 38    // page address
                });
                if ((response != null) && (response.length >= 16)) {  // read always returns 4 pages
                    boolean prot = false;  // false = PWD_AUTH for write only, true = PWD_AUTH for read and write
                    int authlim = 0; // value between 0 and 7
                    response = ultralight.transceive(new byte[] {
                            (byte) 0xA2, // WRITE
                            (byte) 0x2A,   // page address
                            (byte) ((response[0] & 0x078) | (prot ? 0x080 : 0x000) | (authlim & 0x007)),
                            response[1], response[2], response[3]  // keep old value for bytes 1-3, you could also simply set them to 0 as they are currently RFU and must always be written as 0 (response[1], response[2], response[3] will contain 0 too as they contain the read RFU value)
                    });
                }
                byte[] response_pageprot = ultralight.transceive(new byte[] {
                        (byte) 0x30, // READ
                        (byte) 0x83     // page address
                });
                if ((response_pageprot != null) && (response_pageprot.length >= 16)) {  // read always returns 4 pages
                    boolean prot1 = false;  // false = PWD_AUTH for write only, true = PWD_AUTH for read and write
                    int auth0 = 0; // first page to be protected, set to a value between 0 and 37 for NTAG212
                    response_pageprot = ultralight.transceive(new byte[] {
                            (byte) 0xA2, // WRITE
                            (byte) 0x83 ,   // page address
                            response_pageprot[0], // keep old value for byte 0
                            response_pageprot[1], // keep old value for byte 1
                            response_pageprot[2], // keep old value for byte 2
                            (byte) (auth0 & 0x0ff)
                    });
                }
                nfcWrite.error(result.toString()+"aa");
                ultralight.close();
            } catch (IOException e) {
                nfcWrite.error(e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
