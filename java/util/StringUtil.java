package com.post.util;

import java.util.ArrayList;
import java.util.List;

public final class StringUtil {
    private StringUtil() {
    }

    /**
     * 将字符串中的关键字替换为*号
     *
     * @param strContent 输入的字符串内容
     * @param count      替换*号的个数
     * @param isMiddle   true: 替换中间的字符为*号; false: 替换前后的字符为*号
     * @return
     */
    public static String replaceAsterisk(String strContent, int count, boolean isMiddle) {
        if (strContent == null || strContent.trim().length() == 0) {
            return "";
        }

        if (count < 1) {
            return strContent;
        }

        // 替换两边为*号时,防止全部都替换为*号
        if (!isMiddle && strContent.length() == (count * 2)) {
            count -= 1;
        }

        int endIndex = strContent.length() - count;

        if (endIndex <= 1) {
            return strContent;
        }

        // 保存需要替换为*号的位置index集合
        List<Integer> list = new ArrayList<Integer>();

        for (int i = count; i < endIndex; i++) {
            list.add(Integer.valueOf(i));
        }

        if (list.size() == 0) {
            return strContent;
        }

        final String SYMBOL_ASTERISK = "*";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strContent.length(); i++) {
            if (isMiddle) {
                if (list.contains(Integer.valueOf(i))) {
                    sb.append(SYMBOL_ASTERISK);
                } else {
                    sb.append(strContent.charAt(i));
                }
            } else {
                if (list.contains(Integer.valueOf(i))) {
                    sb.append(strContent.charAt(i));
                } else {
                    sb.append(SYMBOL_ASTERISK);
                }
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