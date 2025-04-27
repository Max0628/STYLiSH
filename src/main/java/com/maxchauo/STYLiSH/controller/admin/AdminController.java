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
 * 處理與後台相關的 request
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
   * 上傳商品資料至後端系統
   * @param category 商品類別 ex:'men','women','grocery'
   * @param title 商品標題 ex: '前開衩扭結洋裝'
   * @param description 商品明細 ex: '厚薄：高抗寒素材選用，保暖也時尚有型'
   * @param price 商品價錢 ex: '2200'
   * @param texture 商品材質 ex: '棉、聚脂纖維'
   * @param wash 清洗方法 ex: '手洗（水溫40度'
   * @param place 商品產地 ex: '韓國'
   * @param note 商品備註 ex: '實品顏色以單品照為主'
   * @param story 商品故事 ex: '你絕對不能錯過的超值商品'
   * @param variant 商品變體 JSON 字串
   * @param mainImage 主圖片(MultipartFile) ex: 'https://stylish.com/main.jpg'
   * @param images 子圖片(MultipartFile) ex:['https://stylish.com/0.jpg','https://stylish.com/1.jpg','https://stylish.com/2.jpg']
   * @return 回傳 JSON 物件，表示新增成功或失敗
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

      // 取出變體
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
        returnMap.put("response", "商品上傳成功");
        return new ResponseEntity<>(returnMap, HttpStatus.CREATED);
      }
    } catch (Exception e) {
      Map<String, Object> failMap = new HashMap<>();
      failMap.put("statusCode", "400");
      failMap.put("response", "商品上傳失敗，您填寫的表單資訊有誤");
      return new ResponseEntity<>(failMap, HttpStatus.BAD_REQUEST);
    }
    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("statusCode", "500");
    errorMap.put("response", "商品上傳失敗，請聯絡管理員");
    return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
  }
}
