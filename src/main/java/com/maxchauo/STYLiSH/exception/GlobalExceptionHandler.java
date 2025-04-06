package com.maxchauo.STYLiSH.exception;

import com.maxchauo.STYLiSH.dto.product.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** 全局 exception 處理器 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 處理資料庫相關異常(500)
   */
  @ExceptionHandler(DatabaseOperationException.class)
  public ResponseEntity<ErrorResponseDto> handleDatabaseException(DatabaseOperationException exception) {
  return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,exception.getMessage());
  }

  /**
   * 處理資料完整性異常，可能是客戶從前端傳過來的資料有問題才報錯，回400
   */
  @ExceptionHandler({DataIntegrityException.class, ProductInsertionException.class})
  public ResponseEntity<ErrorResponseDto> handleBadRequestException(RuntimeException exception) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
  }

  /**
   * 處理系統性問題，回500
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleSystemException(Exception exception, HttpServletRequest request) throws Exception {

    //swagger return default response
    String uri = request.getRequestURI();
    if (uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui")) {
      throw exception;
    }
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,"系統發生錯誤，請稍後再試");
  }

  public static ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String message){
  return ResponseEntity
          .status(status)
          .body(new ErrorResponseDto(status.value(), message));
  }
}
