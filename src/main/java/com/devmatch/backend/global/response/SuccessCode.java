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
  PROJECT_FIND_MINE(HttpStatus.OK, "PROJECT-200-2", "내 프로젝트 조회 성공"),
  PROJECT_FIND_ONE(HttpStatus.OK, "PROJECT-200-3", "프로젝트 단일 조회 성공"),
  PROJECT_UPDATE_STATUS(HttpStatus.OK, "PROJECT-200-4", "프로젝트 상태 수정 성공"),
  PROJECT_UPDATE_CONTENT(HttpStatus.OK, "PROJECT-200-5", "역할 배분 내용 수정 성공"),
  PROJECT_CREATE(HttpStatus.CREATED, "PROJECT-201", "프로젝트 생성 성공"),
  PROJECT_DELETE(HttpStatus.NO_CONTENT, "PROJECT-204", "프로젝트 삭제 성공"),
  ;

  private final HttpStatus httpStatus;
  private final String resultCode;
  private final String message;
}
