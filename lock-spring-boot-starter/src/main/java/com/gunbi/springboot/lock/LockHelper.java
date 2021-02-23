package com.gunbi.springboot.lock;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2021/2/7.
 *
 * @author hanzhong
 */
public interface LockHelper {

    boolean lock(String lockName) throws Exception;

    boolean lock(String lockName, long time, TimeUnit unit) throws Exception;

    boolean tryLock(String lockName, long time, TimeUnit unit);

    boolean tryLock(String lockName);

    boolean isLocked(String lockName);

    void unlock(String lockName);
}