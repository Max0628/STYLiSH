package com.maxchauo.STYLiSH.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "STYLiSH API",
                version = "1.0.0",
                description = "STYLiSH 商品平台 API 文件"
        )
)
@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi apiV1(){
  return GroupedOpenApi.builder()
          .group("v1")
          .pathsToMatch("/api/v1/**","/admin/**")
          .displayName("STYLiSH API v1 - 商品前後台")
          .build();
  }

  @Bean
  public GroupedOpenApi apiV2(){
    return GroupedOpenApi.builder()
            .group("v2")
            .pathsToMatch("/api/v2/**")
            .build();
  }
}
