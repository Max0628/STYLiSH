package com.maxchauo.STYLiSH.service.admin;

import com.maxchauo.STYLiSH.dto.product.form.admin.ColorForm;
import com.maxchauo.STYLiSH.dto.product.form.admin.SizeFrom;
import com.maxchauo.STYLiSH.dto.product.form.admin.ProductForm;
import com.maxchauo.STYLiSH.dto.product.form.admin.VariantForm;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.repository.admin.AdminRepository;
import com.maxchauo.STYLiSH.repository.product.ProductRepository;
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
  private final ProductRepository prodRepo;
  private final ImgUtil imgUtil;

  @Transactional
  public boolean insertProduct(ProductForm product, MultipartFile mainImage, List<MultipartFile> images, List<VariantForm> variants) {

    if (!CommonUtil.isNotEmpty(product) || !CommonUtil.isNotEmpty(mainImage) || !CommonUtil.isNotEmpty(images) || !CommonUtil.isNotEmpty(variants)) {
      throw new UserClientException("product, mainImage, images, or variants is empty");
    }
    String mainImageName = imgUtil.saveImage(mainImage);
    product.setUrl(mainImageName);
    long productId = repo.insertProduct(product);

    List<String> imagesName = imgUtil.saveImages(images);
    int imagesId = repo.insertImage(imagesName, productId);
    if (imagesId == 0) {
      throw new RuntimeException("fail to insert images");
    }
    Map<String, Long> colorCache = new HashMap<>();
    Map<String, Long> sizeCache = new HashMap<>();
    for (VariantForm item : variants) {
      String colorKey = item.getColorCode() + "|" + item.getColorName();
      String sizeKey = item.getSize();

      long colorId = colorCache.getOrDefault(colorKey, 0L);
      if (colorId == 0) {
        colorId = prodRepo.findColorIdByCodeAndName(item.getColorCode(), item.getColorName());
        if (colorId == 0) {
          colorId = repo.insertColor(new ColorForm(item.getColorCode(), item.getColorName()));
        }
        colorCache.put(colorKey, colorId);
      }

      long sizeId = sizeCache.getOrDefault(sizeKey, 0L);
      if (sizeId == 0) {
        sizeId = prodRepo.findSizeIdBySize(item.getSize());
        if (sizeId == 0) {
          sizeId = repo.insertSize(new SizeFrom(item.getSize()));
        }
        sizeCache.put(sizeKey, sizeId);
      }


      if (colorId == 0 || sizeId == 0) {
        throw new RuntimeException("fail to insert color or size");
      }

      boolean success = repo.insertVariant(productId, colorId, sizeId, Long.parseLong(item.getStock()));
      if (!success) {
        throw new RuntimeException("fail to insert variant");
      }
    }

    return true;
  }
}
