package com.devmatch.backend.domain.application.service;

import com.devmatch.backend.domain.application.dto.request.ApplicationCreateRequest;
import com.devmatch.backend.domain.application.dto.request.ApplicationCreateRequest.SkillRequest;
import com.devmatch.backend.domain.application.dto.response.ApplicationResponse;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.entity.SkillScore;
import com.devmatch.backend.domain.application.enums.ApplicationStatus;
import com.devmatch.backend.domain.application.repository.ApplicationRepository;
import com.devmatch.backend.domain.project.entity.Project;
import com.devmatch.backend.domain.project.enums.ProjectStatus;
import com.devmatch.backend.domain.project.service.ProjectService;
import com.devmatch.backend.domain.user.service.UserService;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

  private final ApplicationRepository applicationRepository;
  private final ProjectService projectService;
  private final UserService userService;

  public ApplicationResponse createApplication(
      Long userId,
      ApplicationCreateRequest applicationCreateRequest
  ) {
    Project project = projectService.findByProjectId(applicationCreateRequest.projectId());

    if (project.getStatus() != ProjectStatus.RECRUITING) {
      throw new CustomException(ErrorCode.PROJECT_NOT_RECRUITING);
    }

    if (applicationRepository.existsByApplicantIdAndProjectId(userId, project.getId())) {
      throw new CustomException(ErrorCode.APPLICATION_ALREADY_EXISTS);
    }

    Application application = Application.builder()
        .user(userService.getUser(userId))
        .project(project)
        .build();

    List<SkillScore> skillScores = new ArrayList<>();
    for (SkillRequest skill : applicationCreateRequest.skills()) {
      SkillScore score = SkillScore.builder()
          .application(application)
          .techName(skill.techStack())
          .score(skill.techScore())
          .build();

      skillScores.add(score);
    }

    application.setSkillScores(skillScores);
    return ApplicationResponse.from(applicationRepository.save(application));
  }

  public void updateApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
    Application application = findByApplicationId(applicationId);
    ApplicationStatus oldStatus = application.getStatus();

    if (oldStatus != ApplicationStatus.APPROVED && newStatus == ApplicationStatus.APPROVED) {
      application.getProject().increaseCurrentTeamSize();
    } else if (oldStatus == ApplicationStatus.APPROVED && newStatus != ApplicationStatus.APPROVED) {
      application.getProject().decreaseCurrentTeamSize();
    }

    application.updateStatus(newStatus);
  }

  public void deleteApplication(Long applicationId) {
    Application application = findByApplicationId(applicationId);

    if (application.getStatus() == ApplicationStatus.APPROVED) {
      application.getProject().decreaseCurrentTeamSize();
    }

    applicationRepository.delete(application);
  }

  @Transactional(readOnly = true)
  public List<ApplicationResponse> getApplicationsByProjectId(Long projectId) {
    return applicationRepository.findAllByProjectId(projectId).stream()
        .map(ApplicationResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ApplicationResponse> getApplicationsByApplicantId(Long applicantId) {
    return applicationRepository.findAllByApplicantId(applicantId).stream()
        .map(ApplicationResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public ApplicationResponse getApplication(Long applicationId) {
    return ApplicationResponse.from(findByApplicationId(applicationId));
  }

  @Transactional(readOnly = true)
  public Application findByApplicationId(Long applicationId) {
    return applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public List<Application> findByProjectIdAndStatus(Long projectId, ApplicationStatus status) {
    return applicationRepository.findByProjectIdAndStatus(projectId, status);
  }
}
