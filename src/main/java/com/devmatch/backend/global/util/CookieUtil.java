package com.devmatch.backend.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public final class CookieUtil {

  private static final int DEFAULT_MAX_AGE = 60 * 60 * 24 * 365; // 1년

  private CookieUtil() {
  }

  public static void addCookie(
      HttpServletResponse response,
      String name,
      String value,
      int maxAge
  ) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    cookie.setSecure(true);
    cookie.setAttribute("SameSite", "Strict");

    response.addCookie(cookie);
  }

  public static void addCookie(
      HttpServletResponse response,
      String name,
      String value
  ) {
    addCookie(response, name, value, DEFAULT_MAX_AGE);
  }

  public static void deleteCookie(HttpServletResponse response, String name) {
    addCookie(response, name, "", 0);
  }

  public static String getCookieValue(HttpServletRequest request, String name) {
    return Optional.ofNullable(request.getCookies())
        .flatMap(cookies ->
            Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
        ).orElse(null);
  }
}
