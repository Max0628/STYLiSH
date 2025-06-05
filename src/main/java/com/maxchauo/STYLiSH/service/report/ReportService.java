package com.maxchauo.STYLiSH.service.report;

import com.maxchauo.STYLiSH.dto.product.dto.report.UserPaymentReportDto;
import java.util.List;

public interface ReportService {
  List<UserPaymentReportDto> getUserTotalPayments();
  void enqueueOrdersToQueue(String jobId);
}