package com.maxchauo.STYLiSH.exception;

public class UserServerException extends BaseException {
    public UserServerException(String message) {
        super(message, 400);
    }
}