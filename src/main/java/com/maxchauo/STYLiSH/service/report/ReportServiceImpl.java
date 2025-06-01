package com.maxchauo.STYLiSH.service.report;

import com.maxchauo.STYLiSH.dto.product.dto.report.UserPaymentReportDto;
import com.maxchauo.STYLiSH.repository.report.ReportRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
@Log4j2
@Service
public class ReportServiceImpl implements ReportService {

  private final ReportRepository reportRepository;

  public ReportServiceImpl(ReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }

  @Override
  public List<UserPaymentReportDto> getUserTotalPayments() {
  try{
    List<Map<String, Object>> rows = reportRepository.fetchUserOrders();
    Map<Long, Integer> totalMap = new HashMap<>();

    for (Map<String, Object> row : rows) {
      Long userId = ((Number) row.get("user_id")).longValue();
      Integer total = ((Number) row.get("total")).intValue();
      totalMap.put(userId, totalMap.getOrDefault(userId, 0) + total);
    }

    List<UserPaymentReportDto> result = new ArrayList<>();
    for (Map.Entry<Long, Integer> entry : totalMap.entrySet()) {
      result.add(new UserPaymentReportDto(entry.getKey(), entry.getValue()));
    }
    return result;
  }catch (Exception e){
    log.warn("Error fetching user total payments: " + e.getMessage());
  }
  return null;
  }
}