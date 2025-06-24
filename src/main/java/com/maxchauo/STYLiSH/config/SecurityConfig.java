package com.maxchauo.STYLiSH.config;

import com.maxchauo.STYLiSH.filter.JwtAuthenticationFilter;
import com.maxchauo.STYLiSH.filter.JwtExceptionFilter;
//import com.maxchauo.STYLiSH.filter.RateLimiterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtExceptionFilter jwtExceptionFilter;
//  private final RateLimiterFilter rateLimiterFilter;

  public SecurityConfig(
          JwtAuthenticationFilter jwtAuthenticationFilter, JwtExceptionFilter jwtExceptionFilter
//          , RateLimiterFilter rateLimiterFilter
  ) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.jwtExceptionFilter = jwtExceptionFilter;
//    this.rateLimiterFilter = rateLimiterFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)//remove CsrfFilter
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//SessionManagementFilter
        .authorizeHttpRequests(//FilterSecurityInterceptor
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/admin/**", "/api/v1/admin/**").hasRole("ADMIN")// check admin role
                    .requestMatchers("/js/product.js").permitAll()
                    .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/api/v1/user/signin", "/api/v1/user/signup", "/api/v1/admin/upload").permitAll()
                    .requestMatchers("/api/v1/products/getAllProductIdAndTitle","/api/v1/products/insertCampaignProduct").permitAll()
                    .requestMatchers("/api/v1/products/**", "/api/v1/marketing/**","/api/1.0/report/**").permitAll()
                    .requestMatchers("/api/v1/healthCheck").permitAll()
                    .requestMatchers("/images/**").permitAll().anyRequest().authenticated());
    http.addFilterBefore(jwtExceptionFilter, UsernamePasswordAuthenticationFilter.class);// add new custom filter
    // http.addFilterBefore(rateLimiterFilter, JwtExceptionFilter.class); // add rate limiter filter
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
