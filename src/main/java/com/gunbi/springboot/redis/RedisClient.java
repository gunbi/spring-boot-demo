package com.gunbi.springboot.redis;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Set;

/**
 * Created on 2020/10/16.
 *
 * @author hanzhong
 */
@Slf4j
public class RedisClient {

    private StringRedisTemplate redisTemplate;
    private boolean enableRedisCache = true;
    private String keyPrefix;

    public RedisClient() {
    }

    public RedisClient(StringRedisTemplate redisTemplate, String keyPrefix, boolean enableRedisCache) {
        this.keyPrefix = keyPrefix;
        this.enableRedisCache = enableRedisCache;
        this.redisTemplate = redisTemplate;
    }

    String get(String key) {
        if (!enableRedisCache) {
            return null;
        }
        Long startTimeInNanoTime = System.nanoTime();
        String ret = redisTemplate.opsForValue().get(keyPrefix + key);
        log.debug("get redis spend {} ms", (System.nanoTime() - startTimeInNanoTime) / 1000000);
        return ret;
    }

    void set(String key, String value) {
        if (!enableRedisCache) {
            return;
        }
        Long startTimeInNanoTime = System.nanoTime();
        redisTemplate.opsForValue().set(keyPrefix + key, value);
        log.debug("set redis spend {} ms", (System.nanoTime() - startTimeInNanoTime) / 1000000);
    }

    Long increment(String key, Long value) {
        if (!enableRedisCache) {
            return null;
        }
        return redisTemplate.opsForValue().increment(keyPrefix + key, value);
    }

    Set getMembers(String key) {
        if (!enableRedisCache) {
            return null;
        }
        return redisTemplate.opsForSet().members(keyPrefix + key);
    }

    boolean isMember(String key, Object value) {
        if (!enableRedisCache) {
            return false;
        }
        return redisTemplate.opsForSet().isMember(keyPrefix + key, value);
    }

    Long addMember(String key, String value) {
        if (!enableRedisCache) {
            return 0L;
        }
        return redisTemplate.opsForSet().add(keyPrefix + key, value);
    }

    void delete(String key) {
        if (!enableRedisCache) {
            return;
        }
        redisTemplate.delete(keyPrefix + key);
    }


    void hSet(String cacheKey, String key, String values) {
        if (!enableRedisCache) {
            return;
        }
        Long startTime = System.currentTimeMillis();
        redisTemplate.opsForHash().put(keyPrefix + cacheKey, key, values);
        log.debug("set redis hash, cache key:{}, key: {}, spend {} ms", cacheKey, key, System.currentTimeMillis() - startTime);
    }

    Map hmGet(String cacheKey) {
        if (!enableRedisCache) {
            return Maps.newHashMap();
        }
        Long startTime = System.currentTimeMillis();
        Map values = redisTemplate.opsForHash().entries(keyPrefix + cacheKey);
        log.debug("get redis hash, cache key:{}, spend {} ms", cacheKey, System.currentTimeMillis() - startTime);
        return values;
    }

    void setEnableRedisCache(boolean enableRedisCache) {
        this.enableRedisCache = enableRedisCache;
    }

}
