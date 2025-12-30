package com.devmatch.backend.domain.project.service;

import com.devmatch.backend.domain.analysis.service.AnalysisService;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.enums.ApplicationStatus;
import com.devmatch.backend.domain.application.service.ApplicationService;
import com.devmatch.backend.domain.project.dto.request.ProjectCreateRequest;
import com.devmatch.backend.domain.project.dto.request.ProjectUpdateRequest;
import com.devmatch.backend.domain.project.dto.response.ProjectResponse;
import com.devmatch.backend.domain.project.entity.Project;
import com.devmatch.backend.domain.project.repository.ProjectRepository;
import com.devmatch.backend.domain.user.service.UserService;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProjectService {

  private final AnalysisService analysisService;
  private final ApplicationService applicationService;
  private final UserService userService;
  private final ProjectRepository projectRepository;

  public ProjectResponse createProject(
      Long userId,
      ProjectCreateRequest projectCreateRequest
  ) {
    Project project = Project.builder()
        .title(projectCreateRequest.title())
        .description(projectCreateRequest.description())
        .techStacks(projectCreateRequest.techStacks())
        .teamSize(projectCreateRequest.teamSize())
        .creator(userService.findByUserId(userId))
        .durationWeeks(projectCreateRequest.durationWeeks())
        .build();

    return ProjectResponse.from(projectRepository.save(project));
  }

  public ProjectResponse createProjectRoleAssignment(Long creatorId, Long projectId) {
    Project project = findByProjectId(projectId);
    List<Application> approvedApplications = applicationService.findByProjectIdAndStatus(
        projectId,
        ApplicationStatus.APPROVED
    );

    validateRoleAssignmentCreation(project, approvedApplications, creatorId);

    String roleAssignment = analysisService.createProjectRoleAssignment(project);
    project.allocateRole(roleAssignment);
    return ProjectResponse.from(project);
  }

  public ProjectResponse updateProject(
      Long creatorId,
      Long projectId,
      ProjectUpdateRequest projectUpdateRequest
  ) {
    Project project = findByProjectId(projectId);
    validateProjectOwner(project, creatorId);
    project.update(
        projectUpdateRequest.title(),
        projectUpdateRequest.description(),
        projectUpdateRequest.techStacks(),
        projectUpdateRequest.teamSize(),
        projectUpdateRequest.durationWeeks(),
        projectUpdateRequest.roleAssignment()
    );

    return ProjectResponse.from(project);
  }

  public void deleteProject(Long creatorId, Long projectId) {
    Project project = findByProjectId(projectId);
    validateProjectOwner(project, creatorId);
    projectRepository.delete(project);
  }

  @Transactional(readOnly = true)
  public Page<ProjectResponse> getProjects(Pageable pageable) {
    return projectRepository.findAll(pageable)
        .map(ProjectResponse::from);
  }

  @Transactional(readOnly = true)
  public Page<ProjectResponse> getProjectsByUserId(Long userId, Pageable pageable) {
    return projectRepository.findAllByCreatorId(userId, pageable)
        .map(ProjectResponse::from);
  }

  @Transactional(readOnly = true)
  public ProjectResponse getProject(Long projectId) {
    return ProjectResponse.from(findByProjectId(projectId));
  }

  @Transactional(readOnly = true)
  public Project findByProjectId(Long projectId) {
    return projectRepository.findById(projectId)
        .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
  }

  private void validateProjectOwner(Project project, Long creatorId) {
    if (!project.getCreator().getId().equals(creatorId)) {
      throw new CustomException(ErrorCode.PROJECT_ACCESS_DENIED);
    }
  }

  private void validateRoleAssignmentCreation(
      Project project,
      List<Application> approvedApplications,
      Long creatorId
  ) {
    validateProjectOwner(project, creatorId);

    if (approvedApplications.size() != project.getTeamSize()) {
      throw new CustomException(ErrorCode.PROJECT_TEAM_NOT_FULL);
    }
  }
}
