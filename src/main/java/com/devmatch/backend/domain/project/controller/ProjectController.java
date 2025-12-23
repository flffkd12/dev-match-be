package com.devmatch.backend.domain.project.controller;

import com.devmatch.backend.domain.auth.security.SecurityUser;
import com.devmatch.backend.domain.project.dto.ProjectCreateRequest;
import com.devmatch.backend.domain.project.dto.ProjectResponse;
import com.devmatch.backend.domain.project.dto.ProjectUpdateRequest;
import com.devmatch.backend.domain.project.service.ProjectService;
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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

  private final ProjectService projectService;

  @PostMapping
  public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
      @Valid @RequestBody ProjectCreateRequest projectCreateRequest,
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ApiResponse.success(SuccessCode.PROJECT_CREATE,
        projectService.createProject(securityUser.getUserId(), projectCreateRequest));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
    return ApiResponse.success(SuccessCode.PROJECT_FIND_ALL, projectService.getProjects());
  }

  @GetMapping("/my")
  public ResponseEntity<ApiResponse<List<ProjectResponse>>> getMyProjects(
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ApiResponse.success(SuccessCode.PROJECT_FIND_MINE,
        projectService.getProjectsByUserId(securityUser.getUserId()));
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long projectId) {
    return ApiResponse.success(SuccessCode.PROJECT_FIND_ONE, projectService.getProject(projectId));
  }

  @PatchMapping("/{projectId}")
  public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
      @PathVariable Long projectId,
      @Valid @RequestBody ProjectUpdateRequest projectUpdateRequest,
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ApiResponse.success(SuccessCode.PROJECT_UPDATE,
        projectService.updateProject(securityUser.getUserId(), projectId, projectUpdateRequest));
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<ApiResponse<Void>> deleteProject(
      @PathVariable Long projectId,
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    projectService.deleteProject(securityUser.getUserId(), projectId);
    return ApiResponse.success(SuccessCode.PROJECT_DELETE);
  }
}
