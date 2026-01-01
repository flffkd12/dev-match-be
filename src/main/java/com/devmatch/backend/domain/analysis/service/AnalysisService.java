package com.devmatch.backend.domain.analysis.service;

import com.devmatch.backend.domain.analysis.dto.Analysis;
import com.devmatch.backend.domain.application.dto.Skill;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.project.entity.Project;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalysisService {

  private final ChatModel chatModel;
  private final AnalysisPromptGenerator promptGenerator;

  public Analysis createAnalysis(Project project, List<Skill> applicantSkills) {
    String prompt = promptGenerator.generateAnalysisPrompt(project, applicantSkills);
    String aiResponse = chatModel.call(prompt);

    // 응답 디버깅
    System.out.println("AI 원본 응답: " + aiResponse);

    String[] parts = aiResponse.split("\\|");

    if (parts.length < 2) {
      System.err.println("AI 응답 파싱 실패 - parts 길이: " + parts.length);
      System.err.println("전체 프롬프트: " + prompt);
      throw new IllegalArgumentException("AI 응답 형식이 올바르지 않습니다. 응답: " + aiResponse);
    }

    BigDecimal score;
    try {
      score = new BigDecimal(parts[0].trim());

      if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("100")) > 0) {
        throw new IllegalArgumentException("점수는 0에서 100 사이여야 합니다. 받은 점수: " + score);
      }

      // 관대한 평가 권장 - 너무 낮은 점수일 경우 최소 점수로 조정
      if (score.compareTo(new BigDecimal("25")) < 0) {
        System.out.println("⚠️ AI가 너무 낮은 점수(" + score + ")를 부여했습니다. 팀 프로젝트 특성을 고려하여 최소 점수로 조정합니다.");
        score = new BigDecimal("45.00"); // 최소 45점으로 조정
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("점수 형식이 올바르지 않습니다. 응답: " + parts[0].trim(), e);
    }

    String reason = parts[1].trim();
    if (reason.isEmpty()) {
      throw new IllegalArgumentException("이유가 비어있습니다. 응답: " + aiResponse);
    }

    return new Analysis(score, reason);
  }

  public String createProjectRoleAssignment(
      Project project,
      List<Application> approvedApplications
  ) {
    String prompt = promptGenerator.generateRoleAssignmentPrompt(project, approvedApplications);
    return chatModel.call(prompt);
  }
}
