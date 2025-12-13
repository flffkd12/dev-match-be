package com.devmatch.backend.global;

import lombok.Builder;

@Builder
public record ApiResponse<T>(String resultCode, String message, T content) {

  private final static String SUCCESS_CODE = "200";

  public static <T> ApiResponse<T> success(String message) {
    return success(message, null);
  }

  public static <T> ApiResponse<T> success(String message, T content) {
    return ApiResponse.<T>builder()
        .resultCode(SUCCESS_CODE)
        .message(message)
        .content(content)
        .build();
  }

  public static <T> ApiResponse<T> fail(String resultCode, String message) {
    return ApiResponse.<T>builder()
        .resultCode(resultCode)
        .message(message)
        .content(null)
        .build();
  }
}
