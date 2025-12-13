package com.devmatch.backend.domain.user.service;

import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.repository.UserRepository;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
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

  public User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  public User getUserByRefreshToken(String refreshToken) {
    return userRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  public long count() {
    return userRepository.count();
  }

  public User join(String oauthId, String nickname, String profileImgUrl) {
    return userRepository.save(new User(oauthId, nickname, profileImgUrl));
  }

  private User findByOauthId(String oauthId) {
    return userRepository.findByOauthId(oauthId).orElse(null);
  }
}
