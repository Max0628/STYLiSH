package com.maxchauo.STYLiSH.repository.report;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
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
    String sql = "SELECT user_id, total FROM Orders";
    try{
      List<Map<String, Object>> result = template.queryForList(sql, Map.of());
      return result;
    }catch (Exception e) {
      log.warn("Error fetching user orders: " + e.getMessage());
      return List.of(); // Return an empty list in case of error
    }
  }
}
