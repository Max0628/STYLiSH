package com.maxchauo.STYLiSH.exception;

public class JwtTokenInvalidException extends BaseException{
  public JwtTokenInvalidException(String message) {
    super(message, 403);
  }
}
