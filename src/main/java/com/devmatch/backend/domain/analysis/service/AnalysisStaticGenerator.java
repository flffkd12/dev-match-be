package com.devmatch.backend.domain.analysis.service;

import com.devmatch.backend.domain.application.dto.Skill;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.entity.SkillScore;
import com.devmatch.backend.domain.project.entity.Project;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class AnalysisStaticGenerator {

  String generateApplicationAnalysis(Project project, List<Skill> applicantSkills) {
    List<String> projectTechs = project.getTechStacks();
    List<String> matchedSkills = applicantSkills.stream()
        .map(Skill::techStack)
        .filter(skill -> projectTechs.stream()
            .anyMatch(projectTech -> projectTech.equalsIgnoreCase(skill)))
        .toList();

    int score;
    StringBuilder reason = new StringBuilder();

    if (matchedSkills.isEmpty()) {
      score = 40;
      reason.append("프로젝트에서 요구하는 기술 스택과 일치하는 항목이 없습니다.");
    } else {
      double matchRate = (double) matchedSkills.size() / projectTechs.size();
      score = 50 + (int) (matchRate * 40);
      reason.append("프로젝트 요구 기술 중 [")
          .append(String.join(", ", matchedSkills))
          .append("]에 대한 역량을 보유하고 있어 긍정적입니다.");
    }

    reason.append("\n시스템 문제로 간이 분석 결과가 제공되었습니다. 상세 검토를 권장합니다.");

    return score + " | " + reason;
  }

  String generateProjectRoleAssignment(List<Application> approvedApplications) {
    StringBuilder result = new StringBuilder();

    for (Application app : approvedApplications) {
      String nickname = app.getApplicant().getNickname();
      List<SkillScore> scores = app.getSkillScores();

      int beScore = 0, feScore = 0, infraScore = 0;
      List<String> beTechs = new ArrayList<>();
      List<String> feTechs = new ArrayList<>();
      List<String> infraTechs = new ArrayList<>();

      for (SkillScore ss : scores) {
        String tech = ss.getTechStack().toLowerCase();
        int score = ss.getTechScore();

        if (isBackend(tech)) {
          beScore += score;
          beTechs.add(ss.getTechStack());
        } else if (isFrontend(tech)) {
          feScore += score;
          feTechs.add(ss.getTechStack());
        } else if (isInfra(tech)) {
          infraScore += score;
          infraTechs.add(ss.getTechStack());
        }
      }

      String role;
      String reason;
      if (beScore >= feScore && beScore >= infraScore) {
        role = "백엔드 개발자";
        reason = String.format("백엔드 핵심 기술(%s) 역량이 뛰어나 서버 로직 및 데이터베이스 설계를 주도하기에 적합합니다.",
            String.join(", ", beTechs));
      } else if (feScore >= beScore && feScore >= infraScore) {
        role = "프론트엔드 개발자";
        reason = String.format("프론트엔드 핵심 기술(%s) 역량이 우수하여 사용자 경험(UX) 개선 및 인터페이스 구현에 최적화되어 있습니다.",
            String.join(", ", feTechs));
      } else {
        role = "인프라 담당";
        reason = String.format("인프라 및 자동화 기술(%s)에 강점이 있어 안정적인 서비스 배포 및 운영 환경 구축에 기여할 수 있습니다.",
            String.join(", ", infraTechs));
      }

      result.append(String.format("%s - %s | %s\n", nickname, role, reason));
    }
    result.append("시스템 문제로 간이 분석 결과가 제공되었습니다. 상세 검토를 권장합니다.");

    return result.toString();
  }

  private boolean isBackend(String tech) {
    return tech.matches(
        ".*(java|spring|jpa|querydsl|mybatis|sql|hibernate|python|django|fastapi|node|express|ruby|rails|php|go|redis|mongodb|postgresql|mysql).*");
  }

  private boolean isFrontend(String tech) {
    return tech.matches(
        ".*(javascript|typescript|react|vue|angular|svelte|next|nuxt|html|css|tailwind|styled|bootstrap|flutter|dart).*");
  }

  private boolean isInfra(String tech) {
    return tech.matches(
        ".*(docker|kubernetes|k8s|aws|gcp|azure|jenkins|github actions|nginx|terraform|ansible|prometheus|grafana|linux).*");
  }
}
