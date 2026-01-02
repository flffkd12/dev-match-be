package com.devmatch.backend.global.response;

import com.devmatch.backend.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
@JsonInclude(Include.NON_NULL)
public record ApiResponse<T>(String resultCode, String message, T content) {

  public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode successCode) {
    return success(successCode, null);
  }

  public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode successCode, T content) {
    return ResponseEntity.status(successCode.getHttpStatus()).body(
        ApiResponse.<T>builder()
            .resultCode(successCode.getResultCode())
            .message(successCode.getMessage())
            .content(content)
            .build()
    );
  }

  public static <T> ResponseEntity<ApiResponse<T>> fail(ErrorCode errorCode) {
    return fail(errorCode, null);
  }

  public static <T> ResponseEntity<ApiResponse<T>> fail(ErrorCode errorCode, String errorMessage) {
    return ResponseEntity.status(errorCode.getHttpStatus()).body(
        ApiResponse.<T>builder()
            .resultCode(errorCode.getResultCode())
            .message(errorMessage != null ? errorMessage : errorCode.getMessage())
            .content(null)
            .build()
    );
  }
}
