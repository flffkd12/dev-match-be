package com.devmatch.backend.domain.user.service;

import com.devmatch.backend.domain.auth.service.AuthTokenService;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.repository.UserRepository;
import com.devmatch.backend.global.RsData;
import com.devmatch.backend.global.exception.ServiceException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final AuthTokenService authTokenService;
  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public User getUser(Long id) {
    return userRepository.findById(id).orElseThrow(() ->
        new NoSuchElementException("해당 ID 사용자가 없습니다. ID: " + id));
  }

  @Transactional(readOnly = true)
  public long count() {
    return userRepository.count();
  }

  public User join(String username, String nickname, String profileImgUrl) {
    userRepository
        .findByOauthId(username)
        .ifPresent(_member -> {
          throw new ServiceException("409-1", "이미 존재하는 아이디입니다.");
        });

    User user = new User(username, nickname, profileImgUrl);

    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    return userRepository.findByOauthId(username).orElse(null);
  }

  @Transactional(readOnly = true)
  public Optional<User> findByApiKey(String apiKey) {
    return userRepository.findByApiKey(apiKey);
  }

  public String genAccessToken(User user) {
    return authTokenService.genAccessToken(user);
  }

  public Map<String, Object> payload(String accessToken) {
    return authTokenService.payload(accessToken);
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  public RsData<User> modifyOrJoin(String username, String nickname, String profileImgUrl) {
    User user = findByUsername(username);

    if (user == null) {
      user = join(username, nickname, profileImgUrl);
      return new RsData<>("201-1", "회원가입이 완료되었습니다.", user);
    }

    user.modify(nickname, profileImgUrl);

    return new RsData<>("200-1", "회원 정보가 수정되었습니다.", user);
  }
}