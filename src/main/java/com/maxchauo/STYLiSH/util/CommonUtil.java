package com.maxchauo.STYLiSH.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

@Log4j2
@Service
public class CommonUtil {

  /**
   * 檢查傳入物件是否「有任何欄位」不為 null / 空
   *
   * @param dto
   * @return 若非空則為 true
   */
  public static boolean isNotEmpty(Object dto) {
    if (dto == null) return false;

    if (dto instanceof String str) return !str.isBlank();
    if (dto instanceof Number) return true;
    if (dto instanceof Collection<?> collection) return !collection.isEmpty();
    if (dto instanceof Map<?, ?> map) return !map.isEmpty();

    for (Field field : dto.getClass().getDeclaredFields()) {
      try {
        if (!field.canAccess(dto)) {
          field.setAccessible(true);
        }

        Object value = field.get(dto);
        if (isNotEmpty(value)) {
          return true;
        }

      } catch (IllegalAccessException | RuntimeException e) {
        log.warn("CommonUtil exception: " + e);
        continue;
      }
    }

    return false;
  }

  public static String buildFullImageUrl(String domain, String urlPath, String filename) {
    if (filename == null || filename.isBlank() || filename.startsWith("http")) {
      return filename;
    }

    if (domain.endsWith("/")) domain = domain.substring(0, domain.length() - 1);
    if (urlPath.startsWith("/")) urlPath = urlPath.substring(1);
    if (urlPath.endsWith("/")) urlPath = urlPath.substring(0, urlPath.length() - 1);

    return domain + "/" + urlPath + "/" + filename;
  }

  public static boolean isValidPassword(String password){
    if(password ==null || password.length() <8){
      return false;
    }
    boolean hasUpper = password.matches(".*[A-Z].*");
    boolean hasLower = password.matches(".*[a-z].*");
    boolean hasDigit = password.matches(".*[0-9].*");
    boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    return hasUpper && hasLower && hasDigit && hasSpecial;
  }
}
