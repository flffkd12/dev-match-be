package com.devmatch.backend.domain.user.controller;

import com.devmatch.backend.domain.application.dto.response.ApplicationDetailResponseDto;
import com.devmatch.backend.domain.application.service.ApplicationService;
import com.devmatch.backend.domain.auth.security.SecurityUser;
import com.devmatch.backend.domain.project.dto.ProjectDetailResponse;
import com.devmatch.backend.domain.project.service.ProjectService;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final ProjectService projectService;
  private final ApplicationService applicationService;
  private final UserService userService;

  @GetMapping("/profile")
  public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal SecurityUser securityUser) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(securityUser.getUserId()));
  }

  @GetMapping("/projects")
  public ResponseEntity<List<ProjectDetailResponse>> findProjectsById(
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(projectService.getProjectsByUserId(securityUser.getUserId()));
  }

  @GetMapping("/applications")
  public ResponseEntity<List<ApplicationDetailResponseDto>> findApplicationsById(
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(applicationService.getApplicationsByUserId(securityUser.getUserId()));
  }
}