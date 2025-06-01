package com.maxchauo.STYLiSH.controller.report;

import com.maxchauo.STYLiSH.dto.product.dto.report.UserPaymentReportDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/1.0/report")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping("/payments")
  public ResponseEntity<DataWrapper<List<UserPaymentReportDto>>> getPaymentReport() {
    List<UserPaymentReportDto> result = reportService.getUserTotalPayments();
    return ResponseEntity.ok(new DataWrapper<>(result));
  }
}
