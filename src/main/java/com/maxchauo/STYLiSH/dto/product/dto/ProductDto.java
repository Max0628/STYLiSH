package com.maxchauo.STYLiSH.dto.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
  private Long id;
  private String category;
  private String title;
  private String description;
  private Long price;
  private String texture;
  private String wash;
  private String place;
  private String note;
  private String story;
  private Set<ColorDto> colors;
  private Set<String> sizes;
  private Set<VariantOutputDto> variants;
  @JsonProperty("main_image")
  private String mainImage;
  private List<String> images;
}
