package com.maxchauo.STYLiSH.exception;

public class SystemException extends BaseException {
  public SystemException(String message) {
    super(message, 500);
  }
}
