package com.devmatch.backend.domain.auth.social;

import java.util.Map;

public class KakaoUserInfo extends OAuth2UserInfo {

  private final Map<String, Object> properties;

  @SuppressWarnings("unchecked")
  public KakaoUserInfo(Map<String, Object> attributes) {
    super(attributes);
    this.properties = (Map<String, Object>) attributes.get("properties");
  }

  @Override
  public String getProviderId() {
    return attributes.get("id").toString();
  }

  @Override
  public String getNickname() {
    return properties != null ? properties.get("nickname").toString() : null;
  }

  @Override
  public String getImageUrl() {
    return properties != null ? properties.get("profile_image").toString() : null;
  }
}

