package com.devmatch.backend.domain.auth.service;

import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.global.util.Ut;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

  @Value("${custom.jwt.secretKey}")
  private String jwtSecretKey;

  @Value("${custom.accessToken.expirationSeconds}")
  private int accessTokenExpirationSeconds;

  public String genAccessToken(User user) {
    Long id = user.getId();
    String username = user.getOauthId();//롬복
    String name = user.getNickname();//닉네임 가져오는 메서드

    return Ut.jwt.toString(
        jwtSecretKey,
        accessTokenExpirationSeconds,
        Map.of("id", id, "username", username, "name", name)
    );
  }

  public Map<String, Object> getPayload(String accessToken) {
    Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, accessToken);

    if (parsedPayload == null) {
      return null;
    }

    Long id = ((Number) parsedPayload.get("id")).longValue();
    String username = (String) parsedPayload.get("username");
    String name = (String) parsedPayload.get("name");

    return Map.of("id", id, "username", username, "name", name);
  }
}