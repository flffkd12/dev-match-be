package com.devmatch.backend.domain.auth.social;

import java.util.Map;

public class NaverUserInfo extends OAuth2UserInfo {

  private final Map<String, Object> response;

  @SuppressWarnings("unchecked")
  public NaverUserInfo(Map<String, Object> attributes) {
    super(attributes);
    this.response = (Map<String, Object>) attributes.get("response");
  }

  @Override
  public String getProviderId() {
    return response != null ? (String) response.get("id") : null;
  }

  @Override
  public String getNickname() {
    return response != null ? (String) response.get("nickname") : null;
  }

  @Override
  public String getImageUrl() {
    return response != null ? (String) response.get("profile_image") : null;
  }
}
