package com.post.util;

import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.Base64;

/**
 * @author Barkley.Chang
 * @className:Base64Util
 * @description: TODO
 * @date 2020-03-05 12:29
 */
public final class Base64Util {
    private Base64Util() {
    }

    /**
     * Base64编码(依赖sun.misc.BASE64Decoder.jar)
     *
     * @param data 要加密的字符数组
     * @return String 加密后的16进制字符串
     */
    public static String encode(byte[] data) {
        return new BASE64Encoder().encode(data);
    }


    /**
     * Base64解码(依赖sun.misc.BASE64Decoder.jar)
     *
     * @param data 要解密的字符串
     * @return String 解密后的字符串
     * @throws IOException
     */
    public static String decode(String data) throws IOException {
        return new String(new BASE64Decoder().decodeBuffer(data));
    }

    /**
     * Base64编码(JDK1.8以后才能使用)
     *
     * @param data 要加密的字符数组
     * @return String 加密后的16进制字符串
     */
    public static String encode_JDK18(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64解码(JDK1.8以后才能使用)
     *
     * @param data 要解密的字符串
     * @return String 解密后的字符串
     * @throws IOException
     */
    public static String decode_JDK18(String data) throws IOException {
        return new String(Base64.getDecoder().decode(data));
    }

    /**
     * main方法进行测试
     *
     * @param args
     * @throws IOException
     * @throws Exception
     */
    public static void main(String[] args) throws IOException {
        String username = "中华人民共和国中央人民政府中华人民共和国中央人民政府中华人民共和国中央人民政府中华人民共和国中央人民政府中华人民共和国中央人民政府";
        String password = "admin";
        String encryptMessage = username + "\\" + password + "+";
        //编码(依赖sun.misc.BASE64Decoder.jar)
        System.out.println(encode(encryptMessage.getBytes()));//YWRtaW46YWRtaW4=
        //解码(依赖sun.misc.BASE64Decoder.jar)
        System.out.println(decode(encode(encryptMessage.getBytes())));//admin:admin

//        //编码(JDK1.8以后才能使用)
//        System.out.println(encode_JDK18(encryptMessage.getBytes()));//YWRtaW46YWRtaW4=
//        //解码(JDK1.8以后才能使用)
//        System.out.println(decode_JDK18(encode(encryptMessage.getBytes())));//admin:admin

        //编码(Spring工具类)
        String springBase64 = Base64Utils.encodeToString(encryptMessage.getBytes("UTF-8"));
        System.out.println(springBase64);//YWRtaW46YWRtaW4=
        System.out.println(Base64Utils.encodeToUrlSafeString(encryptMessage.getBytes("UTF-8")));
        //解码(Spring工具类)
        System.out.println(new String(Base64Utils.decodeFromString(springBase64)));//admin:admin

    }
}