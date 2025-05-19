package com.maxchauo.STYLiSH.repository.product;

import com.maxchauo.STYLiSH.dto.product.dto.product.*;
import com.maxchauo.STYLiSH.dto.product.form.admin.ProductQueryConditionForm;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.mapper.product.*;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@ToString
@Log4j2
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository{
  private final NamedParameterJdbcTemplate template;

  @Override
  public List<ProductDto> findProductByCondition(ProductQueryConditionForm condition) {
    String category = condition.getCategory() != null ?  condition.getCategory().trim() : null;
    Integer id = condition.getId() != null ? condition.getId() : null;
    String keyword = condition.getKeyword() != null ? condition.getKeyword().trim() : null;
    int paging = condition.getPaging() != null ? condition.getPaging() : 0;
    int pageSize = condition.getPageSize() != null ? condition.getPageSize() : 6;
    int offset = paging * pageSize ;
    int limit = pageSize + 1;

    StringBuilder sql = new StringBuilder(
        "SELECT id, category, title, description, price, texture, wash, place, note, story, main_image_url "
            + "FROM `Product` "
            + "WHERE 1 = 1 ");
    MapSqlParameterSource param = new MapSqlParameterSource();
    if(id!=null){
      sql.append(" AND id = :id");
      param.addValue("id",id);
    }

    if(category !=null && !category.isBlank()){
      sql.append(" AND category = :category");
      param.addValue("category",category);
    }

    if(keyword != null && !keyword.isBlank()){
      sql.append(" AND title LIKE :keyword");
      param.addValue("keyword","%" + keyword + "%");
    }

    sql.append(" LIMIT :limit OFFSET :offset");
    param.addValue("limit", limit);
    param.addValue("offset", offset);
    try {
      return template.query(sql.toString(), param, new ProductRowMapper());
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
  public Map<Long, ColorDto> findColorById(List<Long> colorIds) {
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

  @Override
  public boolean existsProductById(Long productId) {
    String sql = "SELECT COUNT(1) FROM Product WHERE id = :productId";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("productId", productId);
    try {
      Integer count = template.queryForObject(sql, param, Integer.class);
      return count != null && count > 0;
    } catch (Exception e) {
      log.warn("existsProductById exception", e);
      throw new UserClientException("product not exist: " + productId);
    }
  }

  @Override
  public ProductDto findProductById(Long productId) {
    String sql = "SELECT id, category, title, description, price, texture, wash, place, note, story, main_image_url FROM Product WHERE id = :productId";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("productId", productId);
    try {
      return template.queryForObject(sql, param, new ProductRowMapper());
    } catch (Exception e) {
      log.warn("findProductById exception", e);
      throw new UserClientException("product not exist: " + productId);
    }
  }

  @Override
  public Long findColorIdByCodeAndName(String code, String name) {
    String sql = "SELECT id FROM Color WHERE code = :code AND name = :name";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("code", code)
            .addValue("name", name);
    List<Long> resultList = template.query(sql, param, (rs, rowNum) -> rs.getLong("id"));
    if (resultList != null && !resultList.isEmpty()) {
      return resultList.get(0);
    } else {
      return 0L;
    }
  }


  @Override
  public Long findSizeIdBySize(String size) {
    String sql = "SELECT id FROM Size WHERE size = :size";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("size", size);
    List<Long> resultList = template.query(sql, param, (rs, rowNum) -> rs.getLong("id"));
    if (resultList != null && !resultList.isEmpty()) {
      return resultList.get(0);
    } else {
      return 0L;
    }
  }


  @Override
  public VariantDto findVariantByProductIdAndColorIdAndSizeId(Long productId, Long colorId, Long sizeId) {
    String sql = "SELECT product_id, color_id, size_id, stock FROM Variant WHERE product_id = :productId AND color_id = :colorId AND size_id = :sizeId";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("productId", productId)
            .addValue("colorId", colorId)
            .addValue("sizeId", sizeId);
    try {
      return template.queryForObject(sql, param, new VariantRowMapper());
    } catch (Exception e) {
      log.warn("findVariantByProductIdAndColorIdAndSizeId exception", e);
      throw new UserClientException("variant not exist: productId=" + productId + ", colorId=" + colorId + ", sizeId=" + sizeId);
    }
  }
}
