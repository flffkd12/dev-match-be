package com.devmatch.backend.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

  AUTH_LOGOUT(HttpStatus.OK, "200", "로그아웃 성공"),
  USER_FETCH(HttpStatus.OK, "200", "프로필 조회 성공"),
  ;

  private final HttpStatus httpStatus;
  private final String resultCode;
  private final String message;
}
