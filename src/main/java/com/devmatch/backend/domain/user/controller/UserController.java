package com.devmatch.backend.domain.user.controller;

import com.devmatch.backend.domain.auth.security.SecurityUser;
import com.devmatch.backend.domain.user.dto.UserResponse;
import com.devmatch.backend.domain.user.service.UserService;
import com.devmatch.backend.global.response.ApiResponse;
import com.devmatch.backend.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/profile")
  public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    UserResponse user = userService.getUserResponse(securityUser.getUserId());
    return ApiResponse.success(SuccessCode.USER_FETCH, user);
  }
}
