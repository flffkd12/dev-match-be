package com.devmatch.backend.domain.application.controller;

import com.devmatch.backend.domain.application.dto.request.ApplicationStatusUpdateRequestDto;
import com.devmatch.backend.domain.application.dto.request.ProjectApplyRequest;
import com.devmatch.backend.domain.application.dto.response.ApplicationResponse;
import com.devmatch.backend.domain.application.service.ApplicationService;
import com.devmatch.backend.domain.auth.security.SecurityUser;
import com.devmatch.backend.global.response.ApiResponse;
import com.devmatch.backend.global.response.SuccessCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<ApiResponse<ApplicationResponse>> createApplication(
      @AuthenticationPrincipal SecurityUser securityUser,
      @Valid @RequestBody ProjectApplyRequest projectApplyRequest
  ) {
    return ApiResponse.success(SuccessCode.APPLICATION_CREATE,
        applicationService.createApplication(securityUser.getUserId(), projectApplyRequest));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getApplications(
      @RequestParam Long projectId
  ) {
    return ApiResponse.success(SuccessCode.APPLICATION_APPLIED_TO_PROJECT,
        applicationService.getApplicationsByProjectId(projectId));
  }

  @GetMapping("/my")
  public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getMyApplications(
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ApiResponse.success(SuccessCode.APPLICATION_FIND_MINE,
        applicationService.getApplicationsByApplicantId(securityUser.getUserId()));
  }

  @GetMapping("/{applicationId}")
  public ResponseEntity<ApiResponse<ApplicationResponse>> getApplication(
      @PathVariable Long applicationId
  ) {
    return ApiResponse.success(SuccessCode.APPLICATION_FIND_ONE,
        applicationService.getApplication(applicationId));
  }

  @PatchMapping("/{applicationId}/status")
  public ResponseEntity<ApiResponse<Void>> updateApplicationStatus(
      @PathVariable Long applicationId,
      @Valid @RequestBody ApplicationStatusUpdateRequestDto reqBody
  ) {
    applicationService.updateApplicationStatus(applicationId, reqBody.status());
    return ApiResponse.success(SuccessCode.APPLICATION_UPDATE_STATUS);
  }

  @DeleteMapping("/{applicationId}")
  public ResponseEntity<ApiResponse<Void>> deleteApplication(@PathVariable Long applicationId) {
    applicationService.deleteApplication(applicationId);
    return ApiResponse.success(SuccessCode.APPLICATION_DELETE);
  }
}
