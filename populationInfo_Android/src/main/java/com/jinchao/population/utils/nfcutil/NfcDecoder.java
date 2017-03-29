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
    public static byte[] hexToBytes(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (bytesToHex(achar[pos]) << 4 | bytesToHex(achar[pos + 1]));
        }
        return result;
    }

    private static byte bytesToHex(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
