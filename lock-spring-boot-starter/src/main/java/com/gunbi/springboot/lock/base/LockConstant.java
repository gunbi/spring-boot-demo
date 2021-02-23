package com.gunbi.springboot.lock.base;

/**
 * Created on 2021/2/9.
 *
 * @author hanzhong
 */
public class LockConstant {

    public static final String LOCK_FILED_SPLIT = ",";

    public static final String LOCK_KEY_JOINER = "_";

    public static final String DEFAULT_LOCK_IMPL = "com.didi.apollo.lock.impl.ZkDistributorLockHelper";
}