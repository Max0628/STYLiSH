package com.maxchauo.STYLiSH.exception;

public class DatabaseOperationException extends BaseException {
  public DatabaseOperationException(String message) {
    super(message, 500);
  }
}
