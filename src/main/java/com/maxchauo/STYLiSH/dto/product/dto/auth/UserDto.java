package com.maxchauo.STYLiSH.dto.product.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Long id;
  private String provider;
  private String name;
  private String email;
  private String password;
  private String picture;
  private String role; // e.g., "USER", "ADMIN"
}
