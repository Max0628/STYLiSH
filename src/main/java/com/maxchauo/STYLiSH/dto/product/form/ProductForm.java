package com.maxchauo.STYLiSH.dto.product.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {
  private String category;
  private String title;
  private String description;
  private String price;
  private String texture;
  private String wash;
  private String place;
  private String note;
  private String story;
  private String url;
}
