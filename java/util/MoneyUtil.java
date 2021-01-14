package com.post.util;

import java.util.Arrays;

/**
 * @author Barkley.Chang
 * @className:MoneyUtil
 * @description: TODO
 * @date 2020-05-25 09:10
 */
public final class MoneyUtil {
    private static String[] hanArr = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static String[] unitArr = {"十", "百", "千"};

    private MoneyUtil() {
    }

    /**
     * 把一个四位数字字符串变成汉字字符串
     *
     * @param numStr
     * @return
     */
    public static String toHanStr(String numStr) {
        String result = "";
        int numLen = numStr.length();

        for (int i = 0; i < numLen; i++) {
            int num = numStr.charAt(i);

            if (i != (numLen - 1) && num != 0) {
                result += hanArr[num] + unitArr[numLen - 2 - i];
            } else {
                result += hanArr[num];
            }
        }

        return result;
    }

    /**
     * 把一个浮点数分解成整数部分和小数部分字符串
     *
     * @param num
     * @return
     */
    public static String[] divide(double num) {
        long zheng = (long) num;
        long xiao = Math.round((num - zheng) * 100);
        return new String[]{String.valueOf(zheng), String.valueOf(xiao)};
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(MoneyUtil.divide(236711125.123)));
        System.out.println(MoneyUtil.toHanStr("6109"));
    }
}