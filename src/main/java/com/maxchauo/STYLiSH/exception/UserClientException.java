package com.maxchauo.STYLiSH.exception;

public class UserClientException extends BaseException {
    public UserClientException(String message) {
        super(message, 400);
    }
}