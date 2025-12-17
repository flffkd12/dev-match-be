package com.devmatch.backend.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

  LOGOUT_SUCCESS(HttpStatus.OK, "200", "로그아웃 되었습니다."),
  OK(HttpStatus.OK, "200", "요청이 성공적으로 처리되었습니다."),
  CREATED(HttpStatus.CREATED, "201", "리뷰가 성공적으로 등록되었습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String resultCode;
  private final String message;
}
