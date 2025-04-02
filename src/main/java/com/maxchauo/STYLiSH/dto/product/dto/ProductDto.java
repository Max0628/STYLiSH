package com.maxchauo.STYLiSH.dto.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maxchauo.STYLiSH.dto.product.form.ColorForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
  private String category;
  private String title;
  private String description;
  private String price;
  private String texture;
  private String wash;
  private String place;
  private String note;
  private String story;
  private MultipartFile mainImage;
  private Set<ColorForm> colors;
  private Set<String> sizes;
  private Set<VariantDto> variants;
  private List<MultipartFile> images;
}
