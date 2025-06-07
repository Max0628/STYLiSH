package com.maxchauo.STYLiSH.dto.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReportJob {
  private String jobId;
  private String jobType = "PAYMENT_REPORT";
  private int retryCount = 0;
}
