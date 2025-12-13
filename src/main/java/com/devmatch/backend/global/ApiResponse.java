package com.devmatch.backend.global;

import lombok.Builder;

@Builder
public record ApiResponse<T>(Boolean success, String resultCode, String message, T content) {

  public ApiResponse(String msg) {
    this(msg, null);
  }

  public static <T> ApiResponse<T> fail(String resultCode, String message) {
    return ApiResponse.<T>builder()
        .success(false)
        .resultCode(resultCode)
        .message(message)
        .content(null)
        .build();
  }
}
