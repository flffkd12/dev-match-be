package com.devmatch.backend.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String debugMessage;

  public CustomException(ErrorCode errorCode) {
    this(errorCode, null);
  }

  public CustomException(ErrorCode errorCode, String debugMessage) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.debugMessage = debugMessage;
  }
}