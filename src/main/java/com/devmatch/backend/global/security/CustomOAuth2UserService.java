package com.devmatch.backend.global.security;

import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.enums.OAuthProvider;
import com.devmatch.backend.domain.user.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원 확인 및 등록 관련 클래스
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserService userService;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    OAuthProvider oAuthProvider = OAuthProvider.valueOf(
        userRequest.getClientRegistration().getRegistrationId().toUpperCase());

    String oauthUserId = "";
    String nickname = "";
    String profileImgUrl = "";

    switch (oAuthProvider) {
      case KAKAO -> {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> attributesProperties = (Map<String, Object>) attributes.get(
            "properties");

        oauthUserId = oAuth2User.getName();
        nickname = (String) attributesProperties.get("nickname");
        profileImgUrl = (String) attributesProperties.get("profile_image");
      }
      case GOOGLE -> {
        oauthUserId = oAuth2User.getName();
        nickname = (String) oAuth2User.getAttributes().get("name");
        profileImgUrl = (String) oAuth2User.getAttributes().get("picture");
      }
    }

    String username = oAuthProvider.name() + "_%s".formatted(oauthUserId);
    User user = userService.modifyOrJoin(username, nickname, profileImgUrl).data();

    return new SecurityUser(
        user.getId(),
        user.getUsername(),
        user.getNickName(),
        user.getAuthorities()
    );
  }
}

