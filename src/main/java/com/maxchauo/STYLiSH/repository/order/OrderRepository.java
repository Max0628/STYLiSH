package com.maxchauo.STYLiSH.repository.order;

import com.maxchauo.STYLiSH.dto.product.form.order.OrderDataForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderItemForm;

public interface OrderRepository {
  long insertOrder(OrderDataForm orderDataForm);

  boolean insertOrderItem(OrderItemForm orderItemForm, long orderId);

  boolean insertPaymentRecord(long orderId, int amount, String paymentMethod, int statusId);
}