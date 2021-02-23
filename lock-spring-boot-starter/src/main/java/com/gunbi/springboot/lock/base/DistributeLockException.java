package com.gunbi.springboot.lock.base;

/**
 * Created on 2021/2/9.
 *
 * @author hanzhong
 */
public class DistributeLockException extends Exception {

    private String message;

    public DistributeLockException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}