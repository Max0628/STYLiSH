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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
@Log4j2
@Service
@RequiredArgsConstructor
public class AdminService {
  private final AdminRepository repo;
  private final ImgUtil imgUtil;
  public boolean insertProduct(ProductForm product, MultipartFile mainImage, List<MultipartFile> images, List<VariantForm> variants) {
    try {
      if (CommonUtil.isNotEmpty(product) && CommonUtil.isNotEmpty(mainImage) && CommonUtil.isNotEmpty(images) && CommonUtil.isNotEmpty(variants)) {

        String mainImageUrl = imgUtil.saveImage(mainImage); // store mainImage to dir , and return url string;
        product.setUrl(mainImageUrl); // make productForm complete
        long productId = repo.insertProduct(product);
        List<String> imagesUrl = imgUtil.saveImages(images); // store images to dir
        int imagesId = repo.insertImage(imagesUrl, productId); // insert images ot DB

        System.out.println("variants: "+variants.toString());
        // took data in current variants to compose size / color / variantDto
        for (VariantForm item : variants) {
          System.out.println("item: "+item.toString());
          String colorCode = item.getColorCode();
          String colorName = item.getColorName();
          String size = item.getSize();
          String stock = item.getStock();

          // current colorDto
          ColorForm colorDto = new ColorForm();
          colorDto.setCode(colorCode);
          colorDto.setName(colorName);

          // current size
          SizeFrom sizeDto = new SizeFrom();
          sizeDto.setSize(size);

          long colorId = repo.insertColor(colorDto);
          long sizeId = repo.insertSize(sizeDto);
          // insert variant
          boolean insertVariantSucceed = repo.insertVariant(productId, colorId, sizeId, Long.parseLong(stock));
          if (colorId == 0 || sizeId == 0 || !insertVariantSucceed || imagesId == 0) {
            return false;
          }
        }
        return true;
      }
    } catch (Exception e) {
      log.warn("AdminService exception: "+ e);
      return false;
    }
    return false;
  }
}
