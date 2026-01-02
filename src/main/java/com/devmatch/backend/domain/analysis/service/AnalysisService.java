package com.devmatch.backend.domain.analysis.service;

import com.devmatch.backend.domain.analysis.dto.Analysis;
import com.devmatch.backend.domain.application.dto.Skill;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.project.entity.Project;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AnalysisService {

  private static final BigDecimal MAX_SCORE = BigDecimal.valueOf(100);
  private static final BigDecimal MIN_SCORE = BigDecimal.ZERO;

  private final ChatModel chatModel;
  private final AnalysisPromptGenerator promptGenerator;

  public Analysis createAnalysis(Project project, List<Skill> applicantSkills) {
    String prompt = promptGenerator.generateAnalysisPrompt(project, applicantSkills);
    log.debug("지원서 분석 전체 프롬프트: {}", prompt);
    return aiResponseToAnalysis(chatModel.call(prompt));
  }

  public String createProjectRoleAssignment(
      Project project,
      List<Application> approvedApplications
  ) {
    String prompt = promptGenerator.generateRoleAssignmentPrompt(project, approvedApplications);
    log.debug("역할 분석 전체 프롬프트: {}", prompt);
    return chatModel.call(prompt);
  }

  private Analysis aiResponseToAnalysis(String aiResponse) {
    log.debug("AI 응답 원본: {}", aiResponse);

    String[] parts = aiResponse.split(" \\| ");
    if (parts.length != 2) {
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR,
          "AI 응답 파싱 결과가 올바르지 않습니다. 기대값: 2, 실제 파싱 개수: " + parts.length + " 원본: " + aiResponse);
    }

    BigDecimal score;
    try {
      score = new BigDecimal(parts[0].trim());
      if (score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR,
            "점수는 0점 이상, 100점 이하여야 합니다. 현 점수: " + score);
      }
    } catch (NumberFormatException e) {
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR,
          "점수 형식이 올바르지 않습니다. 점수: " + parts[0].trim());
    }

    String reason = parts[1].trim();
    if (reason.isEmpty()) {
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "AI가 분석 사유를 생성하지 못했습니다.");
    }

    return Analysis.builder()
        .compatibilityScore(score)
        .compatibilityReason(reason)
        .build();
  }
}
