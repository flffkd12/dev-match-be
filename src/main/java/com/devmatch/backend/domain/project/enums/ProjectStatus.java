package com.devmatch.backend.domain.project.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProjectStatus {
  
  RECRUITING("모집 중"),
  COMPLETED("모집 완료");

  private final String description;

  @JsonValue
  public String getDescription() {
    return description;
  }
}
