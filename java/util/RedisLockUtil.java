package com.post.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * @author Barkley.Chang
 * @className:RedisLockUtil
 * @description: TODO
 * @date 2020-04-24 10:16
 */
public final class RedisLockUtil {
    private RedisLockUtil() {
    }

    private static Log log = LogFactory.getLog(LockServiceRedisImpl.class);

    private static String SET_SUCCESS = "OK";

    private static String KEY_PRE = "REDIS_LOCK_";

    private DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private RedisConnection redisConnection;

    private Integer dbIndex;

    private Integer lockExpirseTime;

    private Integer tryExpirseTime;

    public void setRedisConnection(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void setDbIndex(Integer dbIndex) {
        this.dbIndex = dbIndex;
    }

    public void setLockExpirseTime(Integer lockExpirseTime) {
        this.lockExpirseTime = lockExpirseTime;
    }

    public void setTryExpirseTime(Integer tryExpirseTime) {
        this.tryExpirseTime = tryExpirseTime;
    }

    public String lock(String key) {
        Jedis jedis = null;
        try {
            jedis = redisConnection.getJedis();
            jedis.select(dbIndex);
            key = KEY_PRE + key;
            String value = fetchLockValue();
            if (SET_SUCCESS.equals(jedis.set(key, value, "NX", "EX", lockExpirseTime))) {
                log.debug("Reids Lock key : " + key + ",value : " + value);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public String tryLock(String key) {
        Jedis jedis = null;
        try {
            jedis = redisConnection.getJedis();
            jedis.select(dbIndex);
            key = KEY_PRE + key;
            String value = fetchLockValue();
            Long firstTryTime = new Date().getTime();
            do {
                if (SET_SUCCESS.equals(jedis.set(key, value, "NX", "EX", lockExpirseTime))) {
                    log.debug("Reids Lock key : " + key + ",value : " + value);
                    return value;
                }
                log.info("Redis lock failure,waiting try next");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ((new Date().getTime() - tryExpirseTime * 1000) < firstTryTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public boolean unLock(String key, String value) {
        Long RELEASE_SUCCESS = 1L;
        Jedis jedis = null;
        try {
            jedis = redisConnection.getJedis();
            jedis.select(dbIndex);
            key = KEY_PRE + key;
            String command = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            if (RELEASE_SUCCESS.equals(jedis.eval(command, Collections.singletonList(key), Collections.singletonList(value)))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 生成加锁的唯一字符串
     *
     * @return 唯一字符串
     */
    private String fetchLockValue() {
        return UUID.randomUUID().toString() + "_" + df.format(new Date());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 9; i++) {
            new Thread(new Runnable() {
                public void run() {
                    RedisConnection redisConnection = RedisConnectionUtil.create();
                    LockServiceRedisImpl lockServiceRedis = new LockServiceRedisImpl();
                    lockServiceRedis.setRedisConnection(redisConnection);
                    lockServiceRedis.setDbIndex(15);
                    lockServiceRedis.setLockExpirseTime(20);
                    String key = "20171228";
                    String value = lockServiceRedis.lock(key);
                    try {
                        if (value != null) {
                            System.out.println(Thread.currentThread().getName() + " lock key = " + key + " success! ");
                            Thread.sleep(25 * 1000);
                        } else {
                            System.out.println(Thread.currentThread().getName() + " lock key = " + key + " failure! ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (value == null) {
                            value = "";
                        }
                        System.out.println(Thread.currentThread().getName() + " unlock key = " + key + " " + lockServiceRedis.unLock(key, value));
                    }
                }
            }).start();
        }
    }
}