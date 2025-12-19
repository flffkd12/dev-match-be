package com.devmatch.backend.domain.project.mapper;

import com.devmatch.backend.domain.project.dto.ProjectResponse;
import com.devmatch.backend.domain.project.entity.Project;
import java.util.Arrays;

public class ProjectMapper {

  public static ProjectResponse toProjectDetailResponse(Project project) {
    return new ProjectResponse(
        project.getId(),
        project.getTitle(),
        project.getDescription(),
        Arrays.stream(project.getTechStack().split(", ")).toList(),
        project.getTeamSize(),
        project.getCurrentTeamSize(),
        project.getCreator().getNickname(),
        project.getStatus().name(),
        project.getRoleAssignment(),
        project.getDurationWeeks(),
        project.getCreatedAt()
    );
  }
}
