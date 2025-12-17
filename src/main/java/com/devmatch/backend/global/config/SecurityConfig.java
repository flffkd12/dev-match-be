package com.devmatch.backend.global.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.devmatch.backend.domain.auth.security.CustomAuthenticationFilter;
import com.devmatch.backend.domain.auth.security.CustomOAuth2LoginFailureHandler;
import com.devmatch.backend.domain.auth.security.CustomOAuth2LoginSuccessHandler;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomAuthenticationFilter customAuthenticationFilter;
  private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
  private final CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler;
  private final HandlerExceptionResolver handlerExceptionResolver;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers("/favicon.ico", "/h2-console/**", "/").permitAll()
                .requestMatchers("/users/**").authenticated()
                .requestMatchers("/projects/**").authenticated()
                .requestMatchers("/analysis/**").authenticated()
                .requestMatchers("/applications/**").authenticated()
                .anyRequest().denyAll()
        )
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
        .oauth2Login(oauth2Login -> oauth2Login.successHandler(customOAuth2LoginSuccessHandler)
            .failureHandler(customOAuth2LoginFailureHandler))
        .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(
                    (request, response, authException) -> handlerExceptionResolver.resolveException(
                        request, response, null, new CustomException(ErrorCode.ACCESS_WITHOUT_LOGIN)
                    )
                )
        )
        .build();
  }

  // CORS 설정을 위한 Bean
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:3000"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
    configuration.setAllowedHeaders(List.of("*"));

    // 클라이언트가 쿠키 실어 보낼 수 있도록 설정
    configuration.setAllowCredentials(true);

    // CORS 설정을 소스에 등록
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
