package com.post.util;

import java.util.*;

/**
 * @author Barkley.Chang
 * @className:ApiUtil
 * @description: TODO
 * @date 2020-04-03 13:55
 */
public final class ApiUtil {
    private ApiUtil() {
    }

    /**
     * 将请求参数再按一定规则排序，并按排序之后的顺序拼装成字符串
     *
     * @param params
     * @return
     * @author Barkley.Chang
     * @version 2017-09-27 14:49
     */
    public static String getSortParams(Map<String, String> params) {
        String result = "";

        if (params != null && !params.isEmpty()) {
            // 删掉sign参数
//            params.remove("sign");

            Set<String> keySet = params.keySet();
            List<String> keyList = new ArrayList<String>();

            for (String key : keySet) {
                String value = params.get(key);
                // 将值为空的参数排除
                if (value != null && value.trim().length() > 0) {
                    keyList.add(key);
                }
            }

            Collections.sort(keyList, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int length = Math.min(o1.length(), o2.length());
                    for (int i = 0; i < length; i++) {
                        char c1 = o1.charAt(i);
                        char c2 = o2.charAt(i);
                        int r = c1 - c2;
                        if (r != 0) {
                            // char值小的排前边
                            return r;
                        }
                    }

                    // 2个字符串关系是str1.startsWith(str2)==true
                    // 取str2排前边
                    return o1.length() - o2.length();
                }
            });

            StringBuffer sb = new StringBuffer();
            // 将参数和参数值按照排序顺序拼装成字符串
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                sb.append(key + params.get(key));
            }
            result = sb.toString();
        }

        return result;
    }
}