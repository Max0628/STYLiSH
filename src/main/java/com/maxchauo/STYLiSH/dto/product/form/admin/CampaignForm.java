package com.maxchauo.STYLiSH.dto.product.form.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignForm {
  private int productId;
  private MultipartFile picture;
  private String pictureName;
  private String story;
}
