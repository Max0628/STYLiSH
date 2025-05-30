package com.maxchauo.STYLiSH.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImgUtil {
  @Value("${upload.path}")
  private String uploadPngDirectory;

  /**
   * parse multipart file to image url
   *
   * @param image
   * @return
   */
  public String saveImage(MultipartFile image) {
    try {
      if (image != null && !image.isEmpty()) {
        String uniqueImageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadPngDirectory + File.separator + uniqueImageName);
        Files.write(path, image.getBytes());
        return uniqueImageName;
      }
    } catch (Exception e) {
      log.warn("saveImage exception: ", e);
    }
    return "";
  }

  /**
   * parse multipart file list to image url list
   *
   * @param images multipart file list
   * @return image url list
   */
  public List<String> saveImages(List<MultipartFile> images) {
    try {
      if (images != null && !images.isEmpty()) {
        List<String> imagesUrl = new ArrayList<String>();
        for (MultipartFile image : images) {
          if (!image.isEmpty()) {
            String uniqueImageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadPngDirectory + File.separator + uniqueImageName);
            Files.write(path, image.getBytes());
            imagesUrl.add(uniqueImageName);
          }
        }
        return imagesUrl;
      }
    } catch (Exception e) {
      log.warn("saveImages exception: ", e);
    }
    return Collections.emptyList();
  }
}
