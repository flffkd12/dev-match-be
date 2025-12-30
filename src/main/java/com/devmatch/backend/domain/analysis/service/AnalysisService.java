package com.devmatch.backend.domain.analysis.service;

import com.devmatch.backend.domain.analysis.dto.Analysis;
import com.devmatch.backend.domain.application.dto.Skill;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.entity.SkillScore;
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

  public Analysis createAnalysis(Project project, List<Skill> applicantSkills) {
    StringBuilder prompt = new StringBuilder();
    prompt.append(
        "당신은 친화적이고 관대한 IT 프로젝트 전문 분석가입니다. 팀 프로젝트의 협업 가치를 중시하며, 지원자의 잠재력을 긍정적으로 평가해주세요.\n\n");

    prompt.append("프로젝트 정보:\n");
    prompt.append("- 프로젝트: ").append(project.getDescription()).append("\n");
    prompt.append("- 팀 규모: ").append(project.getTeamSize()).append("명 (역할 분담 가능)\n");
    prompt.append("- 프로젝트 기간: ").append(project.getDurationWeeks()).append("주 (학습 시간 충분)\n");
    prompt.append("- 필요 기술: ").append(project.getTechStacks()).append("\n\n");

    prompt.append("지원자 기술 역량:\n");
    for (Skill skill : applicantSkills) {
      prompt.append("- ").append(skill.techStack())
          .append(": ").append(skill.techStack()).append("/10점\n");
    }

    prompt.append("\n✨ 긍정적 평가 기준:\n");
    prompt.append("1. 🎯 전문 분야: 한 분야에 7점 이상이면 해당 분야 전문가로 인정\n");
    prompt.append("2. 🤝 팀워크: 프론트엔드 또는 백엔드 중 하나만 잘해도 충분히 기여 가능\n");
    prompt.append("3. 📚 성장성: 기본 점수(3-4점)도 팀 협업으로 빠른 성장 가능\n");
    prompt.append("4. 🔧 상호보완: 팀원들의 기술이 서로 보완되어 시너지 효과\n");
    prompt.append("5. 💡 학습력: 실제 프로젝트를 통한 실무 경험으로 급속 성장\n\n");

    prompt.append("🎉 관대한 점수 가이드라인 (팀 프로젝트 특성 반영):\n");
    prompt.append("85-100: 핵심 기술 전문가 - 팀을 리드하며 다른 팀원들을 가르칠 수 있음\n");
    prompt.append("70-84: 특정 분야 숙련자 - 자신의 전문 분야를 담당하며 안정적으로 기여\n");
    prompt.append("55-69: 기여 가능한 팀원 - 일부 기술에 능숙하여 특정 역할 담당 + 다른 분야 학습\n");
    prompt.append("40-54: 성장형 팀원 - 기본기가 있어 팀원들과 협업하며 빠르게 성장 가능\n");
    prompt.append("25-39: 학습 의지형 - 현재는 기초적이지만 프로젝트를 통해 실력 향상 기대\n");
    prompt.append("0-24: 현재로서는 참여 어려움 (매우 드문 경우)\n\n");

    prompt.append("💝 특별 고려사항:\n");
    prompt.append("- 프론트엔드 전문가(React/Vue 7점+): 백엔드를 모르더라도 75점 이상\n");
    prompt.append("- 백엔드 전문가(Java/Spring 7점+): 프론트엔드를 모르더라도 75점 이상\n");
    prompt.append("- 풀스택 지향(양쪽 5점+): 다재다능함으로 80점 이상\n");
    prompt.append("- 성장 의지 보이는 초보자도 최소 45점 이상 부여\n\n");

    prompt.append("🎯 응답 형식 (긍정적 평가로):\n");
    prompt.append("[점수]|[긍정적 이유]\n\n");
    prompt.append("📋 규칙:\n");
    prompt.append("1. 점수는 40.00-100.00 사이 (팀 프로젝트 특성상 대부분 40점 이상)\n");
    prompt.append("2. | 문자로 점수와 이유를 구분\n");
    prompt.append("3. 이유는 긍정적이고 구체적으로 (기여할 수 있는 부분 강조)\n");
    prompt.append("4. 다른 텍스트 절대 포함 금지\n\n");
    prompt.append("✨ 예시:\n");
    prompt.append("- 78.50|Java/Spring Boot 전문가로 백엔드 개발을 주도할 수 있으며, 팀원과 협업하여 프론트엔드도 학습 가능\n");
    prompt.append("- 72.00|React 숙련자로 프론트엔드 담당 가능, 백엔드 API 연동 경험으로 팀 협업에 유리\n");
    prompt.append("- 58.00|기본기가 탄탄하여 특정 분야 담당하며 다른 기술도 빠르게 습득 가능\n\n");
    prompt.append("응답:");

    String aiResponse = chatModel.call(prompt.toString());

    // 응답 디버깅
    System.out.println("AI 원본 응답: " + aiResponse);

    String[] parts = aiResponse.split("\\|");

    if (parts.length < 2) {
      System.err.println("AI 응답 파싱 실패 - parts 길이: " + parts.length);
      System.err.println("전체 프롬프트: " + prompt.toString());
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
    StringBuilder prompt = new StringBuilder();

    // 프로젝트 컨텍스트 분석
    prompt.append("🎯 프로젝트 분석 및 팀 역할 배분\n\n");
    prompt.append("📋 프로젝트 정보:\n");
    prompt.append("- 프로젝트: ").append(project.getDescription()).append("\n");
    prompt.append("- 팀 규모: ").append(project.getTeamSize()).append("명\n");
    prompt.append("- 프로젝트 기간: ").append(project.getDurationWeeks()).append("주\n\n");

    // 각 팀원의 기술 스택 점수 상세 분석
    prompt.append("👥 팀원 기술 역량 분석:\n");
    for (int i = 0; i < approvedApplications.size(); i++) {
      Application application = approvedApplications.get(i);
      prompt.append("팀원 ").append(i + 1).append(": ")
          .append(application.getApplicant().getNickname())
          .append("\n");

      List<SkillScore> skills = application.getSkillScores();
      for (SkillScore skill : skills) {
        prompt.append("  • ").append(skill.getTechStack())
            .append(": ").append(skill.getTechScore()).append("/10점\n");
      }
      prompt.append("\n");
    }

    // 🤖 AI 역할 분배 지침 (간결하게)
    prompt.append("🎯 각 팀원의 최고 점수 기술을 기준으로 역할을 배정하세요.\n\n");

    prompt.append("🚨 출력 규칙:\n");
    prompt.append("1. 한국어로만 응답\n");
    prompt.append("2. 서론/설명 없이 바로 결과만 출력\n");
    prompt.append("3. 형식: '팀원명 - 역할 | 이유'\n");
    prompt.append("4. 각 팀원마다 한 줄씩\n\n");

    prompt.append("역할 분배:");

    String aiResponse = chatModel.call(prompt.toString());

    // 응답 길이 제한 (데이터베이스 VARCHAR(255) 제약 준수)
    if (aiResponse.length() > 250) {
      aiResponse = aiResponse.substring(0, 250) + "...";
    }

    return aiResponse;
  }
}