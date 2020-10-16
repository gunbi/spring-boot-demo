package com.gunbi.springboot.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created on 2020/10/16.
 *
 * @author hanzhong
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisClientTest {

    private static final String TEST_KEY = "test_key";
    private static final String TEST_VALUE = "test_value";

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private RedisClient userRedisClient;

    @Test
    public void get() {
        redisClient.set(TEST_KEY, TEST_VALUE);
        String defaultTestValue = redisClient.get(TEST_KEY);
        Assert.assertEquals(TEST_VALUE, defaultTestValue);
        userRedisClient.set(TEST_KEY, TEST_VALUE);
        String userTestValue = userRedisClient.get(TEST_KEY);
        Assert.assertEquals(TEST_VALUE, userTestValue);
    }
}
