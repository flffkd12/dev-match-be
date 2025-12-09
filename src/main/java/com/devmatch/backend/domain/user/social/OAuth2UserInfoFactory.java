package com.devmatch.backend.domain.user.social;

import com.devmatch.backend.domain.user.enums.OAuthProvider;
import java.util.Map;

public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo getOAuth2UserInfo(
      OAuthProvider providerType,
      Map<String, Object> attributes
  ) {
    return switch (providerType) {
      case GOOGLE -> new GoogleUserInfo(attributes);
      case KAKAO -> new KakaoUserInfo(attributes);
    };
  }
}
