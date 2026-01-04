package com.devmatch.backend.domain.analysis.service;

import com.devmatch.backend.domain.application.dto.Skill;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.entity.SkillScore;
import com.devmatch.backend.domain.project.entity.Project;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
class AnalysisPromptGenerator {

  String generateAnalysisPrompt(Project project, List<Skill> applicantSkills) {
    String projectInfo = formatProjectInfo(project);
    String applicantSkillsInfo = formatApplicantSkills(applicantSkills);

    return """
        당신은 친화적이고 관대한 IT 프로젝트 전문 분석가입니다. 팀 프로젝트의 협업 가치를 중시하며, 지원자의 잠재력을 긍정적으로 평가해주세요.
        
        프로젝트 정보:
        %s
        
        지원자 기술 역량:
        %s
        
        ✨ 긍정적 평가 기준:
        1. 🎯 전문 분야: 한 분야에 7점 이상이면 해당 분야 전문가로 인정
        2. 🤝 팀워크: 프론트엔드 또는 백엔드 중 하나만 잘해도 충분히 기여 가능
        3. 📚 성장성: 6점 이상이면 팀 협업으로 빠른 성장 가능
        4. 🔧 상호보완: 팀원들의 기술이 서로 보완되어 시너지 효과
        5. 💡 학습력: 실제 프로젝트를 통한 실무 경험으로 급속 성장
        
        🎉 관대한 점수 가이드라인 (팀 프로젝트 특성 반영):
        85-100: 핵심 기술 전문가 - 팀을 리드하며 다른 팀원들을 가르칠 수 있음
        70-84: 특정 분야 숙련자 - 자신의 전문 분야를 담당하며 안정적으로 기여
        55-69: 기여 가능한 팀원 - 일부 기술에 능숙하여 특정 역할 담당 + 다른 분야 학습
        40-54: 성장형 팀원 - 기본기가 있어 팀원들과 협업하며 빠르게 성장 가능
        25-39: 학습 의지형 - 현재는 기초적이지만 프로젝트를 통해 실력 향상 기대
        0-24: 현재로서는 참여 어려움
        
        💝 특별 고려사항:
        - 프론트엔드 전문가(React/Vue 7점+): 백엔드를 모르더라도 75점 이상
        - 백엔드 전문가(Java/Spring 7점+): 프론트엔드를 모르더라도 75점 이상
        - 풀스택 지향(양쪽 5점+): 다재다능함으로 80점 이상
        - 성장 의지 보이는 초보자도 최소 45점 이상 부여
        
        🎯 응답 형식:
        [점수] | [이유]
        
        📋 규칙:
        1. 점수는 40.00-100.00 사이 (팀 프로젝트 특성상 대부분 40점 이상)
        2. " | " 를 기준으로 점수와 이유를 구분
        3. 이유는 긍정적이고 구체적으로 (기여할 수 있는 부분 강조)
        4. 다른 텍스트 절대 포함 금지
        
        ✨ 예시:
        - 78.50 | Java / Spring Boot 전문가로 백엔드 개발을 주도할 수 있으며, 팀원과 협업하여 프론트엔드도 학습 가능
        - 72.00 | React 숙련자로 프론트엔드 담당 가능, 백엔드 API 연동 경험으로 팀 협업에 유리
        - 58.00 | 기본기가 탄탄하여 특정 분야 담당하며 다른 기술도 빠르게 습득 가능
        
        응답:""".formatted(projectInfo, applicantSkillsInfo);
  }

  String generateRoleAssignmentPrompt(
      Project project,
      List<Application> approvedApplications
  ) {
    String projectInfo = formatProjectInfo(project);
    String teamInfo = formatTeamSkills(approvedApplications);

    return """
        🎯 프로젝트 분석 및 팀 역할 배분
        
        📋 프로젝트 정보:
        %s
        
        👥 팀원 기술 역량 분석:
        %s
        
        🤖 AI 역할 분배 지침
        🎯 각 팀원의 최고 점수 기술을 기준으로 역할을 배정하세요.
        
        🚨 출력 규칙:
        1. 한국어로만 응답
        2. 서론/설명 없이 바로 결과만 출력
        3. 형식: '팀원명 - 역할 | 이유'
        4. 각 팀원마다 한 줄씩
        
        역할 분배:""".formatted(projectInfo, teamInfo);
  }

  private String formatProjectInfo(Project project) {
    return """
        - 프로젝트: %s
        - 팀 규모: %d명 (역할 분담 가능)
        - 프로젝트 기간: %d주
        - 필요 기술: %s""".formatted(
        project.getDescription(),
        project.getTeamSize(),
        project.getDurationWeeks(),
        project.getTechStacks()
    );
  }

  private String formatApplicantSkills(List<Skill> skills) {
    return skills.stream()
        .map(skill -> "%s: %d/10점".formatted(skill.techStack(), skill.techScore()))
        .collect(Collectors.joining("\n"));
  }

  private String formatTeamSkills(List<Application> applications) {
    StringBuilder sb = new StringBuilder();
    for (Application application : applications) {
      sb.append("팀원: ")
          .append(application.getApplicant().getNickname())
          .append("\n");

      for (SkillScore skill : application.getSkillScores()) {
        sb.append(skill.getTechStack())
            .append(": ")
            .append(skill.getTechScore()).append("/10점\n");
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}
