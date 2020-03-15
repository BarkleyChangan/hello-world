package com.post.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

<!-- Base64编码工具类 -->
<!-- https://mvnrepository.com/artifact/commons-codec/commons-codec -->
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.14</version>
</dependency>

/**
 * 基于Commons Codec的Base64URL加密
 *
 * @author Barkley.Chang
 * @className:Base64CCUtil
 * @description: TODO
 * @date 2020-03-12 11:24
 */
public final class Base64URLCCUtil {
    private static final String ENCODING = "UTF-8";

    /**
     * URLBase64加密
     */
    public static String encode(String data) {
        try {
            byte[] encodedByte = Base64.encodeBase64URLSafe(data.getBytes(ENCODING));
            return new String(encodedByte, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * URLBase64解密
     */
    public static String decode(String data) {
        try {
            byte[] decodedByte = Base64.decodeBase64(data.getBytes(ENCODING));
            return new String(decodedByte, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 测试
     *
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) {
        String data = "找一个好姑娘做老婆是我的梦 想！";
        System.out.println("原文-->" + data);
        String encodedStr = Base64URLCCUtil.encode(data);
        String encodedStr2 = Base64CCUtil.encode(data);
        System.out.println("加密后-->" + encodedStr + ";");
        System.out.println("加密后-->" + encodedStr2 + ";");
        String decodedStr = Base64URLCCUtil.decode(encodedStr);
        System.out.println("解密后-->" + decodedStr);
        System.out.println(data.equals(decodedStr));
    }
}