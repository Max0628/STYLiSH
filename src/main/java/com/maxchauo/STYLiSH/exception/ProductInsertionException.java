package com.maxchauo.STYLiSH.exception;

public class ProductInsertionException extends BaseException{
  public ProductInsertionException(String message) {
    super(message, 400);
  }
}
