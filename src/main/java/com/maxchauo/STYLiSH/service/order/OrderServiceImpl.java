package com.maxchauo.STYLiSH.service.order;
import com.maxchauo.STYLiSH.dto.product.dto.order.OrderResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderDataForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderItemForm;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.repository.order.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Log4j2
@Service
public class OrderServiceImpl implements OrderService {
  private final NamedParameterJdbcTemplate template;
  private final OrderRepository orderRepository;
  private final OrderValidator orderValidator;

  public OrderServiceImpl(NamedParameterJdbcTemplate template, OrderRepository orderRepository, OrderValidator orderValidator) {
    this.template = template;
    this.orderRepository = orderRepository;
    this.orderValidator = orderValidator;
  }

  @Override
  @Transactional
  public DataWrapper<OrderResponseDto> createOrder(OrderForm orderForm) {

    //prevent NPE
    if (orderForm == null || orderForm.getOrder() == null) {
      throw new UserClientException("order form is null");
    }

    //get prime
    String prime = orderForm.getPrime();

    if (prime == null || prime.isBlank()) {
      throw new UserClientException("prime is null");
    }
    OrderDataForm orderData = orderForm.getOrder();

    if (orderData == null) {
      throw new UserClientException("order data form is null");
    }

    List<OrderItemForm> items = orderData.getOrderItems();

    if (items == null || items.isEmpty()) {
      throw new UserClientException("订单中没有商品");
    }

    OrderItemForm item = items.get(0);
    if (item == null) {
      throw new UserClientException("order item form is null");
    }

    //check order form validation
    boolean validataOrder = orderValidator.validateOrderItem(item);
    if(!validataOrder) {
      throw new UserClientException("order item is invalid");
    }

    orderData.setUserId(1L); // 硬编码临时值，实际应该从用户会话获取
    orderData.setStatusId(1); // 设置为"未付款"状态
    //insert order data and get orderId
    long orderId = orderRepository.insertOrder(orderData);
    //get orderId and insert orderItem;
    boolean  insertOrderItemSuccess = orderRepository.insertOrderItem(item, orderId);
    //insert payment record
    boolean insertPaymentSuccess = orderRepository.insertPaymentRecord(
            orderId,
            orderData.getTotal(),                    // amount
            orderData.getPaymentMethod(),            // payment method
            1                                        // statusId，預設未付款
    );

    if(orderId==0|| !insertOrderItemSuccess || !insertPaymentSuccess) {
      throw new UserClientException("fail to create order");
    }
    OrderResponseDto orderResponseDto = new OrderResponseDto(orderId);
    return new DataWrapper<OrderResponseDto>(orderResponseDto);
  }
}
