package com.jinchao.population.utils.nfcutil;

/**
 * Created by user on 2017/3/27.
 */

public class NfcDecoder {
    public static String toHexString(byte[] data) {
        StringBuilder serial = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hexString = Integer.toHexString(data[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            serial.append(hexString).append(":");
        }
        serial.replace(serial.length() - 1, serial.length(), "");
        return serial.toString();
    }
}
