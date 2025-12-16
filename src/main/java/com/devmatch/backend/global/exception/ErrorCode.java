package com.devmatch.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // 유저 관련 오류
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "사용자를 찾을 수 없습니다."),

  // 인증 관련 오류
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401-INVALID", "유효하지 않은 토큰입니다."),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401-EXPIRED", "토큰이 만료되었습니다."),

  // 공통 오류
  CLIENT_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-400", "올바르지 않은 요청입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-500", "서버 내부 오류가 발생하였습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String resultCode;
  private final String message;
}