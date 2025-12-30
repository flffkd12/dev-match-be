package com.devmatch.backend.domain.analysis.controller;

import com.devmatch.backend.domain.analysis.dto.AnalysisResponse;
import com.devmatch.backend.domain.analysis.service.AnalysisService;
import com.devmatch.backend.global.response.ApiResponse;
import com.devmatch.backend.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

  private final AnalysisService analysisService;

  @PostMapping("/application/{applicationId}")
  public ResponseEntity<ApiResponse<AnalysisResponse>> createAnalysis(
      @PathVariable Long applicationId
  ) {
    return ApiResponse.success(SuccessCode.ANALYSIS_APPLICATION_ANALYZED,
        analysisService.createAnalysis(applicationId));
  }

  @PostMapping("/project/{projectId}")
  public ResponseEntity<ApiResponse<String>> createProjectRoleAssignment(
      @PathVariable Long projectId
  ) {
    return ApiResponse.success(SuccessCode.ANALYSIS_ROLE_ASSIGNED,
        analysisService.createProjectRoleAssignment(projectId));
  }

  @GetMapping("/application/{applicationId}")
  public ResponseEntity<ApiResponse<AnalysisResponse>> getAnalysis(
      @PathVariable Long applicationId
  ) {
    return ApiResponse.success(SuccessCode.ANALYSIS_FIND_ONE,
        analysisService.getAnalysis(applicationId));
  }
}
