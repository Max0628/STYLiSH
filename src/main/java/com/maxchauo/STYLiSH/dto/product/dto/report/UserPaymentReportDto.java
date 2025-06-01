package com.maxchauo.STYLiSH.dto.product.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPaymentReportDto {
  private Long userId;
  private Integer totalPayment;
}