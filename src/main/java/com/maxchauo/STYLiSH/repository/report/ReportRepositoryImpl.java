package com.maxchauo.STYLiSH.repository.report;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Log4j2
@Repository
public class ReportRepositoryImpl implements ReportRepository {

  private final NamedParameterJdbcTemplate template;

  public ReportRepositoryImpl(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  @Override
  public List<Map<String, Object>> fetchUserOrders() {
    String sql = "SELECT userId, total FROM OrderReport";
    try{
      return template.queryForList(sql, Map.of());
    }catch (Exception e) {
      log.warn("Error fetching user orders: " + e.getMessage());
      return List.of(); // Return an empty list in case of error
    }
  }

  @Override
  public List<Map<String, Object>> getOrdersToEnqueue() {
    String sql = "SELECT id, user_id, total FROM OrderReport WHERE status = 'NEW'";
    try{
      return template.queryForList(sql, new MapSqlParameterSource());
    }catch (Exception e){
      log.warn("Error fetching orders to enqueue");
      return List.of(); // Return an empty list in case of error
    }
  }

  @Override
  public void updateOrderStatus(Long orderId, String status) {
    String sql = "UPDATE OrderReport SET status = :status WHERE id = :orderId";
    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("status", status)
            .addValue("orderId", orderId);
    try{
      template.update(sql, params);
    } catch (Exception e) {
      log.warn("Failed to update order status for orderId");
      throw new RuntimeException(e);
    }
  }
}
