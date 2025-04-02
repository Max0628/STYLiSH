package com.maxchauo.STYLiSH.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImgUtil {
  @Value("${upload.path}") // 抓取 前儲存檔案的資料夾路徑
  private String uploadPngDirectory;

  @Value("${upload.domain}") // 抓取當前的 domain 寫死在 .properties 裡面
  private String uploadPngDomain;

  /**
   * 單張圖片解析出圖片名稱，二進位資料
   *
   * @param image
   * @return
   */
  public String saveImage(MultipartFile image) {
    try {
      if (image != null && !image.isEmpty()) {
        String uniqueImageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadPngDirectory, uniqueImageName);
        Files.write(path, image.getBytes());
        return uploadPngDomain + "/images/" + uniqueImageName;
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.warn("saveImage exception: ", e);
    }
    return "";
  }

  /**
   * 傳入多張商品副圖片 MultipartFile 檔案，回傳 商品副圖片可訪問之 url List 用於插入資料庫
   *
   * @param images 商品多張副圖片 MultipartFile 格式
   * @return 可訪問的副圖片 url List，用於插入資料庫
   */
  public List<String> saveImages(List<MultipartFile> images) {
    try {
      if (images != null && !images.isEmpty()) {
        List<String> imagesUrl = new ArrayList<String>();
        for (MultipartFile image : images) {
          if (!image.isEmpty()) {
            String uniqueImageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadPngDirectory, uniqueImageName);
            Files.write(path, image.getBytes());
            imagesUrl.add(uploadPngDomain + "/images/" + uniqueImageName);
          }
        }
        return imagesUrl;
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.warn("saveImages exception: ", e);
    }
    return Collections.emptyList();
  }
}
