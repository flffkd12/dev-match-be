package com.devmatch.backend.global.exception;

import com.devmatch.backend.global.ApiResponse;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
    log.warn("handleCustomException: {}", e.getMessage(), e);

    ApiResponse<Void> response = ApiResponse.fail(e.getResultCode(), e.getMessage());
    return ResponseEntity.status(e.getHttpStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e
  ) {
    log.warn("handleMethodArgumentNotValidException", e);
    String message = e.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> (FieldError) error)
        .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
        .collect(Collectors.joining("\n"));

    ApiResponse<Void> response = ApiResponse.fail(ErrorCode.CLIENT_ERROR.getResultCode(), message);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
    log.warn("handleAccessDeniedException", e);
    ApiResponse<Void> response = ApiResponse.fail("AUTH-403", "접근 권한이 없습니다");
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e
  ) {
    log.warn("handleHttpMessageNotReadableException", e);
    ApiResponse<Void> response = ApiResponse.fail(ErrorCode.CLIENT_ERROR.getResultCode(),
        e.getMostSpecificCause().getMessage());
    return ResponseEntity.badRequest().body(response);
  }
}