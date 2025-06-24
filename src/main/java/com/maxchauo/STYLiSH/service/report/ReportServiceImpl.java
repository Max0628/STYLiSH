package com.maxchauo.STYLiSH.service.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxchauo.STYLiSH.dto.product.dto.PaymentReportJob;
import com.maxchauo.STYLiSH.dto.product.dto.report.UserPaymentReportDto;
import com.maxchauo.STYLiSH.repository.report.ReportRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
@Log4j2
@Service
public class ReportServiceImpl implements ReportService {
  @Value("${queue.payment.report.key}")
  private String jobQueueKey;

  private final ReportRepository reportRepository;
  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;
  private static final String JOB_QUEUE_KEY = "payment_report_job_queue";

  public ReportServiceImpl(ReportRepository reportRepository, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
    this.reportRepository = reportRepository;
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<UserPaymentReportDto> getUserTotalPayments() {
  try{
    List<Map<String, Object>> rows = reportRepository.fetchUserOrders();
    Map<Long, Integer> totalMap = new HashMap<>();

    for (Map<String, Object> row : rows) {
      Long userId = ((Number) row.get("userId")).longValue();
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

  @Override
  public void enqueueOrdersToQueue(String jobId) {

    try {
      PaymentReportJob job = new PaymentReportJob(jobId, "PAYMENT_REPORT", 0);
      String jobJson = objectMapper.writeValueAsString(job);
      redisTemplate.opsForList().rightPush(jobQueueKey, jobJson);
    } catch (Exception e) {
      throw new RuntimeException("Failed to enqueue job: " + e.getMessage(), e);
    }
  }
}