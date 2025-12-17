package com.devmatch.backend.domain.auth.controller;

import com.devmatch.backend.domain.auth.enums.Token;
import com.devmatch.backend.global.response.ApiResponse;
import com.devmatch.backend.global.response.SuccessCode;
import com.devmatch.backend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @DeleteMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
    CookieUtil.deleteCookie(response, Token.REFRESH_TOKEN.getName());
    CookieUtil.deleteCookie(response, Token.ACCESS_TOKEN.getName());

    return ApiResponse.success(SuccessCode.AUTH_LOGOUT);
  }
}
