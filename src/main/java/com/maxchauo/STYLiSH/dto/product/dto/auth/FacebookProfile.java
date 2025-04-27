package com.maxchauo.STYLiSH.dto.product.dto.auth;

import lombok.Data;

@Data
public class FacebookProfile {
  private String name;
  private String email;
  private Picture picture;

  @Data
  public static class Picture {
    private PictureData data;

    @Data
    public static class PictureData {
      private String url;
    }
  }
}

