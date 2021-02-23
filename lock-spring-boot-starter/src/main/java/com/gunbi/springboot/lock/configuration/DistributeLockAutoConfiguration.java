package com.gunbi.springboot.lock.configuration;

import com.gunbi.springboot.lock.LockHelper;
import com.gunbi.springboot.lock.aspect.DistributeLockAspect;
import com.gunbi.springboot.lock.base.DistributeLockException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created on 2021/2/8.
 *
 * @author hanzhong
 */
@Configuration
@EnableConfigurationProperties(value = {DistributeLockProperties.class})
public class DistributeLockAutoConfiguration {

    @Resource
    private DistributeLockProperties distributeLockProperties;

    @Bean
    public DistributeLockAspect distributeLockAspect(List<LockHelper> lockHelpers) throws DistributeLockException {
        return new DistributeLockAspect(distributeLockProperties, lockHelpers);
    }
}