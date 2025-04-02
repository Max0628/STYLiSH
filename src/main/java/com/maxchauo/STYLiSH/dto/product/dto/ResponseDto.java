package com.maxchauo.STYLiSH.dto.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
  private String statusCode;
  private String message;
  private Object data;
}
