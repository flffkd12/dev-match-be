package com.devmatch.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // 유저 관련 오류
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "사용자를 찾을 수 없습니다."),

  // 유저 관련 오류
  PROJECT_SAME_STATUS(HttpStatus.BAD_REQUEST, "PROJECT-400-1", "동일한 상태의 프로젝트로 변경할 수 없습니다."),
  PROJECT_FULL_PEOPLE(HttpStatus.BAD_REQUEST, "PROJECT-400-2", "프로젝트 최대 정원에 도달해서 지원서를 승인할 수 없습니다"),
  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-404", "프로젝트를 찾을 수 없습니다."),

  // 인증 관련 오류
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401-INVALID-ACCESS",
      "유효하지 않은 엑세스 토큰입니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401-INVALID-REFRESH",
      "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),
  ACCESS_WITHOUT_LOGIN(HttpStatus.UNAUTHORIZED, "AUTH-401-ACCESS_WITHOUT_LOGIN",
      "로그인 없이 접근할 수 없습니다."),

  // 공통 오류
  CLIENT_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-400", "올바르지 않은 요청입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-500", "서버 내부 오류가 발생하였습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String resultCode;
  private final String message;
}