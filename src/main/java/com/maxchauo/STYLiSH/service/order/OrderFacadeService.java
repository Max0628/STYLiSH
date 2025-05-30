package com.maxchauo.STYLiSH.service.order;

import com.maxchauo.STYLiSH.dto.product.dto.order.OrderResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.order.TapPayResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderDataForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderForm;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderItemForm;
import com.maxchauo.STYLiSH.exception.UserClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
@Log4j2
@Service
public class OrderFacadeService {
  private final OrderCreationService orderCreationService;
  private final TapPayService tapPayService;
  private final OrderConfirmationService orderConfirmationService;

  public OrderFacadeService(OrderCreationService orderCreationService, TapPayService tapPayService, OrderConfirmationService orderConfirmationService) {
    this.orderCreationService = orderCreationService;
    this.tapPayService = tapPayService;
    this.orderConfirmationService = orderConfirmationService;
  }

  public DataWrapper<OrderResponseDto> createOrder(OrderForm orderForm) {
    long orderId = orderCreationService.createPendingOrder(orderForm);
    if(orderId<=0) {
      log.warn("order creation failed");
      throw new UserClientException("order creation failed");
    }
    OrderDataForm orderData = orderForm.getOrder();
    List<OrderItemForm> items = orderData.getOrderItems();
    OrderItemForm item = items.get(0);

    String detail = item.getProductTitleSnapshot();     //get product title
    String email = orderData.getRecipient().getEmail(); //get email
    String prime = orderForm.getPrime();                //get prime
    int amount = orderForm.getOrder().getTotal();       //get total amount
    TapPayResponseDto tapPayResponseDto;
    try {
      tapPayResponseDto = tapPayService.executePayment(prime, amount, detail, email);
    } catch (RestClientException e) {
      log.warn("TapPay connection failed: " + e.getMessage());
      throw new UserClientException("TapPay connection failed，please try again later");
    }

    // Null response
    if (tapPayResponseDto == null) {
      log.warn("TapPay response is null");
      throw new UserClientException("TapPay response is null");
    }

    if (tapPayResponseDto.getStatus() == 91) {
      log.warn("TapPay payment failed: " + tapPayResponseDto.getMsg());
      throw new UserClientException("TapPay payment failed: " + tapPayResponseDto.getMsg());
    }

    // status check
    if (tapPayResponseDto.getStatus() != 0) {
      log.warn("TapPay payment failed: " + tapPayResponseDto.getMsg());
      throw new UserClientException("TapPay payment failed: " + tapPayResponseDto.getMsg());
    }

    // recTradeId should not be null if success
    if (tapPayResponseDto.getRecTradeId() == null) {
      log.warn("TapPay response recTradeId is null");
      throw new UserClientException("TapPay response recTradeId is null");
    }

    orderConfirmationService.confirmOrderPayment(orderId, tapPayResponseDto.getRecTradeId());
    OrderResponseDto orderResponseDto = new OrderResponseDto(orderId);
    return new DataWrapper<OrderResponseDto>(orderResponseDto);
  }
}
