package com.maxchauo.STYLiSH.exception;

public class AuthorizationException extends BaseException {
  public AuthorizationException(String message) {
    super(message, 403);
  }
}