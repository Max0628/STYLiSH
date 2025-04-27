package com.maxchauo.STYLiSH.exception;

public class MissingTokenException extends BaseException{
  public MissingTokenException(String message) {
    super(message, 401);
  }
}
