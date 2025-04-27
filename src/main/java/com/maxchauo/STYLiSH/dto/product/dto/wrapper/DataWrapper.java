package com.maxchauo.STYLiSH.dto.product.dto.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataWrapper<T> {
  private T data;
}