package com.maxchauo.STYLiSH.exception;

import com.maxchauo.STYLiSH.dto.product.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponseDto> handleBaseException(BaseException exception) {
    return buildErrorResponse(exception.getStatusCode(), exception.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception) {
	   exception.printStackTrace();
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "system error, please try again later");
  }

  private static ResponseEntity<ErrorResponseDto> buildErrorResponse(int statusCode, String message) {
    return ResponseEntity
            .status(statusCode)
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(new ErrorResponseDto(statusCode, message));
  }
}
