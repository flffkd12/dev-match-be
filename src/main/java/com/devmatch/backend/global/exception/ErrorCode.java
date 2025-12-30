package com.devmatch.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // 인증 관련 오류
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401-1", "유효하지 않은 엑세스 토큰입니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401-2", "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),
  ACCESS_WITHOUT_LOGIN(HttpStatus.UNAUTHORIZED, "AUTH-401-3", "로그인 없이 접근할 수 없습니다."),

  // 유저 관련 오류
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "사용자를 찾을 수 없습니다."),

  // 프로젝트 관련 오류
  PROJECT_SAME_STATUS(HttpStatus.BAD_REQUEST, "PROJECT-400-1", "동일한 상태의 프로젝트로 변경할 수 없습니다."),
  PROJECT_FULL_PEOPLE(HttpStatus.BAD_REQUEST, "PROJECT-400-2", "프로젝트 최대 정원에 도달해서 지원서를 승인할 수 없습니다."),
  PROJECT_TEAM_SIZE_INVALID(HttpStatus.BAD_REQUEST, "PROJECT-400-3",
      "프로젝트 최대 인원이 현재 인원보다 적을 수 없습니다."),
  PROJECT_NOT_RECRUITING(HttpStatus.BAD_REQUEST, "PROJECT-400-4", "모집 중인 프로젝트가 아닙니다."),
  PROJECT_TEAM_NOT_FULL(HttpStatus.BAD_REQUEST, "PROJECT-400-5", "정원이 다 차지 않았습니다."),
  PROJECT_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "PROJECT-400-6", "모집 완료된 프로젝트가 아닙니다."),
  PROJECT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "PROJECT-403", "해당 프로젝트에 대한 권한이 없습니다."),
  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT-404", "프로젝트를 찾을 수 없습니다."),

  // 지원서 관련 오류
  APPLICATION_ALREADY_ANALYZED(HttpStatus.BAD_REQUEST, "APPLICATION-400-1", "이미 분석된 결과가 있습니다."),
  APPLICATION_SAME_STATUS(HttpStatus.BAD_REQUEST, "APPLICATION-400-2",
      "현재 상태와 동일한 상태로 변경할 수 없습니다."),
  APPLICATION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "APPLICATION-400-3", "이미 해당 프로젝트에 지원하였습니다."),
  APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLICATION-404", "지원서를 찾을 수 없습니다."),

  // 분석 관련 오류
  ANALYSIS_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS-404", "분석 결과를 찾을 수 없습니다."),

  // 공통 오류
  CLIENT_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-400", "올바르지 않은 요청입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-500", "서버 내부 오류가 발생하였습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String resultCode;
  private final String message;
}