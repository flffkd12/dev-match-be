package com.devmatch.backend.domain.analysis.dto;

import java.math.BigDecimal;

public record Analysis(BigDecimal compatibilityScore, String compatibilityReason) {

}