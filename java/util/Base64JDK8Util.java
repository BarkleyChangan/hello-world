package com.post.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author Barkley.Chang
 * @className:Base64JDK8Util
 * @description: TODO
 * @date 2020-06-01 10:10
 */
public final class Base64JDK8Util {
    private Base64JDK8Util() {
    }

    /**
     * 将Basic编码存储在一个长行上
     *
     * @return
     */
    public static Base64.Encoder getEncoder() {
        return Base64.getEncoder();
    }

    /**
     * 将MIME编码存储在多行中
     *
     * @return
     */
    public static Base64.Encoder getMimeEncoder() {
        return Base64.getMimeEncoder();
    }

    /**
     * 标准的Base64编码中可能出现字符+和/字符，不能直接用在URL中，需要对其进行处理，把字符+和/分别变成-和_
     *
     * @return
     */
    public static Base64.Encoder getUrlEncoder() {
        return Base64.getUrlEncoder();
    }

    public static Base64.Decoder getDecoder() {
        return Base64.getDecoder();
    }

    public static Base64.Decoder getMimeDecoder() {
        return Base64.getMimeDecoder();
    }

    public static Base64.Decoder getUrlDecoder() {
        return Base64.getUrlDecoder();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Base64.Encoder enc1 = getEncoder();
        Base64.Encoder enc2 = getMimeEncoder();
        Base64.Encoder enc3 = getUrlEncoder();

        System.out.println(enc1.encodeToString("中华人民共和国12中华人民共和国中华人民4共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国".getBytes
                ("UTF-8")));
        System.out.println("================================");
        System.out.println(enc2.encodeToString("中华人民共和国12中华人民共和国中华人民4共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国".getBytes
                ("UTF-8")));
        System.out.println("================================");
        System.out.println(enc3.encodeToString("中华人民共和国12中华人民共和国中华人民4共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国".getBytes
                ("UTF-8")));

        // URL安全的Base64编码
        String safeEncoder = getUrlEncoder().encodeToString("但愿人长久 千里共婵娟".getBytes());
        System.out.println("safeEncoder:" + safeEncoder);
        // URL安全的Base64解码
        Stirng safeDecoder = new String(getUrlDecoder().decode(safeEncoder));
        System.out.println("safeDecoder:" + safeDecoder);
    }
}