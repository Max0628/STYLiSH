package com.maxchauo.STYLiSH.repository.order;

import com.maxchauo.STYLiSH.dto.product.form.order.OrderDataForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderItemForm;
import com.maxchauo.STYLiSH.repository.product.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Log4j2
@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final NamedParameterJdbcTemplate template;

  public OrderRepositoryImpl(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  @Override
  public long insertOrder(OrderDataForm form) {
    String sql =
        """
        INSERT INTO Orders (
            user_id,
            shipping_method,
            payment_method,
            subtotal,
            freight,
            total,
            status_id,
            recipient_name,
            recipient_phone,
            recipient_email,
            recipient_address,
            recipient_time
        ) VALUES (
            :userId,
            :shippingMethod,
            :paymentMethod,
            :subtotal,
            :freight,
            :total,
            :statusId,
            :recipientName,
            :recipientPhone,
            :recipientEmail,
            :recipientAddress,
            :recipientTime
        )
    """;

    MapSqlParameterSource params =
        new MapSqlParameterSource()
            .addValue("userId", form.getUserId())
            .addValue("shippingMethod", form.getShippingMethod())
            .addValue("paymentMethod", form.getPaymentMethod())
            .addValue("subtotal", form.getSubtotal())
            .addValue("freight", form.getFreight())
            .addValue("total", form.getTotal())
            .addValue("statusId", form.getStatusId())
            .addValue("recipientName", form.getRecipient().getName())
            .addValue("recipientPhone", form.getRecipient().getPhone())
            .addValue("recipientEmail", form.getRecipient().getEmail())
            .addValue("recipientAddress", form.getRecipient().getAddress())
            .addValue("recipientTime", form.getRecipient().getTime());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    try {
      template.update(sql, params, keyHolder, new String[] {"id"});
      return keyHolder.getKey().longValue();
    } catch (DataAccessException e) {
      e.printStackTrace();
      throw new DataAccessException("Insert Order failed", e) {};
    }
  }

  @Override
  public boolean insertOrderItem(OrderItemForm item, long orderId) {
    String sql = """
        INSERT INTO OrderItem (
            order_id,
            product_id,
            product_title_snapshot,
            unit_price,
            color_code_snapshot,
            color_name_snapshot,
            size_snapshot,
            quantity
        ) VALUES (
            :orderId,
            :productId,
            :productTitle,
            :unitPrice,
            :colorCode,
            :colorName,
            :size,
            :quantity
        )
    """;

    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("orderId", orderId)
            .addValue("productId", item.getProductId())
            .addValue("productTitle", item.getProductTitleSnapshot())
            .addValue("unitPrice", item.getUnitPrice())
            .addValue("colorCode", item.getColor().getCode())
            .addValue("colorName", item.getColor().getName())
            .addValue("size", item.getSize())
            .addValue("quantity", item.getQuantity());

    int rows = template.update(sql, params);
    return rows > 0;
  }


  @Override
  public boolean insertPaymentRecord(long orderId, int amount, String paymentMethod, int statusId) {
    String sql = """
        INSERT INTO Payment (
            order_id,
            amount,
            payment_method,
            status_id
        ) VALUES (
            :orderId,
            :amount,
            :paymentMethod,
            :statusId
        )
    """;

    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("orderId", orderId)
            .addValue("amount", amount)
            .addValue("paymentMethod", paymentMethod)
            .addValue("statusId", statusId);

    int rows = template.update(sql, params);
    return rows > 0;
  }

  @Override
  public boolean updateOrderStatus(long orderId, int statusId) {
    String sql = "UPDATE Orders SET status_id = :statusId WHERE id = :orderId";

    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("statusId", statusId)
            .addValue("orderId", orderId);

    return template.update(sql, params) == 1;
  }

  @Override
  public boolean updatePaymentRecord(long orderId, String transactionId, int statusId, LocalDateTime paymentTime) {
    String sql = "UPDATE Payment SET transaction_id = :txnId, status_id = :statusId, payment_time = :paymentTime WHERE order_id = :orderId";

    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("txnId", transactionId)
            .addValue("statusId", statusId)
            .addValue("paymentTime", paymentTime)
            .addValue("orderId", orderId);

    return template.update(sql, params) == 1;
  }
}
