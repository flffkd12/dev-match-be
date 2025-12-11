package com.devmatch.backend.domain.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomOAuth2LoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception
  ) throws IOException {
    log.error("### OAuth2 Login Failed:", exception);
    String redirectUrl = "http://localhost:8080/";
    response.sendRedirect(redirectUrl);
  }
}
