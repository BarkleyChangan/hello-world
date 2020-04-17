package com.post.util;

import java.security.MessageDigest;

/**
 * @author Barkley.Chang
 * @className:MD5Util
 * @description: TODO
 * @date 2020-04-03 13:50
 */
public final class MD5Util {
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private MD5Util() {
    }

    public static String encode(String origin) {
        return encode(origin, "UTF-8");
    }

    public static String encode(String origin, String charsetName) {
        String result = "";

        try {
            result = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                result = byteArrayToHexString(md.digest(result.getBytes()));
            } else {
                result = byteArrayToHexString(md.digest(result.getBytes(charsetName)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.encode("中华人民共和国"));
    }
}