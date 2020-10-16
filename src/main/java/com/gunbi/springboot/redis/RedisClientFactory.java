package com.gunbi.springboot.redis;

import com.gunbi.springboot.redis.configuration.RedisConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * Created on 2020/10/16.
 *
 * @author hanzhong
 */
@Component
public class RedisClientFactory {

    private static final String DEFAULT_KEY_PREFIX = "DEFAULT_";
    private static final String USER_KEY_PREFIX = "USER_";

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.pool.max-wait}")
    private long maxWait;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Resource(name = "defaultRedisConfiguration")
    private RedisConfiguration defaultRedisConfiguration;

    @Resource(name = "userRedisConfiguration")
    private RedisConfiguration userRedisConfiguration;

    @Bean(name = "redisClient")
    public RedisClient defaultRedisClient() {
        StringRedisTemplate redisTemplate = stringRedisTemplate(connectionFactory(defaultRedisConfiguration));
        return new RedisClient(redisTemplate, DEFAULT_KEY_PREFIX, defaultRedisConfiguration.isEnable());
    }

    @Bean(name = "userRedisClient")
    public RedisClient userRedisClient() {
        StringRedisTemplate redisTemplate = stringRedisTemplate(connectionFactory(userRedisConfiguration));
        return new RedisClient(redisTemplate, USER_KEY_PREFIX, userRedisConfiguration.isEnable());
    }

    private StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private RedisConnectionFactory connectionFactory(RedisConfiguration redisConfiguration) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisConfiguration.getHost());
        jedisConnectionFactory.setPort(redisConfiguration.getPort());
        jedisConnectionFactory.setTimeout(redisConfiguration.getTimeout());
        jedisConnectionFactory.setPoolConfig(poolConfig());
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    private JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setMinIdle(minIdle);
        return poolConfig;
    }
}
