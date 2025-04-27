package com.maxchauo.STYLiSH.exception;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(String message) {
        super(message, 403);
    }
}