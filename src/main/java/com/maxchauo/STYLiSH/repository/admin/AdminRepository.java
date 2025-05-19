package com.maxchauo.STYLiSH.repository.admin;

import com.maxchauo.STYLiSH.dto.product.form.admin.ColorForm;
import com.maxchauo.STYLiSH.dto.product.form.admin.SizeFrom;
import com.maxchauo.STYLiSH.dto.product.form.admin.ProductForm;
import com.maxchauo.STYLiSH.exception.DatabaseOperationException;
import com.maxchauo.STYLiSH.exception.ProductInsertionException;
import com.maxchauo.STYLiSH.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Repository
@RequiredArgsConstructor // constructor injection
public class AdminRepository {

  private final NamedParameterJdbcTemplate template;

  public long insertProduct(ProductForm productForm) {
    final String INSERT_PRODUCT_SQL =
        "INSERT INTO `Product`(category, title, description, price, texture, wash, place, note, story, main_image_url)"
            + "VALUES (:category, :title, :description, :price, :texture, :wash, :place, :note, :story, :main_image_url)";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("category", productForm.getCategory())
            .addValue("title", productForm.getTitle())
            .addValue("description", productForm.getDescription())
            .addValue("price", productForm.getPrice())
            .addValue("texture", productForm.getTexture())
            .addValue("wash", productForm.getWash())
            .addValue("place", productForm.getPlace())
            .addValue("note", productForm.getNote())
            .addValue("story", productForm.getStory())
            .addValue("main_image_url", productForm.getUrl());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    try {
      int rowsAffected = template.update(INSERT_PRODUCT_SQL, param, keyHolder);
      if (rowsAffected == 0) {
        throw new ProductInsertionException("insert product failed, affected rows = 0");
      }
      Number productId = keyHolder.getKey();
      if (productId != null) {
        return productId.longValue();
      } else {
        throw new ProductInsertionException("insert product failed, no productId");
      }
    } catch (DatabaseOperationException e) {
      log.error("insert product to db failed: {}", e.getMessage(), e);
      throw new DatabaseOperationException("insert product to db failed");
    } catch (Exception e) {
      log.error("unexcepted error: {}", e.getMessage(), e);
      throw new SystemException("unexcepted error");
    }
  }

  public long insertColor(ColorForm colorDto) {
    String INSERT_COLOR_SQL = "INSERT INTO `Color`(code, name) VALUES(:code, :name)";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("code", colorDto.getCode())
            .addValue("name", colorDto.getName());
    KeyHolder keyHolder = new GeneratedKeyHolder();
    try {
      int rowsAffected = template.update(INSERT_COLOR_SQL, param, keyHolder);
      if (rowsAffected == 0) {
        throw new ProductInsertionException("failed to insert color, affected rows = 0");
      }
      Number colorId = keyHolder.getKey();
      if (colorId != null) {
        return (colorId.longValue());
      } else {
        throw new ProductInsertionException("failed to insert color, no colorId");
      }
    } catch (DatabaseOperationException e) {
      log.error("insert color to db fail: {}", e.getMessage(), e);
      throw new DatabaseOperationException("insert color to db fail");
    } catch (Exception e) {
      log.error("unexcepted error: {}", e.getMessage(), e);
      throw new SystemException("unexcepted error");
    }
  }

  /**
   * insets size to db
   *
   * @return sizeId {BigInt}
   * @auther tashuchiu
   */
  public long insertSize(SizeFrom sizeDto) {
    String SIZE_INSERT_SQL = "INSERT INTO `Size`(size) VALUES(:size)";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("size", sizeDto.getSize());
    KeyHolder keyHolder = new GeneratedKeyHolder();
    try {
      int rowAffected = template.update(SIZE_INSERT_SQL, param, keyHolder);
      if (rowAffected == 0) {
        throw new ProductInsertionException("insert size failed, affected rows = 0");
      }
      Number sizeId = keyHolder.getKey();
      if (sizeId != null) {
        return sizeId.longValue();
      } else {
        throw new RuntimeException("insert size failed, no sizeId");
      }
    } catch (DataAccessException e) {
      log.error("fail to insert size to db: {}", e.getMessage(), e);
      throw new DatabaseOperationException("fail to insert size to db");
    } catch (Exception e) {
      log.error("unexcepted error: {}", e.getMessage(), e);
      throw new SystemException("unexcepted error");
    }
  }

  /***
   * get productId, colorId, sizeId,insert variant table
   * @param productId
   * @param colorId
   * @param sizeId
   * @param stock
   * @return
   * @author tashuchiu
   */
  public boolean insertVariant(long productId, long colorId, long sizeId, long stock) {
    String INSERT_VARIANT_SQL =
        "INSERT INTO `Variant`(size_id, color_id, product_id, stock) VALUES(:sizeId, :colorId, :productId, :stock)";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("sizeId", sizeId)
            .addValue("colorId", colorId)
            .addValue("productId", productId)
            .addValue("stock", stock);
    try {
      int rowAffected = template.update(INSERT_VARIANT_SQL, param);
      if (rowAffected == 0) {
        throw new ProductInsertionException("failed to insert variant, affected rows = 0");
      }else return true;
    } catch (DataAccessException e) {
      log.error("insert variant to db fail: {}", e.getMessage(), e);
      throw new DatabaseOperationException("insert variant to db fail");
    } catch (Exception e) {
      log.error("unexcepted error: {}", e.getMessage(), e);
      throw new SystemException("unexcepted error");
    }
  }

  public int insertImage(List<String> imagesUrl, long productId) {
    String INSERT_IMAGES_SQL = " INSERT INTO `Image`(url,product_id) VALUES(:url,:productId)";
    List<Map<String, Object>> arrayListMap = new ArrayList<Map<String, Object>>();
    for (String imageUrl : imagesUrl) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("productId", productId);
      paramMap.put("url", imageUrl);
      arrayListMap.add(paramMap);
    }
    Map<String, Object>[] batchArray = arrayListMap.toArray(new Map[0]);
    try {
      int[] affectedRows = template.batchUpdate(INSERT_IMAGES_SQL, batchArray);
      int totalInserted = 0;
      for (int row : affectedRows) {
        totalInserted += row;
      }

      if (totalInserted == 0) {
        throw new ProductInsertionException("failed to insert images, affected rows = 0");
      }
      return totalInserted;
    } catch (DataAccessException e) {
      log.error("fail to insert images to db: {}", e.getMessage(), e);

    } catch (Exception e) {
      log.error("unexpected error: {}", e.getMessage(), e);
      throw new SystemException("unexpected error");
    }
    return 0;
  }
}
