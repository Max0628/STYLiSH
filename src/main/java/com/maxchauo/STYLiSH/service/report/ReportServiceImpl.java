package com.maxchauo.STYLiSH.service.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxchauo.STYLiSH.dto.product.dto.report.UserPaymentReportDto;
import com.maxchauo.STYLiSH.repository.report.ReportRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
@Log4j2
@Service
public class ReportServiceImpl implements ReportService {

  private final ReportRepository reportRepository;
  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

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

  @Override
  public void enqueueOrdersToQueue(String jobId) {
    List<Map<String, Object>> orderList = reportRepository.getOrdersToEnqueue();

    for (Map<String, Object> order : orderList) {
      try {
        Map<String, Object> payload = new HashMap<>();
        payload.put("jobId", jobId);
        payload.put("orderId", order.get("id"));
        payload.put("userId", order.get("user_id"));
        payload.put("total", order.get("total"));
        String json = objectMapper.writeValueAsString(payload);
        redisTemplate.opsForList().rightPush("order_jobs_queue", json);
        reportRepository.updateOrderStatus((Long) order.get("id"), "QUEUED");
      } catch (Exception e) {
        e.printStackTrace();
        log.error("Failed to enqueue order {}", order.get("id"), e);
      }
    }
  }
}