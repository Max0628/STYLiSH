package com.maxchauo.STYLiSH.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maxchauo.STYLiSH.dto.product.form.admin.ProductForm;
import com.maxchauo.STYLiSH.dto.product.form.admin.VariantForm;
import com.maxchauo.STYLiSH.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * handle admin backend related request
 * @author tashuchiu
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
  private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(AdminController.class);
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AdminService adminService;

  /**
   * Upload product data to the backend system.
   *
   * @param category Product category, ex: '男','女','雜貨'
   * @param title Product title, ex: '前開衩扭結洋裝'
   * @param description Product description, ex: '厚薄：高抗寒素材選用，保暖也時尚有型'
   * @param price Product price, ex: '2200'
   * @param texture Product material, ex: '棉、聚脂纖維'
   * @param wash Washing instructions, ex: '手洗（水溫40度）'
   * @param place Place of origin, ex: '韓國'
   * @param note Product notes, ex: '實品顏色以單品照為主'
   * @param story Product story, ex: '你絕對不能錯過的超值商品'
   * @param variant Product variants as a JSON string
   * @param mainImage Main image (MultipartFile), ex: 'https://stylish.com/main.jpg'
   * @param images Additional images (MultipartFile list), ex: ['https://stylish.com/0.jpg', 'https://stylish.com/1.jpg', 'https://stylish.com/2.jpg']
   * @return Returns a JSON object indicating whether the upload was successful or failed
   */
  @PostMapping("/upload")
  public ResponseEntity<Map<String, Object>> insertProduct(
      @RequestPart("category") String category,
      @RequestPart("title") String title,
      @RequestPart("description") String description,
      @RequestPart("price") String price,
      @RequestPart("texture") String texture,
      @RequestPart("wash") String wash,
      @RequestPart("place") String place,
      @RequestPart("note") String note,
      @RequestPart("story") String story,
      @RequestPart(value = "mainImage", required = true) MultipartFile mainImage,
      @RequestParam("variant") String variant,
      @RequestPart(value = "images[]", required = true) List<MultipartFile> images) {
    try {
      ProductForm product = new ProductForm();
      product.setCategory(category);
      product.setTitle(title);
      product.setDescription(description);
      product.setPrice(price);
      product.setTexture(texture);
      product.setWash(wash);
      product.setPlace(place);
      product.setStory(story);
      product.setNote(note);
      List<VariantForm> variants = objectMapper.readValue(variant, new TypeReference<List<VariantForm>>() {});
      boolean insertSuccess = adminService.insertProduct(product, mainImage, images, variants);
      if(insertSuccess){
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("statusCode", "201");
        returnMap.put("response", "upload product success");
        return new ResponseEntity<>(returnMap, HttpStatus.CREATED);
      }
    } catch (Exception e) {
      Map<String, Object> failMap = new HashMap<>();
      failMap.put("statusCode", "400");
      failMap.put("response", "upload product fail, something wrong in your form");
      return new ResponseEntity<>(failMap, HttpStatus.BAD_REQUEST);
    }
    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("statusCode", "500");
    errorMap.put("response", "upload product fail, please contact to IT team.");
    return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
  }
}
