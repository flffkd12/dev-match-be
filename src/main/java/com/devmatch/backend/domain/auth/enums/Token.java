package com.devmatch.backend.domain.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Token {

  ACCESS_TOKEN("accessToken"),
  REFRESH_TOKEN("refreshToken");

  private final String name;
}
