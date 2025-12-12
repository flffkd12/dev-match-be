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
  private String username;//기존 name 필드 대신 사용, 유니크한 사용자 이름
  @Column(unique = true)
  private String apiKey;//리프레시 토큰

  private String nickname;
  private String profileImgUrl;

  public User(Long id, String username, String nickname) {
    setId(id);
    this.username = username;
    this.nickname = nickname;
  }

  public User(String username, String nickname, String profileImgUrl) {
    this.username = username;
    this.nickname = nickname;
    this.profileImgUrl = profileImgUrl;
    this.apiKey = UUID.randomUUID().toString();
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  public void modify(String nickname, String profileImgUrl) {
    this.nickname = nickname;
    this.profileImgUrl = profileImgUrl;
  }

  //getActor를 통한 유저 객체에는 url이 없으니 getActorFromDb를 통해 DB에서 꺼낸 유저 객체를 사용해서 url을 사용.
  public String getProfileImgUrlOrDefault() {
    if (profileImgUrl == null) {
      return "https://placehold.co/600x600?text=U_U";
    }

    return profileImgUrl;
  }
}