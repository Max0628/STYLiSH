package com.maxchauo.STYLiSH.service.order;

import com.maxchauo.STYLiSH.dto.product.dto.order.OrderResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderForm;

public interface OrderService {
  DataWrapper<OrderResponseDto> createOrder(OrderForm orderForm);
}
