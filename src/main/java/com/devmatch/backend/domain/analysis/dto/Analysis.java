package com.devmatch.backend.domain.analysis.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record Analysis(BigDecimal compatibilityScore, String compatibilityReason) {

}