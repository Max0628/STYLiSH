package com.maxchauo.STYLiSH.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/** 全局 exception 處理器 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 處理資料庫相關異常(500)
   */
  @ExceptionHandler(DatabaseOperationException.class)
  public ResponseEntity<Map<String, Object>> handleDatabaseException(DatabaseOperationException exception) {
    return new ResponseEntity<>(
        buildErrorResponse(
        exception.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * 處理資料完整性異常，可能是客戶從前端傳過來的資料有問題才報錯，回400
   */
  @ExceptionHandler({DataIntegrityException.class, ProductInsertionException.class})
  public ResponseEntity<Map<String,Object>> handleBadRequestException(RuntimeException exception) {
    return new ResponseEntity<>(
        buildErrorResponse(
        exception.getMessage(),
        HttpStatus.BAD_REQUEST),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * 處理系統性問題，回500
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleSystemException(Exception exception) {
    return new ResponseEntity<>(
        buildErrorResponse(
        "系統發生錯誤，請稍後再試",
        HttpStatus.INTERNAL_SERVER_ERROR),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * 傳入錯誤訊息與 HTTP 狀態碼，回傳 JSON
   * @param message
   * @param status
   * @return
   */
  private Map<String, Object> buildErrorResponse(String message, HttpStatus status) {
    return Map.of("status", status.value(), "error", message);
  }
}
