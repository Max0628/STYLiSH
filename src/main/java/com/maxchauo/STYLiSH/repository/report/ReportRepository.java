package com.maxchauo.STYLiSH.repository.report;

import java.util.List;
import java.util.Map;

public interface ReportRepository {
  List<Map<String, Object>> fetchUserOrders();
  List<Map<String, Object>> getOrdersToEnqueue();
  void updateOrderStatus(Long orderId, String status);
}