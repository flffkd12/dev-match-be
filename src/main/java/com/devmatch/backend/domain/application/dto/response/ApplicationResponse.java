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
    List<String> techName,    // 지원자의 기술명
    List<Integer> score       // 지원자의 기술 점수
) {

  public static ApplicationResponse from(Application application) {
    return new ApplicationResponse(
        application.getId(),
        application.getApplicant().getNickname(),
        application.getStatus(),
        application.getCreatedAt(),
        application.getSkillScore().stream()
            .map(SkillScore::getTechName)
            .toList(),
        application.getSkillScore().stream()
            .map(SkillScore::getScore)
            .toList()
    );
  }
}