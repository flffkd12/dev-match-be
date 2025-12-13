package com.devmatch.backend.global.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.devmatch.backend.global.ApiResponse;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
    log.info(e.getMessage(), e);
    ApiResponse<Void> response = ApiResponse.fail(
        e.getResultCode(),
        e.getMessage()
    );
    return ResponseEntity.status(e.getHttpStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handle(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> (FieldError) error)
        .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
        .collect(Collectors.joining("\n"));

    return ResponseEntity.badRequest().body(new ApiResponse<>(message));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex
  ) {
    return ResponseEntity.badRequest()
        .body(new ApiResponse<>("요청 값이 올바르지 않습니다: " + ex.getMostSpecificCause().getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handle(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(new ApiResponse<>(ex.getMessage()));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiResponse<Void>> handle(NoSuchElementException ex) {
    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse<>(ex.getMessage()));
  }
}