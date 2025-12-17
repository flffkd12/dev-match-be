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
    return jwtProcessor.createToken(Map.of(
        "userId", user.getId(),
        "nickname", user.getNickname())
    );
  }

  public Map<String, Object> getPayload(String accessToken) {
    Map<String, Object> claims = jwtProcessor.getPayload(accessToken);
    return Map.of(
        "userId", ((Number) claims.get("userId")).longValue(),
        "nickname", claims.get("nickname")
    );
  }
}
