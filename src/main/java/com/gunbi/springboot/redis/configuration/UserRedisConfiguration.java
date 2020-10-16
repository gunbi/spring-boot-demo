package com.gunbi.springboot.redis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2020/10/15.
 *
 * @author hanzhong
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis.user")
public class UserRedisConfiguration extends RedisConfiguration {
}
