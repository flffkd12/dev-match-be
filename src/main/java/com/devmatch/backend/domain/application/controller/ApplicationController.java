package com.devmatch.backend.domain.application.controller;

import com.devmatch.backend.domain.application.dto.request.ApplicationStatusUpdateRequestDto;
import com.devmatch.backend.domain.application.dto.response.ApplicationDetailResponseDto;
import com.devmatch.backend.domain.application.service.ApplicationService;
import com.devmatch.backend.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

  private final ApplicationService applicationService;

  /**
   * 지원서 상세 조회 API
   *
   * @param id 지원서 ID
   * @return 지원서 상세 정보
   */
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ApplicationDetailResponseDto>> getApplicationDetail(
      @PathVariable Long id
  ) {
    ApplicationDetailResponseDto applicationDetailResponseDto =
        applicationService.getApplicationDetail(id);
    return ResponseEntity.ok(ApiResponse.success("지원서의 상세 정보 조회 성공", applicationDetailResponseDto));
  }

  /**
   * 지원서 삭제 API
   *
   * @param id 지원서 ID
   * @return 지원서 삭제 성공 메시지
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteApplication(
      @PathVariable Long id
  ) {
    applicationService.deleteApplication(id);
    return ResponseEntity.ok(ApiResponse.success("지원서의 삭제 성공"));
  }

  /**
   * 지원서 상태 업데이트 API
   *
   * @param id      지원서 ID
   * @param reqBody 상태 업데이트 요청 DTO
   * @return 상태 업데이트 성공 메시지
   */
  @PatchMapping("/{id}/status")
  public ResponseEntity<ApiResponse<String>> updateApplicationStatus(
      @PathVariable Long id,
      @Valid @RequestBody ApplicationStatusUpdateRequestDto reqBody
  ) {
    applicationService.updateApplicationStatus(id, reqBody);
    return ResponseEntity.ok(ApiResponse.success("지원서 상태 업데이트 성공"));
  }
}