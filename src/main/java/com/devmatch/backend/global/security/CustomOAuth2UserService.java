package com.devmatch.backend.global.security;

import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.service.UserService;
import java.util.Map;
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
    // 엑세스 토큰으로 사용자 정보 가져오기
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String oauthUserId = "";
    String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

    String nickname = "";
    String profileImgUrl = "";

    switch (providerTypeCode) {
      case "KAKAO" -> {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> attributesProperties = (Map<String, Object>) attributes.get(
            "properties");

        oauthUserId = oAuth2User.getName();
        nickname = (String) attributesProperties.get("nickname");
        profileImgUrl = (String) attributesProperties.get("profile_image");
      }
      case "GOOGLE" -> {
        oauthUserId = oAuth2User.getName();
        nickname = (String) oAuth2User.getAttributes().get("name");
        profileImgUrl = (String) oAuth2User.getAttributes().get("picture");
      }
    }

    //유저 이름이 유니크 해야 하므로 소셜 서비스 이름과 아이디를 조합하여 유저 이름을 생성
    //예시: KAKAO__1234567890 뒷자리 숫자는 소셜 서비스에서 발급한 유저 아이디(고유함)
    String username = providerTypeCode + "__%s".formatted(oauthUserId);
    String password = "";//소셜 로그인은 비밀번호가 없으므로 빈 문자열로 설정

    User user = userService.modifyOrJoin(username, password, nickname, profileImgUrl).data();

    //이번 요청안에서만 로그인으로 처리. 그 이후에는 날라감
    //이 객체가 시큐리티 컨텍스트에 저장되어 최초 시점의 로그인한 유저 정보를 담고 있다
    //얘는 customAuthenticationFilter의 SecurityUser랑 뭐가 다른거지? -> 시점이 다르다. 이건 최초 로그인 시점의 객체
    // 커스텀 필터는 로그인 후에 토큰을 가지고 있는 상태에서만
    //어디서 쓰이는애지? -> 로그인 유저라고 인식하게 함. 콘텍스트에 들어가고 세션에도 들어간다.
    //어짜피 인증은 소셜이 해주니까 패스워드는 필요 없지않나? -> 필요 없다. 다만 패스워드를 삭제한 버전과 삭제하지 않은 버전 두 개를 만들어야 하니 놔둔거 뿐이다.
    return new SecurityUser(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getNickName(),
        user.getAuthorities()
    ); //-> 석세스 핸들러 호출
  }
}