package com.bolingcavalry.jacksondemo.anno;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Barkley.Chang
 * @className:JacksonJsonUtil
 * @description: Jackson Json工具类
 * @date 2021-01-29 15:42
 */
public final class JacksonJsonUtil {
    private final static ObjectMapper objectMapper;
    private final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    static {
        objectMapper = new ObjectMapper();
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略空Bean转json错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 所有的日期格式都统一为以下的样式,即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(FORMAT_DATETIME));
        // 忽略在json字符串中存在但是在Java对象中不存在对应属性的情况,防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JacksonJsonUtil() {
    }

    /**
     * 对象转Json格式字符串
     *
     * @param obj 对象
     * @return Json格式字符串
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return "";
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     *
     * @param obj 对象
     * @return 美化的Json格式字符串
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return "";
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param str   要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (str == null || str.trim().length() == 0 || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (str == null || str.trim().length() == 0 || typeReference == null) {
            return null;
        }

        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T string2Obj(String str, Class<?> collectionClazz, Class<?>... elementClasses) {
        if (str == null || str.trim().length() == 0 || collectionClazz == null) {
            return null;
        }

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elementClasses);
        try {
            objectMapper.readValue(str, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}