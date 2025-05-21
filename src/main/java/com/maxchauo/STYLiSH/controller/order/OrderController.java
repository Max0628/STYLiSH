package com.maxchauo.STYLiSH.controller.order;

import com.maxchauo.STYLiSH.dto.product.dto.order.OrderResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderForm;
import com.maxchauo.STYLiSH.service.order.OrderFacadeService;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
  private final OrderFacadeService orderFacadeService;

  public OrderController(OrderFacadeService orderFacadeService) {
    this.orderFacadeService = orderFacadeService;
  }


  @PostMapping("/checkout")
  public ResponseEntity<DataWrapper<OrderResponseDto>> createOrder(@RequestBody OrderForm orderForm) {
    DataWrapper<OrderResponseDto> response = orderFacadeService.createOrder(orderForm);
    return ResponseEntity.ok().body(response);
  }
}
