package com.devmatch.backend.global.exception;

import com.devmatch.backend.global.response.ApiResponse;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    String logMessage = e.getDebugMessage() != null ?
        e.getMessage() + " [Debug: " + e.getDebugMessage() + "]" : e.getMessage();

    if (e.getErrorCode().getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
      log.error("handleCustomException: {}", logMessage, e);
    } else {
      log.warn("handleCustomException: {}", logMessage, e);
    }

    return ApiResponse.fail(e.getErrorCode());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e
  ) {
    log.warn("handleMethodArgumentNotValidException", e);
    String errorMessage = e.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> (FieldError) error)
        .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
        .collect(Collectors.joining("\n"));

    return ApiResponse.fail(ErrorCode.CLIENT_ERROR, errorMessage);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e
  ) {
    log.warn("handleHttpMessageNotReadableException", e);
    return ApiResponse.fail(ErrorCode.CLIENT_ERROR);
  }
}