package com.post.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Barkley.Chang
 * @className:JacksonUtil
 * @description: TODO
 * @date 2020-01-19 09:41
 */
public final class JacksonUtil {
    private JacksonUtil() {
    }

    private static ObjectMapper mapper = null;

    /**
     * bean 转换为 Json 字符串
     */
    public static String bean2json(Object object) {
        mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转换为 bean
     */
    public static Object json2bean(String json, Class c) {
        mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * list 转换为 Json 字符串
     */
    public static String list2json(List list) {
        mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转换为 list
     */
    public static List json2List(String json) {
        mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * set 转换为 Json 字符串
     */
    public static String set2json(Set set) {
        mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(set);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转换为 set
     */
    public static Set json2Set(String json) {
        mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Set.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map 转换为 Json 字符串
     */
    public static String map2json(Map map) {
        mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转换为 map
     */
    public static Map json2Map(String json) {
        mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}