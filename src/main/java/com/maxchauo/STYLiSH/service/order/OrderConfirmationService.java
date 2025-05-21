package com.maxchauo.STYLiSH.service.order;

import com.maxchauo.STYLiSH.repository.order.OrderRepository;
import com.maxchauo.STYLiSH.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class OrderConfirmationService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  public OrderConfirmationService(OrderRepository orderRepository, ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
  }

  @Transactional
  public boolean confirmOrderPayment(long orderId, String transactionId) {

    // update order status
    boolean updatedOrder = orderRepository.updateOrderStatus(orderId, 2);
    if (!updatedOrder) {
      throw new IllegalStateException("failed to update order status");
    }

    // update payment record
    boolean updatedPayment = orderRepository.updatePaymentRecord(
            orderId,
            transactionId,
            2, // status_id = 2 status paid
            LocalDateTime.now()
    );
    if (!updatedPayment) {
      throw new IllegalStateException("failed to update payment record");
    }

    // minus product stock
    boolean reduced = productRepository.reduceStockByOrderId(orderId);
    if (!reduced) {
      throw new IllegalStateException("failed to reduce stock");
    }
    return true;
  }
}
