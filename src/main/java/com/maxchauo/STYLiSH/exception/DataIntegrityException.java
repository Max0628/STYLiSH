package com.maxchauo.STYLiSH.exception;

public class DataIntegrityException extends BaseException {
  public DataIntegrityException(String message) {
    super(message, 400);
  }
}
