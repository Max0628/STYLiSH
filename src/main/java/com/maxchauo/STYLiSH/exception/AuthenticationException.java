package com.maxchauo.STYLiSH.exception;

public class AuthenticationException extends BaseException {
  public AuthenticationException(String message) {
    super(message, 401);
  }
}