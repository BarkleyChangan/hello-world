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
 * 基于Commons Codec的Base64加密
 *
 * @author Barkley.Chang
 * @className:Base64CCUtil
 * @description: TODO
 * @date 2020-03-12 11:24
 */
public final class Base64CCUtil {
    private static final String ENCODING = "UTF-8";

    private Base64CCUtil() {
    }

    /**
     * 一般Base64加密
     */
    public static String encode(String data) {
        try {
            byte[] encodedByte = Base64.encodeBase64(data.getBytes(ENCODING));
            return new String(encodedByte, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 安全Base64加密
     */
    public static String encodeSafe(String data) {
        /*
         * 注意：这里采用的encodeBase64(byte[] bytes, boolean arg1)
         * arg1为true时，加密后的字符串每行为76个字符，不论每行够不够76个字符，都要在行尾添加“\r\n”
         */
        try {
            byte[] encodedByte = Base64.encodeBase64(data.getBytes(ENCODING), true);
            return new String(encodedByte, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Base64解密
     */
    public static String decode(String data) {
        try {
            byte[] decodedByte = Base64.decodeBase64(data.getBytes(ENCODING));
            return new String(decodedByte, ENCODING);
        } catch (UnsupportedEncodingException e) {
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
        /********************测试一般encode*********************/
        String data = "找一个好姑娘做老婆是我的梦 想！";
        System.out.println("原文-->" + data);
        String encodedStr = Base64CCUtil.encode(data);
        System.out.println("加密后-->" + encodedStr);
        String decodedStr = Base64CCUtil.decode(encodedStr);
        System.out.println("解密后-->" + decodedStr);
        System.out.println(data.equals(decodedStr));
        System.out.println("================================");
        /********************测试安全encode*********************/
        String data2 = "找一个好姑娘做老婆是我的梦 想！找一个好姑娘做老婆是我的梦 想！";
        System.out.println("原文-->" + data2);
        String encodedStr2 = Base64CCUtil.encodeSafe(data2);
        System.out.println("加密后-->" + encodedStr2);
        String decodedStr2 = Base64CCUtil.decode(encodedStr2);
        System.out.println("解密后-->" + decodedStr2);
        System.out.println(data2.equals(decodedStr2));
    }
}