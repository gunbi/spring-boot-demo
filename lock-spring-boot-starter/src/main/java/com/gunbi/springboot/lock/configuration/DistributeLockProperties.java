package com.gunbi.springboot.lock.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2021/2/7.
 *
 * @author hanzhong
 */
@ConfigurationProperties(prefix = "gunbi.lock")
public class DistributeLockProperties {

    private boolean enable;

    private String lockPrefix;

    private String lockHelperClass;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLockHelperClass() {
        return lockHelperClass;
    }

    public void setLockHelperClass(String lockHelperClass) {
        this.lockHelperClass = lockHelperClass;
    }

    public String getLockPrefix() {
        return lockPrefix;
    }

    public void setLockPrefix(String lockPrefix) {
        this.lockPrefix = lockPrefix;
    }
}