package com.jetsun.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CharMD5 {
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public CharMD5() {
    }
    public static String MD5(String strs){
        String re_md5 = new String();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strs.getBytes());
            byte b[] = md.digest();
            re_md5 = getFormattedText(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            re_md5="";
        }
        return re_md5;

    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        //把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++){
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

}
