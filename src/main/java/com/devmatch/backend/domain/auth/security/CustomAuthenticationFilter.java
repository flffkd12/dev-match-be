package com.devmatch.backend.domain.auth.security;

import com.devmatch.backend.domain.auth.enums.Token;
import com.devmatch.backend.domain.auth.service.AuthTokenService;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.service.UserService;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import com.devmatch.backend.global.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// 요청에 대한 인증 여부 확인 클래스
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {

  private final AuthTokenService authTokenService;
  private final UserService userService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    log.debug("Processing request for {}", request.getRequestURI());
    if (isOAuth2LoginProcessURI(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = CookieUtil.getCookieValue(request, Token.ACCESS_TOKEN.getName());
    if (accessToken != null) {
      try {
        Map<String, Object> payload = authTokenService.getPayload(accessToken);
        setAuthenticationContext(userService.getUser((Long) payload.get("id")));
      } catch (ExpiredJwtException e) {
        String refreshToken = CookieUtil.getCookieValue(request, Token.REFRESH_TOKEN.getName());
        User user = userService.getUserByRefreshToken(refreshToken);
        CookieUtil.addCookie(response, Token.ACCESS_TOKEN.getName(), authTokenService.genAccessToken(user));
        setAuthenticationContext(user);
      } catch (Exception e) {
        log.error("Invalid access token stack trace: ", e);
        throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
      }
    }

    filterChain.doFilter(request, response);
  }

  private boolean isOAuth2LoginProcessURI(String requestURI) {
    return requestURI.startsWith("/oauth2/authorization/") ||
        requestURI.startsWith("/login/oauth2/code/");
  }

  private void setAuthenticationContext(User user) {
    UserDetails authUser = new SecurityUser(user.getId(), user.getOauthId(), user.getAuthorities());
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        authUser,
        authUser.getPassword(),
        authUser.getAuthorities()
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}