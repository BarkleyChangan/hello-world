package com.post.common.util;

import org.apache.commons.collections4.map.LRUMap;

/**
 * @author Barkley.Chang
 * @className:IdempotentUtil
 * @description: 请求幂等性判断
 * <!-- 集合工具类 apache commons collections -->
 * <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-collections4 -->
 * <dependency>
 * <groupId>org.apache.commons</groupId>
 * <artifactId>commons-collections4</artifactId>
 * <version>4.4</version>
 * </dependency>
 * @date 2020-07-20 10:21
 */
public final class IdempotentUtil {
    // 根据LRU(Least Recently Used，最近最少使用)算法淘汰数据的Map集合，最大容量100个
    private final static LRUMap<String, Integer> reqCache = new LRUMap<>(100);

    private IdempotentUtil() {
    }

    /**
     * 幂等性判断
     *
     * @param id
     * @param lockClazz
     * @return
     */
    public static boolean judge(String id, Object lockClazz) {
        synchronized (lockClazz) {
            // 重复请求判断
            if (reqCache.containsKey(id)) {
                // 重复请求
                System.out.println("请勿重复提交！！！" + id);
                return false;
            }

            // 非重复请求,存储请求ID
            reqCache.put(id, 1);
        }

        return true;
    }
}