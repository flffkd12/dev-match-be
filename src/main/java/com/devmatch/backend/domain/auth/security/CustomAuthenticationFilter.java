package com.devmatch.backend.domain.auth.security;

import com.devmatch.backend.domain.auth.service.AuthTokenService;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.service.UserService;
import com.devmatch.backend.global.rq.Rq;
import com.devmatch.backend.global.util.CookieUtil;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {

  private final AuthTokenService authTokenService;
  private final UserService userService;
  private final Rq rq;

  // 엑세스 토큰이 유효한지 확인하고, 인증된 사용자 정보를 SecurityContextHolder에 저장하는 메소드
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

    String refreshToken = CookieUtil.getCookieValue(request, "refreshToken");
    String accessToken = CookieUtil.getCookieValue(request, "accessToken");

    boolean isRefreshTokenExists = !refreshToken.isBlank();
    boolean isAccessTokenExists = !accessToken.isBlank();

    if (!isRefreshTokenExists && !isAccessTokenExists) {
      filterChain.doFilter(request, response);
      return;
    }

    //뭐가 됐던 이 시점부터는 로그인 후의 요청이라는 것 -> 토큰들을 가지고 있으니까.
    User user = null;
    boolean isAccessTokenValid = false;

    if (isAccessTokenExists) {
      Map<String, Object> payload = authTokenService.payload(accessToken);

      if (payload != null) {
        Long id = (Long) payload.get("id");
        user = userService.getUser(id);

        isAccessTokenValid = true;
      }
    }

    if (user == null) {
      user = userService.getUserByRefreshToken(refreshToken);
    }

    if (isAccessTokenExists && !isAccessTokenValid) {
      String actorAccessToken = authTokenService.genAccessToken(user);
      CookieUtil.addCookie(response, "accessToken", actorAccessToken, -1);
    }

    //이제 이 사용자는 인증된 사용자이다. 다만 우리 db에서 꺼낸게 아닌 사용자가 준 토큰에서 꺼낸 정보로 이루어짐.
    //이게 Rq에에서 getActor()를 통해 꺼내지는 정보이다.
    UserDetails authUser = new SecurityUser(
        user.getId(),
        user.getOauthId(),
        user.getAuthorities()
    );

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        authUser,
        authUser.getPassword(),
        authUser.getAuthorities()
    );

    // 이 시점 이후부터는 시큐리티가 이 요청을 인증된 사용자(기존의 로그인 해서 토큰을 발급 받은 사람)의 요청이다.
    // SecurityContext에 인증 정보를 저장한다.(Authentication 객체)
    // 이게 어떻게 rq로 들어가는거지? -> 콘텍스트에서 authentication 객체를 꺼내서 User로 캐스팅.
    //로그인 후의 유저 정보다.
    SecurityContextHolder
        .getContext()
        .setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private boolean isOAuth2LoginProcessURI(String requestURI) {
    return requestURI.startsWith("/oauth2/authorization/") ||
        requestURI.startsWith("/login/oauth2/code/");
  }
}