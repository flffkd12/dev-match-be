package com.devmatch.backend.domain.project.facade;

import com.devmatch.backend.domain.analysis.dto.Analysis;
import com.devmatch.backend.domain.analysis.service.AnalysisService;
import com.devmatch.backend.domain.application.dto.request.ApplicationCreateRequest;
import com.devmatch.backend.domain.application.dto.response.ApplicationResponse;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.enums.ApplicationStatus;
import com.devmatch.backend.domain.application.service.ApplicationService;
import com.devmatch.backend.domain.project.dto.response.ProjectResponse;
import com.devmatch.backend.domain.project.entity.Project;
import com.devmatch.backend.domain.project.service.ProjectService;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectRecruitmentFacade {

  private final AnalysisService analysisService;
  private final ApplicationService applicationService;
  private final ProjectService projectService;
  private final UserService userService;

  public ApplicationResponse createApplication(Long applicantId, ApplicationCreateRequest request) {
    User applicant = userService.findByUserId(applicantId);
    Project project = projectService.findByProjectId(request.projectId());
    Analysis analysis = analysisService.createAnalysis(project, request.skills());
    return applicationService.createApplication(applicant, project, request.skills(), analysis);
  }

  public ProjectResponse createProjectRoleAssignment(Long creatorId, Long projectId) {
    Project project = projectService.findByProjectId(projectId);
    List<Application> approvedApplications = applicationService.findByProjectIdAndStatus(
        projectId, ApplicationStatus.APPROVED);
    String roleAssignment = analysisService.createProjectRoleAssignment(project,
        approvedApplications);
    return projectService.createProjectRoleAssignment(creatorId, project, approvedApplications,
        roleAssignment);
  }
}
