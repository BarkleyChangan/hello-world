package com.post.web.controller;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * @author Barkley.Chang
 * @className:RepeatSubmitController
 * @description: 防止重复提交
 * @date 2020-07-20 10:06
 */
@RequestMapping("repeatSubmit")
@Controller
public class RepeatSubmitController {
    // 请求ID存储集合
    private final String[] reqCache = new String[100];
    // 请求计数器(指明ID在数组中的下标)
    private static int reqCacheIndex = 0;

    // 最大容量100个,根据LRU算法淘汰数据的Map集合
    private final static LRUMap<String, Integer> reqLRUCache = new LRUMap<>(100);

    /**
     * 双重检测锁(DCL)版
     *
     * @param requestId
     * @return
     */
    @RequestMapping("/add1")
    public String addUser1(String requestId) {
        // TODO:非空判断
        // 重复请求判断
        if (Arrays.asList(reqCache).contains(requestId)) {
            // 重复请求
            System.out.println("请勿重复提交!" + requestId);
            return "Fail";
        }

        synchronized (this.getClass()) {
            // 双重检查锁(DCL,double checked locking)提高程序的执行效率
            if (Arrays.asList(reqCache).contains(requestId)) {
                // 重复请求
                System.out.println("请勿重复提交!" + requestId);
                return "Fail";
            }

            // 记录请求ID
            if (reqCacheIndex >= reqCache.length) {
                // 重置计数器
                reqCacheIndex = 0;
            }

            // 将ID保存到缓存
            reqCache[reqCacheIndex] = requestId;
            // 下表往后移一位
            reqCacheIndex++;
        }

        // 业务代码...
        System.out.println("添加用户ID:" + requestId);
        return "Success";
    }

    /**
     * LRUMap版(需引入commons-collections4)
     * <!-- 集合工具类 apache commons collections -->
     * <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-collections4 -->
     * <dependency>
     * <groupId>org.apache.commons</groupId>
     * <artifactId>commons-collections4</artifactId>
     * <version>4.4</version>
     * </dependency>
     *
     * @param requestId
     * @return
     */
    @RequestMapping("/add2")
    public String addUser2(String requestId) {
        // TODO:非空判断
        synchronized (this.getClass()) {
            // 重复请求判断
            if (reqLRUCache.containsKey(requestId)) {
                // 重复请求
                System.out.println("请勿重复提交!" + requestId);
                return "Fail";
            }

            // 存储请求ID
            reqLRUCache.putIfAbsent(requestId, 1);
        }

        // 业务代码...
        System.out.println("添加用户ID:" + requestId);
        return "Success";
    }
}