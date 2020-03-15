package com.post.util;

import java.util.concurrent.TimeUnit;

/**
 * @author Barkley.Chang
 * @date 2019-10-09 14:07
 */
public class SleepUtil {
    private SleepUtil() {
    }

    public static final void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}