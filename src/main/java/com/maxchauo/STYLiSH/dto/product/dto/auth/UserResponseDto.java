package com.maxchauo.STYLiSH.dto.product.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
  private Long id;
  private String provider;
  private String name;
  private String email;
  private String picture;
}
