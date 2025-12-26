package com.devmatch.backend.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

  AUTH_LOGOUT(HttpStatus.OK, "AUTH-200", "로그아웃 성공"),

  USER_FETCH(HttpStatus.OK, "USER-200", "프로필 조회 성공"),

  PROJECT_FIND_ALL(HttpStatus.OK, "PROJECT-200-1", "프로젝트 전체 조회 성공"),
  PROJECT_FIND_MINE(HttpStatus.OK, "PROJECT-200-2", "내 프로젝트 목록 조회 성공"),
  PROJECT_FIND_ONE(HttpStatus.OK, "PROJECT-200-3", "프로젝트 단일 조회 성공"),
  PROJECT_UPDATE(HttpStatus.OK, "PROJECT-200-4", "프로젝트 수정 성공"),
  PROJECT_CREATE(HttpStatus.CREATED, "PROJECT-201", "프로젝트 생성 성공"),
  PROJECT_DELETE(HttpStatus.NO_CONTENT, "PROJECT-204", "프로젝트 삭제 성공"),

  APPLICATION_APPLIED_TO_PROJECT(HttpStatus.OK, "APPLICATION-200-1", "프로젝트의 지원서 목록 조회 성공"),
  APPLICATION_FIND_MINE(HttpStatus.OK, "APPLICATION-200-2", "내 지원서 목록 조회 성공"),
  APPLICATION_FIND_ONE(HttpStatus.OK, "APPLICATION-200-3", "지원서 단일 조회 성공"),
  APPLICATION_UPDATE_STATUS(HttpStatus.OK, "APPLICATION-200-4", "지원서 상태 업데이트 성공"),
  APPLICATION_CREATE(HttpStatus.CREATED, "APPLICATION-201", "지원서 생성 성공"),
  APPLICATION_DELETE(HttpStatus.NO_CONTENT, "APPLICATION-204", "지원서 삭제 성공"),
  ;

  private final HttpStatus httpStatus;
  private final String resultCode;
  private final String message;
}
