package com.maxchauo.STYLiSH.repository.order;

import com.maxchauo.STYLiSH.dto.product.form.order.OrderDataForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderItemForm;

import java.time.LocalDateTime;

public interface OrderRepository {
  long insertOrder(OrderDataForm orderDataForm);

  boolean insertOrderItem(OrderItemForm orderItemForm, long orderId);

  boolean insertPaymentRecord(long orderId, int amount, String paymentMethod, int statusId);

  boolean updateOrderStatus(long orderId, int statusId);

  boolean updatePaymentRecord(long orderId, String transactionId, int statusId, LocalDateTime paymentTime);
}