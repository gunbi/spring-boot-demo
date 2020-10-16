package com.gunbi.springboot.redis.configuration;

import lombok.Data;

/**
 * Created on 2020/10/16.
 *
 * @author hanzhong
 */
@Data
public class RedisConfiguration {

    private boolean enable;

    private String host;

    private int port;

    private int timeout;

}
