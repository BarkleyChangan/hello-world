package com.post.util;

import java.util.ArrayList;
import java.util.List;

public final class StringUtil {
    private StringUtil() {
    }
    
    /**
     * 字符串脱敏:将字符串中的中间位置关键字替换为*号
     * 
     * @param strContent
     *            输入的字符串内容
     * @param count
     *            替换*号的个数
     * @return
     */
    public static String replaceAsteriskMiddle(String strContent, int count) {
        if (strContent == null || strContent.trim().length() == 0) {
            return "";
        }

        if (count < 1) {
            return strContent;
        }

        int showCount = strContent.length() - count;

        if (showCount < 1) {
            return strContent;
        }

        int startIndex = showCount / 2;
        startIndex = startIndex == 0 ? 1 : startIndex;

        // 保存需要替换为*号的位置index集合
        List<Integer> list = new ArrayList<Integer>();

        for (int i = startIndex; i < startIndex + count; i++) {
            list.add(Integer.valueOf(i));
        }

        if (list.size() == 0) {
            return strContent;
        }

        final String SYMBOL_ASTERISK = "*";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strContent.length(); i++) {
            if (list.contains(Integer.valueOf(i))) {
                sb.append(SYMBOL_ASTERISK);
            } else {
                sb.append(strContent.charAt(i));
            }
        }

        return sb.toString();
    }

    /**
     * 字符串脱敏:将字符串中的开始、结束位置关键字替换为*号
     * 
     * @param strContent
     *            输入的字符串内容
     * @param count
     *            替换*号的个数
     * @return
     */
    public static String replaceAsteriskStartEnd(String strContent, int count) {
        if (strContent == null || strContent.trim().length() == 0) {
            return "";
        }

        if (count < 1) {
            return strContent;
        }

        int showCount = strContent.length() - count;

        if (showCount < 1) {
            return strContent;
        }

        // 保存需要替换为*号的位置index集合
        List<Integer> list = new ArrayList<Integer>();

        // 存入开始位置index集合
        for (int i = 0; i < count; i++) {
            list.add(Integer.valueOf(i));
        }

        int showEndCount = strContent.length() - 2 * count;
        if (showEndCount > 0) {
            // 存入结束位置index集合
            for (int i = (strContent.length() - count); i < strContent.length(); i++) {
                list.add(Integer.valueOf(i));
            }
        }

        if (list.size() == 0) {
            return strContent;
        }

        final String SYMBOL_ASTERISK = "*";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strContent.length(); i++) {
            if (list.contains(Integer.valueOf(i))) {
                sb.append(SYMBOL_ASTERISK);
            } else {
                sb.append(strContent.charAt(i));
            }
        }

        return sb.toString();
    }

    /**
     * 去掉指定的文件扩展名扩展名
     *
     * @param fileName
     * @return
     * @author Barkley.Chang
     * @date 2020-03-16 16:59
     */
    public static String getFileNameWithoutExt(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) {
            return "";
        }

        // 忽略大小写
        String result = fileName.replaceAll("(?i)\\.(txt|enc|check|sign)", "");

        return result;
    }
}