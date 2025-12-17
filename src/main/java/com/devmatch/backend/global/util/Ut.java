package com.devmatch.backend.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

public class Ut {

  public static class jwt {

    private static SecretKey getSecretKey(String secret) {
      return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public static String toString(String secret, int expireSeconds, Map<String, Object> body) {
      ClaimsBuilder claimsBuilder = Jwts.claims();

      for (Map.Entry<String, Object> entry : body.entrySet()) {
        claimsBuilder.add(entry.getKey(), entry.getValue());
      }

      Claims claims = claimsBuilder.build();

      Date issuedAt = new Date();
      Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

      return Jwts.builder()
          .claims(claims)
          .issuedAt(issuedAt)
          .expiration(expiration)
          .signWith(getSecretKey(secret))
          .compact();
    }

    public static Map<String, Object> payload(String secret, String jwtStr) {
      return getClaims(secret, jwtStr);
    }

    private static Claims getClaims(String secret, String jwtStr) {
      return Jwts
          .parser()
          .verifyWith(getSecretKey(secret))
          .build()
          .parseSignedClaims(jwtStr)
          .getPayload();
    }
  }

  public static class json {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toString(Object object) {
      return toString(object, null);
    }

    public static String toString(Object object, String defaultValue) {
      try {
        return objectMapper.writeValueAsString(object);
      } catch (Exception e) {
        return defaultValue;
      }
    }
  }
}
