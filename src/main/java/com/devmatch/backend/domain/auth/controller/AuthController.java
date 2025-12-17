package com.devmatch.backend.domain.auth.controller;

import com.devmatch.backend.global.response.ApiResponse;
import com.devmatch.backend.global.success.SuccessCode;
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
    CookieUtil.deleteCookie(response, "refreshToken");
    CookieUtil.deleteCookie(response, "accessToken");

    return ApiResponse.success(SuccessCode.LOGOUT_SUCCESS);
  }
}
