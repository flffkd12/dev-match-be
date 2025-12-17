package com.devmatch.backend.domain.auth.security;

import com.devmatch.backend.domain.auth.enums.Token;
import com.devmatch.backend.domain.auth.service.AuthTokenService;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.service.UserService;
import com.devmatch.backend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// 인증 객체가 설정되고 나면 수행되는 작업 관련 클래스
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final AuthTokenService authTokenService;
  private final UserService userService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException {
    log.info("OAuth2 authentication success handler initiated.");

    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    User user = userService.getUser(securityUser.getUserId());
    log.debug("User authenticated: userId={}", user.getId());

    String accessToken = authTokenService.genAccessToken(user);
    log.debug("Access token generated for user {}", user.getId());

    CookieUtil.addCookie(response, Token.REFRESH_TOKEN.getName(), user.getRefreshToken());
    CookieUtil.addCookie(response, Token.ACCESS_TOKEN.getName(), accessToken, -1);
    log.debug("Refresh and access tokens set as cookies for user {}", user.getId());

    String encodedState = request.getParameter("state");
    String decodedState = new String(Base64.getUrlDecoder().decode(encodedState),
        StandardCharsets.UTF_8);
    String redirectUrl = decodedState.split("#")[0];
    log.info("Redirecting user {} to URL: {}", user.getId(), redirectUrl);

    response.sendRedirect(redirectUrl);
  }
}
