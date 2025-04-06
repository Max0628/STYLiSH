package com.maxchauo.STYLiSH.service.product;

import com.maxchauo.STYLiSH.dto.product.dto.*;
import com.maxchauo.STYLiSH.dto.product.form.ProductQueryCondition;
import com.maxchauo.STYLiSH.dto.product.response.ProductResponse;
import com.maxchauo.STYLiSH.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService{
  private  final  ProductRepository repo;

  public ProductServiceImpl(ProductRepository repo) {
    this.repo = repo;
  }

  @Override
  public ProductResponse findProductByCondition(ProductQueryCondition condition) {

    try{
      List<ProductDto>  productDtos = repo.findProductByCondition(condition);
      List<Long> productIds = new ArrayList<>();
      for(ProductDto item: productDtos){
        productIds.add(item.getId()); //put productId into a list.
      }
      Integer nextPaging = null;
      if(productDtos.size() > condition.getPageSize()){
        nextPaging = condition.getPaging()+1;
        productDtos = productDtos.subList(0,condition.getPageSize());
      }

      //use productId get image
      Map<Long, List<String>>  imageMap = repo.findImageUrlById(productIds);
      Map<Long, List<VariantDto>>  variantMap = repo.findVariantById(productIds);
      List<VariantDto> allVariant = new ArrayList<>();
      for(List<VariantDto> variants : variantMap.values()){ // extract variantList out from the HashMap.
        allVariant.addAll(variants);
      }

      List<Long> colorIds = new ArrayList<>();
      List<Long> sizeIds = new ArrayList<>();

      for(VariantDto variantDto: allVariant){ // allVariant is an ArrayList of VariantDto.
        colorIds.add(variantDto.getColor_id()); // extract colorId & sizeId from variantDto into each ArrayList.
        sizeIds.add(variantDto.getSize_id());
      }

      Map<Long,ColorDto> colorMap = repo.findColorById(colorIds); // get color & size info.
      Map<Long,SizeDto> sizeMap = repo.findSizeById(sizeIds);

      // extract data finished.
      // starting put data together form ProductDto.

      List<ProductDto> result = new ArrayList<>(); // will return an arrayList of ProductDto.
      for(ProductDto p: productDtos){
        Long pid = p.getId();
        ProductDto dto = new ProductDto();
        dto.setId(pid);
        dto.setCategory(p.getCategory());
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setTexture(p.getTexture());
        dto.setNote(p.getNote());
        dto.setStory(p.getStory());
        dto.setMainImage(p.getMainImage());
        dto.setImages(imageMap.getOrDefault(pid,List.of()));

        List<VariantOutputDto> variantList = new ArrayList<>();
        Set<String> sizeSet = new HashSet<>();
        Set<ColorDto> colorSet = new HashSet<>();

        for(VariantDto variant: variantMap.getOrDefault(pid,List.of())){
          ColorDto color = colorMap.get(variant.getColor_id());
          SizeDto size = sizeMap.get(variant.getSize_id());

          if (color != null && size != null) {
            variantList.add(new VariantOutputDto(color.getCode(), size.getSize(), variant.getStock()));
            colorSet.add(color);
            sizeSet.add(size.getSize());
          }
        }
        dto.setVariants(new HashSet<>(variantList));
        dto.setColors(colorSet);
        dto.setSizes(sizeSet);
        result.add(dto);
      }
      return new ProductResponse(result,nextPaging);
    }catch (Exception e){
      e.printStackTrace();
    }
    return new ProductResponse(List.of(),null);
  }
}
