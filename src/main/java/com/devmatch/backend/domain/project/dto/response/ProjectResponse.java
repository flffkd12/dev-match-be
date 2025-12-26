package com.devmatch.backend.domain.project.dto.response;

import com.devmatch.backend.domain.project.entity.Project;
import com.devmatch.backend.domain.project.enums.ProjectStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse(
    Long projectId,
    String creator,
    String title,
    String description,
    List<String> techStacks,
    Integer teamSize,
    Integer currentTeamSize,
    Integer durationWeeks,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    ProjectStatus projectStatus,
    String roleAssignment
) {

  public static ProjectResponse from(Project project) {
    return new ProjectResponse(
        project.getId(),
        project.getCreator().getNickname(),
        project.getTitle(),
        project.getDescription(),
        project.getTechStacks(),
        project.getTeamSize(),
        project.getCurrentTeamSize(),
        project.getDurationWeeks(),
        project.getCreatedAt(),
        project.getUpdatedAt(),
        project.getStatus(),
        project.getRoleAssignment()
    );
  }
}