package com.maxchauo.STYLiSH.service.order;

import com.maxchauo.STYLiSH.dto.product.form.order.OrderDataForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderItemForm;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.repository.order.OrderRepository;
import com.maxchauo.STYLiSH.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OrderCreationService {
  private final  OrderValidator orderValidator;
  private final OrderRepository orderRepository;
  private final SecurityUtil securityUtil;

  public OrderCreationService(OrderValidator orderValidator, OrderRepository orderRepository, SecurityUtil securityUtil) {
    this.orderValidator = orderValidator;
    this.orderRepository = orderRepository;
    this.securityUtil = securityUtil;
  }

  long createPendingOrder (OrderForm orderForm) {
  OrderDataForm orderData = orderForm.getOrder();
  //prevent NPE
  if (orderForm == null || orderData == null) {
    throw new UserClientException("order form is null");
  }

  //get prime
  String prime = orderForm.getPrime();

  if (prime == null || prime.isBlank()) {
    throw new UserClientException("prime is null");
  }

  List<OrderItemForm> items = orderData.getOrderItems();

  if (items == null || items.isEmpty()) {
    throw new UserClientException("order items are null or empty");
  }

  OrderItemForm item = items.get(0);
  if (item == null) {
    throw new UserClientException("order item form is null");
  }

  //check order form validation
  orderValidator.validateOrderItem(item);

  orderData.setUserId(securityUtil.getCurrentUserId());
  long validateSubtotal = 0;
    validateSubtotal += (long) item.getUnitPrice() * item.getQuantity();

  long freight = 100;//default freight
    validateSubtotal += freight;
  if(orderData.getTotal()!= validateSubtotal) {
    throw new UserClientException("subtotal mismatch, please check your order");
  }

  orderData.setTotal((int) validateSubtotal);
  orderData.setStatusId(1); //set order unpaid

  //insert order data and get orderId
  long orderId = orderRepository.insertOrder(orderData);

  //get orderId and insert orderItem;
  boolean  insertOrderItemSuccess = orderRepository.insertOrderItem(item, orderId);

  //insert payment record
  boolean insertPaymentSuccess = orderRepository.insertPaymentRecord(
          orderId,
          orderData.getTotal(),                    // amount
          orderData.getPaymentMethod(),            // payment method
          1                                        // statusId，default to 1(unpaid)
  );
  if(orderId==0|| !insertOrderItemSuccess || !insertPaymentSuccess) {
    throw new UserClientException("fail to create order");
  }
    return orderId; // Placeholder return value
  }
}
