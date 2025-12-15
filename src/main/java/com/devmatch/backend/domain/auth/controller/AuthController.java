package com.devmatch.backend.domain.auth.controller;

import com.devmatch.backend.global.RsData;
import com.devmatch.backend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @DeleteMapping("/logout")
  public RsData<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    CookieUtil.deleteCookie(response, "refreshToken");
    CookieUtil.deleteCookie(response, "accessToken");

    return new RsData<>("200-1", "로그아웃 되었습니다.");
  }
}
