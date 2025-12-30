package com.devmatch.backend.domain.analysis.dto;

import com.devmatch.backend.domain.analysis.entity.Analysis;
import java.math.BigDecimal;

public record AnalysisResponse(
    Long analysisId,
    Long applicationId,
    BigDecimal compatibilityScore,
    String compatibilityReason
) {

  public static AnalysisResponse from(Analysis analysis) {
    return new AnalysisResponse(
        analysis.getId(),
        analysis.getApplication().getId(),
        analysis.getCompatibilityScore(),
        analysis.getCompatibilityReason()
    );
  }
}