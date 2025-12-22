package com.devmatch.backend.domain.project.service;

import com.devmatch.backend.domain.project.dto.ProjectCreateRequest;
import com.devmatch.backend.domain.project.dto.ProjectResponse;
import com.devmatch.backend.domain.project.entity.Project;
import com.devmatch.backend.domain.project.enums.ProjectStatus;
import com.devmatch.backend.domain.project.repository.ProjectRepository;
import com.devmatch.backend.domain.user.service.UserService;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProjectService {

  private final UserService userService;
  private final ProjectRepository projectRepository;

  public ProjectResponse createProject(
      Long userId,
      ProjectCreateRequest projectCreateRequest
  ) {
    if (!projectCreateRequest.techStack().matches("^([\\w .+#-]+)(, [\\w .+#-]+)*$")) {
      throw new IllegalArgumentException("기술 스택 기재 형식이 올바르지 않습니다. \", \"로 구분해주세요");
    }

    Project project = Project.builder()
        .title(projectCreateRequest.title())
        .description(projectCreateRequest.description())
        .techStack(projectCreateRequest.techStack())
        .teamSize(projectCreateRequest.teamSize())
        .creator(userService.getUser(userId))
        .durationWeeks(projectCreateRequest.durationWeeks())
        .build();

    return ProjectResponse.from(projectRepository.save(project));
  }

  public ProjectResponse modifyStatus(Long projectId, ProjectStatus status) {
    Project project = findByProjectId(projectId);
    project.changeStatus(status);

    return ProjectResponse.from(project);
  }

  public ProjectResponse modifyContent(Long projectId, String content) {
    Project project = findByProjectId(projectId);
    project.updateRoleAssignment(content);

    return ProjectResponse.from(project);
  }

  public void deleteProject(Long projectId) {
    getProject(projectId);
    projectRepository.deleteById(projectId);
  }

  @Transactional(readOnly = true)
  public List<ProjectResponse> getProjects() {
    return projectRepository.findAll()
        .stream()
        .map(ProjectResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ProjectResponse> getProjectsByUserId(Long userId) {
    return projectRepository.findAllByCreatorId(userId)
        .stream()
        .map(ProjectResponse::from)
        .toList();
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
}
