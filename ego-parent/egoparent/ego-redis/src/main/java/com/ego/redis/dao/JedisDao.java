package com.ego.redis.dao;

public interface JedisDao {
    /**
     * 判断key是否存在
     */
    boolean exists(String key);

    /**
     * 删除key
     */
    Long del(String key);

    /**
     * 设置或覆盖key
     */
    String set(String key, String value);

    /**
     * 获取key
     */
    String get(String key);

    /**
     * 设置redis过期时间
     */
    Long expire(String key, int seconds);
}
