package com.maxchauo.STYLiSH.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Value("${upload.path}")
  private String uploadPngDirectory;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/images/**") // setting url path mapping to local directory rule.
        .addResourceLocations("file:" + uploadPngDirectory + "/");
  }
}
