package com.devmatch.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final String resultCode;
  private final String message;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.httpStatus = errorCode.getHttpStatus();
    this.resultCode = errorCode.getResultCode();
    this.message = errorCode.getMessage();
  }
}