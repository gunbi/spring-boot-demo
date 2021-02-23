package com.gunbi.springboot.lock.annotation;

import com.didi.apollo.lock.base.LockConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2021/2/8.
 *
 * @author hanzhong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LockField {

    boolean isMulti() default false;

    String split() default LockConstant.LOCK_FILED_SPLIT;
}