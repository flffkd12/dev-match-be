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
    Long id = user.getId();
    String username = user.getOauthId();
    String name = user.getNickname();

    return jwtProcessor.createToken(
        Map.of("id", id, "username", username, "name", name)
    );
  }

  public Map<String, Object> getPayload(String accessToken) {
    Map<String, Object> parsedPayload = jwtProcessor.getPayload(accessToken);

    if (parsedPayload == null) {
      return null;
    }

    Long id = ((Number) parsedPayload.get("id")).longValue();
    String username = (String) parsedPayload.get("username");
    String name = (String) parsedPayload.get("name");

    return Map.of("id", id, "username", username, "name", name);
  }
}
