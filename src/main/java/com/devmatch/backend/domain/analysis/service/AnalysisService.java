package com.devmatch.backend.domain.analysis.service;

import com.devmatch.backend.domain.analysis.dto.Analysis;
import com.devmatch.backend.domain.analysis.enums.RateLimitType;
import com.devmatch.backend.domain.application.dto.Skill;
import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.entity.SkillScore;
import com.devmatch.backend.domain.project.entity.Project;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AnalysisService {

  private final String STATIC_ANALYSIS_REQUIRED = "모든 AI 모델의 일일 할당량 초과";

  private final ChatModel chatModel;
  private final AnalysisPromptGenerator promptGenerator;
  private final String defaultModel;
  private final String fallbackModel;

  AnalysisService(
      ChatModel chatModel,
      AnalysisPromptGenerator promptGenerator,
      @Value("${spring.ai.openai.chat.options.model}") String defaultModel,
      @Value("${custom.ai.fallback-model}") String fallbackModel
  ) {
    this.chatModel = chatModel;
    this.promptGenerator = promptGenerator;
    this.defaultModel = defaultModel;
    this.fallbackModel = fallbackModel;
  }

  public Analysis createAnalysis(Project project, List<Skill> applicantSkills) {
    String prompt = promptGenerator.generateAnalysisPrompt(project, applicantSkills);
    log.debug("지원서 분석 전체 프롬프트: {}", prompt);

    String aiResponse = callAiWithRetry(prompt);
    log.debug("AI 지원서 분석 응답: {}", aiResponse);
    if (aiResponse.equals(STATIC_ANALYSIS_REQUIRED)) {
      aiResponse = generateStaticApplicationAnalysis(project, applicantSkills);
    }

    return Analysis.from(aiResponse);
  }

  public String createProjectRoleAssignment(
      Project project,
      List<Application> approvedApplications
  ) {
    String prompt = promptGenerator.generateRoleAssignmentPrompt(project, approvedApplications);
    log.debug("역할 분석 전체 프롬프트: {}", prompt);

    String aiResponse = callAiWithRetry(prompt);
    log.debug("AI 프로젝트 역할 분석 응답: {}", aiResponse);
    if (aiResponse.equals(STATIC_ANALYSIS_REQUIRED)) {
      return generateStaticProjectRoleAssignment(approvedApplications);
    }

    return aiResponse;
  }

  private String generateStaticProjectRoleAssignment(List<Application> approvedApplications) {
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

      result.append(String.format("%s - %s | %s", nickname, role, reason));
      result.append("\n시스템 문제로 간이 분석 결과가 제공되었습니다. 상세 검토를 권장합니다.");
    }

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

  private String callAiWithRetry(String promptText) {
    int maxRetries = 3;
    int retryCount = 0;
    String currentModel = defaultModel;

    while (retryCount < maxRetries) {
      try {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
            .model(currentModel)
            .temperature(0.0)
            .build();

        return chatModel.call(new Prompt(promptText, options))
            .getResult()
            .getOutput()
            .getText();
      } catch (Exception e) {
        currentModel = handleRateLimitError(e.getMessage(), currentModel, retryCount, maxRetries);
        retryCount++;
      }
    }

    throw new CustomException(ErrorCode.ANALYSIS_MANY_REQUESTS, "AI 모델 호출 재시도 횟수 초과");
  }

  private String handleRateLimitError(
      String errorMessage,
      String currentModel,
      int retryCount,
      int maxRetries
  ) {
    RateLimitType rateLimitType = RateLimitType.fromErrorMessage(errorMessage);

    if (rateLimitType == RateLimitType.UNKNOWN) {
      log.error("처리하고 있지 않은 quotaId 오류입니다. 메시지: {}", errorMessage);
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "AI 분석 중 예상치 못한 오류가 발생했습니다.");
    }

    log.warn("AI 모델 Rate Limit 발생 (타입: {}, 모델: {}). 시도 {}/{}", rateLimitType, currentModel,
        retryCount + 1, maxRetries);

    if (rateLimitType == RateLimitType.RPD) {
      if (currentModel.equals(fallbackModel)) {
        log.warn("모든 AI 모델의 일일 할당량이 초과되었습니다. 정적 분석으로 전환합니다.");
        return STATIC_ANALYSIS_REQUIRED;
      }

      log.info("RPD 감지됨. 모델을 {} -> {} 로 전환하여 재시도합니다.", currentModel, fallbackModel);
      return fallbackModel;
    } else if (rateLimitType == RateLimitType.RPM) {
      try {
        Thread.sleep(extractWaitTime(errorMessage));
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "분석 재시도 대기 중 인터럽트 발생");
      }
    }

    return currentModel;
  }

  private long extractWaitTime(String errorMessage) {
    try {
      Pattern jsonPattern = Pattern.compile("\"retryDelay\":\\s*\"([0-9]+)s\"");
      Matcher jsonMatcher = jsonPattern.matcher(errorMessage);
      if (jsonMatcher.find()) {
        long seconds = Long.parseLong(jsonMatcher.group(1));
        return (seconds + 1) * 1000;
      }
    } catch (Exception e) {
      log.warn("대기 시간 파싱 실패, 기본값 60초 적용", e);
    }

    return 60000L;
  }

  private String generateStaticApplicationAnalysis(Project project, List<Skill> applicantSkills) {
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
}
