package com.devmatch.backend.domain.auth.security;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class SecurityUser extends User implements OAuth2User {

  private final Long userId;

  public SecurityUser(
      Long userId,
      String oauthId,
      Collection<? extends GrantedAuthority> authorities
  ) {
    super(oauthId, "", authorities);
    this.userId = userId;
  }

  // 필요할 정보는 User 테이블에 존재
  @Override
  public Map<String, Object> getAttributes() {
    return Map.of();
  }

  @Override
  public String getName() {
    return super.getUsername();
  }
}