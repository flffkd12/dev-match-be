package com.devmatch.backend.domain.user.entity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PROTECTED)
  private Long id;

  @NotNull
  @Column(unique = true)
  @Size(min = 1, max = 50, message = "사용자 이름은 1자 이상 50자 이하이어야 합니다.")
  private String oauthId;

  @Column(unique = true)
  private String apiKey;//리프레시 토큰

  private String nickname;
  private String profileImgUrl;

  public User(Long id, String oAuthId, String nickname) {
    setId(id);
    this.oauthId = oAuthId;
    this.nickname = nickname;
  }

  public User(String oAuthId, String nickname, String profileImgUrl) {
    this.oauthId = oAuthId;
    this.apiKey = UUID.randomUUID().toString();
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