package com.maxchauo.STYLiSH.controller.report;

import com.maxchauo.STYLiSH.dto.product.dto.report.UserPaymentReportDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping(" /report/payments")
  public ResponseEntity<DataWrapper<List<UserPaymentReportDto>>> getPaymentReport() {
    List<UserPaymentReportDto> result = reportService.getUserTotalPayments();
    return ResponseEntity.ok(new DataWrapper<>(result));
  }

  @GetMapping("/2.0/report/payments")
  public ResponseEntity<Map<String, Object>> triggerPaymentReport() {
    String jobId = UUID.randomUUID().toString();
    reportService.enqueueOrdersToQueue(jobId);
    return ResponseEntity.accepted().body(Map.of(
            "jobId", jobId,
            "status", "PROCESSING",
            "message", "Job accepted and being processed"
    ));
  }
}
