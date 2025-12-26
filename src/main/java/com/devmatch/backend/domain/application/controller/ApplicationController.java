package com.devmatch.backend.domain.application.controller;

import com.devmatch.backend.domain.application.dto.request.ApplicationStatusUpdateRequestDto;
import com.devmatch.backend.domain.application.dto.request.ProjectApplyRequest;
import com.devmatch.backend.domain.application.dto.response.ApplicationResponse;
import com.devmatch.backend.domain.application.service.ApplicationService;
import com.devmatch.backend.domain.auth.security.SecurityUser;
import com.devmatch.backend.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

  private final ApplicationService applicationService;

  @PostMapping
  public ResponseEntity<ApiResponse<ApplicationResponse>> apply(
      @AuthenticationPrincipal SecurityUser securityUser,
      @Valid @RequestBody ProjectApplyRequest projectApplyRequest
  ) {
    return ResponseEntity.ok(ApiResponse.success("지원서 생성 성공",
        applicationService.createApplication(securityUser.getUserId(), projectApplyRequest)));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getApplications(
      @RequestParam Long projectId
  ) {
    return ResponseEntity.ok(ApiResponse.success("프로젝트의 지원서 목록 조회 성공",
        applicationService.getApplicationsByProjectId(projectId)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ApplicationResponse>> getApplicationDetail(
      @PathVariable Long id
  ) {
    ApplicationResponse applicationResponse =
        applicationService.getApplication(id);
    return ResponseEntity.ok(ApiResponse.success("지원서의 상세 정보 조회 성공", applicationResponse));
  }

  @GetMapping("/my")
  public ResponseEntity<List<ApplicationResponse>> getMyApplications(
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(applicationService.getApplicationsByUserId(securityUser.getUserId()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteApplication(
      @PathVariable Long id
  ) {
    applicationService.deleteApplication(id);
    return ResponseEntity.ok(ApiResponse.success("지원서의 삭제 성공"));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<ApiResponse<String>> updateApplicationStatus(
      @PathVariable Long id,
      @Valid @RequestBody ApplicationStatusUpdateRequestDto reqBody
  ) {
    applicationService.updateApplicationStatus(id, reqBody.status());
    return ResponseEntity.ok(ApiResponse.success("지원서 상태 업데이트 성공"));
  }
}
