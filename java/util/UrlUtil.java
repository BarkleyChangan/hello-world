package com.hnpost.icbcapi.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Barkley.Chang
 * @className:UrlUtil
 * @description: TODO
 * @date 2020-03-05 12:18
 */
public final class UrlUtil {
    private UrlUtil() {
    }

    public static String encode(String url, String charSet) {
        if (url == null || url.trim().length() == 0) {
            return "";
        }

        if (charSet == null || charSet.trim().length() == 0) {
            charSet = "UTF-8";
        }

        String result = "";

        try {
            result = URLEncoder.encode(url, charSet).replaceAll("\\+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String decode(String url, String charSet) {
        if (url == null || url.trim().length() == 0) {
            return "";
        }

        if (charSet == null || charSet.trim().length() == 0) {
            charSet = "UTF-8";
        }

        String result = "";

        try {
            result = URLDecoder.decode(url, charSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // encodeURIComponent("aa+aa aa")
        // "aa%2Baa%20aa"
        String charSet = "UTF-8";
        // aa%2Baa+aa
        String str1 = URLEncoder.encode("aa+aa aa", charSet);
        // aa%2Baa%20aa
        String str2 = UrlUtil.encode("aa+aa aa", charSet);
        System.out.println("aa+aa aa ===> " + str1);
        System.out.println("aa+aa aa ===> " + str2);
        System.out.println("=======================");
        System.out.println(UrlUtil.decode(str1, charSet));
        System.out.println(UrlUtil.decode(str2, charSet));
        System.out.println();
    }
}