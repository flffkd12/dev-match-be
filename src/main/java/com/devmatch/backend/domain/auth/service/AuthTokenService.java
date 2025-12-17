package com.devmatch.backend.domain.auth.service;

import com.devmatch.backend.domain.auth.jwt.JwtProcessor;
import com.devmatch.backend.domain.user.entity.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

  private final JwtProcessor jwtProcessor;

  public String genAccessToken(User user) {
    return jwtProcessor.createToken(
        Map.of("userId", user.getId(), "nickname", user.getNickname())
    );
  }

  public Map<String, Object> getPayload(String accessToken) {
    Map<String, Object> parsedPayload = jwtProcessor.getPayload(accessToken);

    if (parsedPayload == null) {
      return null;
    }

    Long id = ((Number) parsedPayload.get("id")).longValue();
    String nickname = (String) parsedPayload.get("nickname");

    return Map.of("userId", id, "nickname", nickname);
  }
}
