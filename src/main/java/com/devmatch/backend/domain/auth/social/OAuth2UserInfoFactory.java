package com.devmatch.backend.domain.auth.social;

import com.devmatch.backend.domain.auth.social.enums.OAuthProvider;
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
