package com.devmatch.backend.domain.analysis.dto;

import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import java.math.BigDecimal;
import lombok.Builder;

@Builder

public record Analysis(BigDecimal compatibilityScore, String compatibilityReason) {

  private static final BigDecimal MAX_SCORE = BigDecimal.valueOf(100);
  private static final BigDecimal MIN_SCORE = BigDecimal.ZERO;

  public static Analysis from(String aiResponse) {
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