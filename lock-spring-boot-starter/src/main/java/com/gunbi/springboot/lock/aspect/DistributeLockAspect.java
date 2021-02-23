package com.gunbi.springboot.lock.aspect;

import com.google.common.collect.Lists;
import com.gunbi.springboot.lock.LockHelper;
import com.gunbi.springboot.lock.annotation.DistributeLock;
import com.gunbi.springboot.lock.annotation.LockField;
import com.gunbi.springboot.lock.base.DistributeLockException;
import com.gunbi.springboot.lock.base.LockConstant;
import com.gunbi.springboot.lock.configuration.DistributeLockProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2021/2/7.
 *
 * @author hanzhong
 */
@Aspect
public class DistributeLockAspect {
    private static Logger logger = LoggerFactory.getLogger(DistributeLockAspect.class);

    private final DistributeLockProperties distributeLockProperties;

    private LockHelper lockHelper;

    public DistributeLockAspect(DistributeLockProperties distributeLockProperties, List<LockHelper> lockHelpers) throws DistributeLockException {
        this.lockHelper = getLockHelper(distributeLockProperties, lockHelpers);
        this.distributeLockProperties = distributeLockProperties;
    }

    private LockHelper getLockHelper(DistributeLockProperties distributeLockProperties, List<LockHelper> lockHelpers) throws DistributeLockException {
        LockHelper lockHelperImpl = null;
        String lockClass = LockConstant.DEFAULT_LOCK_IMPL;
        if (StringUtils.isNotBlank(distributeLockProperties.getLockHelperClass())) {
            lockClass = distributeLockProperties.getLockHelperClass();
        }
        for (LockHelper lockHelper : lockHelpers) {
            if (lockHelper.getClass().getName().equals(lockClass)) {
                lockHelperImpl = lockHelper;
            }
        }
        if (lockHelperImpl == null) {
            throw new DistributeLockException("can not find lock helper impl");
        }
        return lockHelperImpl;
    }

    @Around(value = "@annotation(distributeLock)")
    public Object doAround(ProceedingJoinPoint joinPoint, DistributeLock distributeLock) throws Throwable {
        if (!distributeLockProperties.isEnable()) {
            return joinPoint.proceed();
        }
        List<String> lockKeys = Lists.newArrayList();
        try {
            lockKeys = getDistributeLockKey(joinPoint);
            logger.debug("[distributeLock]: lock key: {}, use lock helper: {}.", lockKeys, distributeLockProperties.getLockHelperClass());
        } catch (Exception e) {
            logger.error("can not get lockKeys, error: ", e);
        }

        lock(distributeLock, lockKeys);

        try {
            return joinPoint.proceed();
        } finally {
            unLock(lockKeys);
        }
    }

    private void lock(DistributeLock distributeLock, List<String> lockKeys) throws DistributeLockException {
        List<String> successLockKeys = Lists.newArrayList();
        for (String lockKey : lockKeys) {
            boolean tryLock = true;
            try {
                tryLock = lockHelper.lock(lockKey, distributeLock.timeout(), distributeLock.timeUnit());
            } catch (Exception e) {
                logger.error("try get lock error, e", e);
            }
            if (!tryLock) {
                logger.debug("try get lock failed, key: {}, timeout: {}.", lockKey, distributeLock.timeout());
                unLock(successLockKeys);
                throw new DistributeLockException("try get lock failed, please try again later.");
            }
            logger.debug("get lock success, lock key : {}", lockKey);
            successLockKeys.add(lockKey);
        }
    }

    private void unLock(List<String> unLockKeys) {
        if (CollectionUtils.isNotEmpty(unLockKeys)) {
            logger.debug("unLock keys: {}.", unLockKeys);
            unLockKeys.forEach(lockHelper::unlock);
        }
    }

    private List<String> getDistributeLockKey(ProceedingJoinPoint joinPoint) {
        List<String> lockKeys = Lists.newArrayList();
        Object[] methodArgs = joinPoint.getArgs();
        if (methodArgs.length == 0) {
            return lockKeys;
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        List<String> multiParams = Lists.newArrayList();
        List<String> params = Lists.newArrayList();
        for (int i = 0; i < parameters.length; i++) {
            LockField lockField = parameters[i].getAnnotation(LockField.class);
            if (lockField == null) {
                continue;
            }
            if (lockField.isMulti()) {
                multiParams = Lists.newArrayList(methodArgs[i].toString().split(lockField.split()));
            } else {
                params.add(methodArgs[i].toString());
            }
        }
        return getLockKeys(params, multiParams);
    }

    private List<String> getLockKeys(List<String> params, List<String> multiParams) {
        if (CollectionUtils.isEmpty(params) && CollectionUtils.isEmpty(multiParams)) {
            return Lists.newArrayList();
        }
        String lockKey = distributeLockProperties.getLockPrefix();
        if (CollectionUtils.isNotEmpty(params)) {
            lockKey = lockKey + LockConstant.LOCK_KEY_JOINER + String.join(LockConstant.LOCK_KEY_JOINER, params);
        }
        if (CollectionUtils.isNotEmpty(multiParams)) {
            String finalLockKey = lockKey;
            return multiParams.stream().map(multiParam -> finalLockKey + LockConstant.LOCK_KEY_JOINER + multiParam).collect(Collectors.toList());
        } else {
            return Lists.newArrayList(lockKey);
        }
    }
}