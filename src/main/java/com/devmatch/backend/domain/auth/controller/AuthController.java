package com.devmatch.backend.domain.auth.controller;

import com.devmatch.backend.global.RsData;
import com.devmatch.backend.global.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final Rq rq;

  @DeleteMapping("/logout")
  public RsData<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    rq.deleteCookie("refreshToken");
    rq.deleteCookie("accessToken");

    return new RsData<>("200-1", "로그아웃 되었습니다.");
  }
}
