package com.devmatch.backend.domain.analysis.enums;

import lombok.Getter;

@Getter
public enum RateLimitType {
  RPM("GenerateRequestsPerMinutePerProjectPerModel-FreeTier"),
  RPD("GenerateRequestsPerDayPerProjectPerModel-FreeTier"),
  UNKNOWN("");

  private final String quotaId;

  RateLimitType(String quotaId) {
    this.quotaId = quotaId;
  }

  public static RateLimitType fromErrorMessage(String errorMessage) {
    if (errorMessage == null) {
      return UNKNOWN;
    }

    if (errorMessage.contains(RPM.quotaId)) {
      return RPM;
    } else if (errorMessage.contains(RPD.quotaId)) {
      return RPD;
    }

    return UNKNOWN;
  }
}
