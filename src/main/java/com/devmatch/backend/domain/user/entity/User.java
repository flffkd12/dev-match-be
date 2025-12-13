package com.devmatch.backend.domain.user.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @NotNull
  @Column(unique = true)
  private String oauthId;

  @Column(unique = true)
  private String refreshToken;

  private String nickname;
  private String profileImgUrl;

  public User(String oAuthId, String nickname, String profileImgUrl) {
    this.oauthId = oAuthId;
    this.refreshToken = UUID.randomUUID().toString();
    this.nickname = nickname;
    this.profileImgUrl = StringUtils.hasText(profileImgUrl) ? profileImgUrl
        : "https://placehold.co/600x600?text=U_U";
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  public User modify(String nickname, String profileImgUrl) {
    this.nickname = nickname;
    this.profileImgUrl = profileImgUrl;
    return this;
  }
}