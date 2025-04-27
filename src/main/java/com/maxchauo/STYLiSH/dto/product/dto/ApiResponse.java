package com.maxchauo.STYLiSH.dto.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
  private String statusCode;
  private String message;
  private Object data;
}
