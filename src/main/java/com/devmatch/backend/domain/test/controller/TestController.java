package com.devmatch.backend.domain.test.controller;

import com.devmatch.backend.domain.auth.service.AuthTokenService;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.service.UserService;
import com.devmatch.backend.global.response.ApiResponse;
import com.devmatch.backend.global.response.SuccessCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("!prod")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

  private final UserService userService;
  private final AuthTokenService authTokenService;

  @PostMapping("/users/login")
  public ResponseEntity<ApiResponse<String>> setupTestUser(
      @RequestParam(value = "oauthId", required = false) String oauthId,
      @RequestParam(value = "nickname", required = false) String nickname
  ) {
    String finalOauthId = oauthId != null ? oauthId : "test_" + UUID.randomUUID();
    String finalNickname = nickname != null ? nickname : "User_" + finalOauthId.substring(0, 8);

    User user = userService.modifyOrJoin(finalOauthId, finalNickname, null);
    String accessToken = authTokenService.genAccessToken(user);

    return ApiResponse.success(SuccessCode.USER_FETCH, accessToken);
  }
}
