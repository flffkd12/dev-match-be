package com.devmatch.backend.domain.application.dto.response;

import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.entity.SkillScore;
import com.devmatch.backend.domain.application.enums.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ApplicationResponse(
    Long applicationId,
    String nickname,
    ApplicationStatus status,
    LocalDateTime appliedAt,
    List<String> techStacks,
    List<Integer> techScores
) {

  public static ApplicationResponse from(Application application) {
    return new ApplicationResponse(
        application.getId(),
        application.getApplicant().getNickname(),
        application.getStatus(),
        application.getCreatedAt(),
        application.getSkillScore().stream()
            .map(SkillScore::getTechStack)
            .toList(),
        application.getSkillScore().stream()
            .map(SkillScore::getTechScore)
            .toList()
    );
  }
}