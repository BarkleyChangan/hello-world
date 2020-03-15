package com.post.util;

/**
 * @author Barkley.Chang
 * @className:HexUtil
 * @description: TODO
 * @date 2020-01-24 07:48
 */
public final class HexUtil {
    private HexUtil() {
    }

    public static String format(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int n = 0;
        for (byte b : data) {
            if (n % 16 == 0) {
                sb.append(String.format("%05X: ", n));
            }

            sb.append(String.format("%02X ", b));
            n++;
            if (n % 16 == 0) {
                sb.append(String.format("%n"));
            }
        }

        sb.append(String.format("%n"));
        return sb.toString();
    }
}
