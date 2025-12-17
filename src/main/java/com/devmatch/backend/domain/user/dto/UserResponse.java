package com.devmatch.backend.domain.user.dto;

import com.devmatch.backend.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserResponse(Long id, String nickname, String profileImgUrl) {

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .profileImgUrl(user.getProfileImgUrl())
        .build();
  }
}
