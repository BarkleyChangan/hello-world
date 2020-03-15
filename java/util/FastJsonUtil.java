package com.post.util;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 使用 fastjson 需要注意，转换的类必须有默认的无参构造函数
 *
 * @author Barkley.Chang
 * @className:FastJsonUtil
 * @description: TODO
 * @date 2020-01-19 09:37
 */
public final class FastJsonUtil {
    private FastJsonUtil() {
    }

    /**
     * bean 转换为 Json 字符串
     */
    public static String bean2json(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * json 转换为 bean
     */
    public static Object json2bean(String json, Class c) {
        return JSON.parseObject(json, c);
    }

    /**
     * list 转换为 Json 字符串
     */
    public static String list2json(List list) {
        return JSON.toJSONString(list);
    }

    /**
     * json 转换为 list
     */
    public static List json2List(String json, Class c) {
        return JSON.parseArray(json, c);
    }

    /**
     * set 转换为 Json 字符串
     */
    public static String set2json(Set set) {
        return JSON.toJSONString(set);
    }

    /**
     * json 转换为 set
     */
    public static Set json2Set(String json) {
        return JSON.parseObject(json, Set.class);
    }

    /**
     * map 转换为 Json 字符串
     */
    public static String map2json(Map map) {
        return JSON.toJSONString(map);
    }

    /**
     * json 转换为 map
     */
    public static Map json2Map(String json) {
        return JSON.parseObject(json);
    }
}