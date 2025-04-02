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
   * @param dto
   * @return 若非空則為 true
   */
  public static boolean isNotEmpty(Object dto) {
    if (dto == null) return false;

    // 基本型別先判斷，避免對系統類別做反射
    if (dto instanceof String str) return !str.isBlank();
    if (dto instanceof Number) return true;
    if (dto instanceof Collection<?> collection) return !collection.isEmpty();
    if (dto instanceof Map<?, ?> map) return !map.isEmpty();

    // 只針對自定義物件使用反射
    for (Field field : dto.getClass().getDeclaredFields()) {
      try {
        if (!field.canAccess(dto)) {
          field.setAccessible(true);
        }

        Object value = field.get(dto);
        if (isNotEmpty(value)) { // 巢狀遞迴檢查
          return true;
        }

      } catch (IllegalAccessException | RuntimeException e) {
        log.warn("CommonUtil exception: "+ e);
        continue;
      }
    }

    return false;
  }
}
