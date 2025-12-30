package com.devmatch.backend.domain.analysis.controller;

import com.devmatch.backend.domain.analysis.service.AnalysisService;
import com.devmatch.backend.global.response.ApiResponse;
import com.devmatch.backend.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

  private final AnalysisService analysisService;

  @PostMapping("/project/{projectId}")
  public ResponseEntity<ApiResponse<String>> createProjectRoleAssignment(
      @PathVariable Long projectId
  ) {
    return ApiResponse.success(SuccessCode.ANALYSIS_ROLE_ASSIGNED,
        analysisService.createProjectRoleAssignment(projectId));
  }
}
