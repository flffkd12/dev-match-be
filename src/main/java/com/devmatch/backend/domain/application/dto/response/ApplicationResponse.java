package com.devmatch.backend.domain.application.dto.response;

import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.entity.SkillScore;
import com.devmatch.backend.domain.application.enums.ApplicationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ApplicationResponse(
    Long applicationId,
    String nickname,
    ApplicationStatus status,
    LocalDateTime appliedAt,
    List<String> techStacks,
    List<Integer> techScores,
    BigDecimal compatibilityScore,
    String compatibilityReason
) {

  public static ApplicationResponse from(Application application) {
    return new ApplicationResponse(
        application.getId(),
        application.getApplicant().getNickname(),
        application.getStatus(),
        application.getCreatedAt(),
        application.getSkillScores().stream()
            .map(SkillScore::getTechStack)
            .toList(),
        application.getSkillScores().stream()
            .map(SkillScore::getTechScore)
            .toList(),
        application.getCompatibilityScore(),
        application.getCompatibilityReason()
    );
  }
}
