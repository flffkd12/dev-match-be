package com.devmatch.backend.global.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProcessor {

  private final SecretKey secretKey;
  private final int expirationSeconds;

  public JwtProcessor(
      @Value("${custom.jwt.secret}") String secret,
      @Value("${custom.jwt.expirationSeconds}") int expirationSeconds
  ) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationSeconds = expirationSeconds;
  }

  public String createToken(Map<String, Object> body) {
    Date issuedAt = new Date();
    Date expiration = new Date(issuedAt.getTime() + 1000L * expirationSeconds);

    return Jwts.builder()
        .claims(body)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .signWith(secretKey)
        .compact();
  }

  public Map<String, Object> getPayload(String jwt) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(jwt)
        .getPayload();
  }
}
