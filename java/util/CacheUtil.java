package com.post.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Barkley.Chang
 * @date 2019-10-10 13:59
 */
public final class CacheUtil {
    private CacheUtil() {
    }

    static Map<String, Object> map = new HashMap<>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();

    /**
     * 获取一个key对应的value
     *
     * @param key
     * @return
     */
    public static final Object get(String key) {
        r.lock();

        try {
            return map.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            r.unlock();
        }

        return null;
    }

    /**
     * 设置key对应的value,并返回旧的value
     *
     * @param key
     * @param value
     * @return
     */
    public static final Object put(String key, Object value) {
        w.lock();

        try {
            return map.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            w.unlock();
        }

        return null;
    }

    public static final void clear() {
        w.lock();

        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }
}