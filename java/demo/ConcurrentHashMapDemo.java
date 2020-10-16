package com.post.console;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Barkley.Chang
 * @className:ConcurrentHashMapDemo
 * @description: TODO
 * @date 2020-10-13 09:48
 */
public class ConcurrentHashMapDemo {
    public static void main(String[] args) {

    }

    /**
     * 线程安全类仍然要注意函数的正确使用。
     * 例如：即使用了ConcurrentHashMap，但直接使用get/put方法，仍然可能会多线程间互相覆盖。
     *
     * @param key
     * @return
     */
    private static String get(Integer key) {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
        String value = map.get(key);
        if (value == null) {
            value = new String("1");
            String previous = map.putIfAbsent(key, value);
            if (previous != null) {
                return previous;
            }
        }
        return value;
    }
}
