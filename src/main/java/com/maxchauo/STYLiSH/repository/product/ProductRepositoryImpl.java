package com.maxchauo.STYLiSH.repository.product;

import com.maxchauo.STYLiSH.dto.product.dto.*;
import com.maxchauo.STYLiSH.dto.product.form.ProductQueryCondition;
import com.maxchauo.STYLiSH.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;


@Log4j2
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository{
  private final NamedParameterJdbcTemplate template;

  @Override
  public List<ProductDto> findProductByCondition(ProductQueryCondition condition) {
    String category = condition.getCategory();
    int paging = condition.getPaging();
    int pageSize = condition.getPageSize();
    int offset = paging * pageSize;
    int limit = pageSize + 1;
    String QUERY_PRODUCT =
        "SELECT id, category, title, description, price, texture, wash, place, note, story, main_image_url "
            + "FROM `Product` "
            + "WHERE category =" + "'" + category + "'"
            + " LIMIT " + limit +  " OFFSET " + offset + " ;";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("category", category)
            .addValue("limit", limit)
            .addValue("offset", offset);
    try {
      List<ProductDto> data = template.query(QUERY_PRODUCT, param, new ProductRowMapper());
      return data;
    } catch (Exception e) {
      log.warn("findProductByCondition exception", e);
      return List.of();
    }
  }

  @Override
  public Map<Long, List<String>> findImageUrlById(List<Long> productIds) {
    String QUERY_IMAGE = "SELECT id, url, product_id FROM `Image` WHERE product_Id IN (:productIds)";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("productIds", productIds);
    try {
      List<ImageDto> result = template.query(QUERY_IMAGE, param, new ImageRowMapper()); // finished query image as a list.
      Map<Long, List<String>> imageMap = new HashMap<>(); // create a container of return
      for (ImageDto dto : result) {
        Long productId = dto.getProductId();
        String url = dto.getUrl();
        if (!imageMap.containsKey(productId)) { // if there is no key in the map , create one.
          imageMap.put(productId, new ArrayList<>());
        }
        imageMap.get(productId).add(url);
      }
      return imageMap;
    } catch (Exception e) {
      log.warn("findImageUrlById exception: " + e);
    }
    return Collections.emptyMap();
  }

  @Override
  public Map<Long, List<VariantDto>> findVariantById(List<Long> productIds) {
    String QUERY_VARIANT = "SELECT size_id, color_id, product_id, stock FROM `Variant` WHERE product_Id IN (:productIds)";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("productIds", productIds);
    try {
      List<VariantDto> result = template.query(QUERY_VARIANT, param, new VariantRowMapper());
      Map<Long, List<VariantDto>> variantMap = new HashMap<>();
      for (VariantDto dto : result) {
        long productId = dto.getProduct_id();
        if (!variantMap.containsKey(productId)) {
          variantMap.put(productId, new ArrayList<>());
        }
        variantMap.get(productId).add(dto);
      }
      return variantMap;
    } catch (Exception e) {
      log.warn("findVariantById exception: ", e);
    }
    return Collections.emptyMap();
  }

  @Override
  public Map<Long,ColorDto> findColorById(List<Long> colorIds) {
    String QUERY_COLOR = "SELECT id, code, name FROM `Color` WHERE id IN (:colorIds)";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("colorIds", colorIds);
    try {
      List<ColorDto> result = template.query(QUERY_COLOR, param, new ColorRowMapper());
      Map<Long, ColorDto> colorMap = new HashMap<>();
      for (ColorDto dto : result) {
          colorMap.put(dto.getId(), dto);
        }
      return colorMap;
    } catch (Exception e) {
      log.warn("findColorById: ", e);
    }
    return Collections.emptyMap();
  }

  @Override
  public Map<Long, SizeDto> findSizeById(List<Long> sizeIds) {
    String QUERY_SIZE = "SELECT id, size FROM `Size` WHERE id IN (:sizeIds)";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("sizeIds", sizeIds);
    try {
      List<SizeDto> result = template.query(QUERY_SIZE, param, new SizeRowMapper());
      Map<Long, SizeDto> sizeMap = new HashMap<>();
      for (SizeDto dto : result) {
          sizeMap.put(dto.getId(), dto);

      }
      return sizeMap;
    } catch (Exception e) {
      log.warn("findSizeById exception: ", e);
    }
    return Collections.emptyMap();
  }

}
