package com.devmatch.backend.domain.project.controller;

import com.devmatch.backend.domain.auth.security.SecurityUser;
import com.devmatch.backend.domain.project.dto.ProjectContentUpdateRequest;
import com.devmatch.backend.domain.project.dto.ProjectCreateRequest;
import com.devmatch.backend.domain.project.dto.ProjectResponse;
import com.devmatch.backend.domain.project.dto.ProjectStatusUpdateRequest;
import com.devmatch.backend.domain.project.service.ProjectService;
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
    return ResponseEntity.ok(ApiResponse.success("프로젝트 생성 성공",
        projectService.createProject(securityUser.getUserId(), projectCreateRequest)));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
    return ResponseEntity.ok(ApiResponse.success("프로젝트 전체 조회 성공",
        projectService.getProjects()));
  }

  @GetMapping("/my")
  public ResponseEntity<List<ProjectResponse>> getMyProjects(
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(projectService.getProjectsByUserId(securityUser.getUserId()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.success("프로젝트 단일 조회 성공",
        projectService.getProject(id)));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<ApiResponse<ProjectResponse>> modifyProjectStatus(
      @PathVariable Long id,
      @Valid @RequestBody ProjectStatusUpdateRequest projectStatusUpdateRequest
  ) {
    return ResponseEntity.ok(ApiResponse.success("프로젝트 상태 수정 성공",
        projectService.modifyStatus(id, projectStatusUpdateRequest.status())));
  }

  @PatchMapping("/{id}/content")
  public ResponseEntity<ApiResponse<ProjectResponse>> modifyProjectRoleAssignment(
      @PathVariable Long id,
      @Valid @RequestBody ProjectContentUpdateRequest projectContentUpdateRequest
  ) {
    return ResponseEntity.ok(ApiResponse.success("역할 배분 내용 수정 성공",
        projectService.modifyRoleAssignment(id, projectContentUpdateRequest.content())));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
    projectService.deleteProject(id);
    return ResponseEntity.noContent().build();
  }
}
