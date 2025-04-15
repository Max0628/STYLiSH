package com.maxchauo.STYLiSH.service.admin;

import com.maxchauo.STYLiSH.dto.product.form.ColorForm;
import com.maxchauo.STYLiSH.dto.product.form.SizeFrom;
import com.maxchauo.STYLiSH.dto.product.form.ProductForm;
import com.maxchauo.STYLiSH.dto.product.form.VariantForm;
import com.maxchauo.STYLiSH.repository.admin.AdminRepository;
import com.maxchauo.STYLiSH.util.CommonUtil;
import com.maxchauo.STYLiSH.util.ImgUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminService {

  private final AdminRepository repo;
  private final ImgUtil imgUtil;

  @Transactional
  public boolean insertProduct(ProductForm product, MultipartFile mainImage, List<MultipartFile> images, List<VariantForm> variants) {

    if (!CommonUtil.isNotEmpty(product) || !CommonUtil.isNotEmpty(mainImage) || !CommonUtil.isNotEmpty(images) || !CommonUtil.isNotEmpty(variants)) {
      throw new IllegalArgumentException("參數不得為空");
    }

    String mainImageName = imgUtil.saveImage(mainImage);
    product.setUrl(mainImageName);
    long productId = repo.insertProduct(product);

    List<String> imagesName = imgUtil.saveImages(images);
    int imagesId = repo.insertImage(imagesName, productId);
    if (imagesId == 0) {
      throw new RuntimeException("圖片儲存失敗");
    }

    for (VariantForm item : variants) {
      ColorForm colorDto = new ColorForm(item.getColorCode(), item.getColorName());
      SizeFrom sizeDto = new SizeFrom(item.getSize());

      long colorId = repo.insertColor(colorDto);
      long sizeId = repo.insertSize(sizeDto);

      if (colorId == 0 || sizeId == 0) {
        throw new RuntimeException("顏色或尺寸儲存失敗");
      }

      boolean success = repo.insertVariant(productId, colorId, sizeId, Long.parseLong(item.getStock()));
      if (!success) {
        throw new RuntimeException("variant 儲存失敗");
      }
    }

    return true;
  }
}
