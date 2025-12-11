package com.devmatch.backend.domain.auth.security;

import com.devmatch.backend.domain.auth.social.OAuth2UserInfo;
import com.devmatch.backend.domain.auth.social.OAuth2UserInfoFactory;
import com.devmatch.backend.domain.auth.social.enums.OAuthProvider;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원 확인 및 등록 관련 클래스
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserService userService;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    log.info("Kakao OAuth2User Attributes: {}", oAuth2User.getAttributes());

    OAuthProvider oAuthProvider = OAuthProvider.valueOf(
        userRequest.getClientRegistration().getRegistrationId().toUpperCase());

    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuthProvider,
        oAuth2User.getAttributes());

    String oAuthUserId = userInfo.getProviderId();
    String nickname = userInfo.getNickname();
    String profileImgUrl = userInfo.getImageUrl();
    String username = oAuthProvider.name() + "_%s".formatted(oAuthUserId);
    User user = userService.modifyOrJoin(username, nickname, profileImgUrl).data();

    return new SecurityUser(
        user.getId(),
        user.getUsername(),
        user.getNickName(),
        user.getAuthorities()
    );
  }
}
