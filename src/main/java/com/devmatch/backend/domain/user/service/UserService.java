package com.devmatch.backend.domain.user.service;

import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.repository.UserRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public User modifyOrJoin(String oauthId, String nickname, String profileImgUrl) {
    User user = findByOauthId(oauthId);
    return user == null ? join(oauthId, nickname, profileImgUrl)
        : user.modify(nickname, profileImgUrl);
  }

  public User getUser(Long id) {
    return userRepository.findById(id).orElseThrow(() ->
        new NoSuchElementException("해당 ID 사용자가 없습니다. ID: " + id));
  }

  public Optional<User> findByApiKey(String apiKey) {
    return userRepository.findByApiKey(apiKey);
  }

  public long count() {
    return userRepository.count();
  }

  private User join(String oauthId, String nickname, String profileImgUrl) {
    return userRepository.save(new User(oauthId, nickname, profileImgUrl));
  }

  private User findByOauthId(String oauthId) {
    return userRepository.findByOauthId(oauthId).orElse(null);
  }
}
