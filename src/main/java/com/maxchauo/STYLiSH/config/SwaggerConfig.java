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
                description = "STYLiSH API docs"
        )
)
@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi frontendApi() {
    return GroupedOpenApi.builder()
            .group("frontend")
            .pathsToMatch("/api/v1/products/**", "/api/v1/user/**")
            .displayName("STYLiSH API - frontend")
            .build();
  }

  @Bean
  public GroupedOpenApi adminApi() {
    return GroupedOpenApi.builder()
            .group("admin")
            .pathsToMatch("/api/v1/admin/**")
            .displayName("STYLiSH API - backend")
            .build();
  }

}
