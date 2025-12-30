package com.devmatch.backend.domain.user.service;

import com.devmatch.backend.domain.user.dto.UserResponse;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.repository.UserRepository;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User modifyOrJoin(String oauthId, String nickname, String profileImgUrl) {
    User user = userRepository.findByOauthId(oauthId).orElse(null);
    return user == null ? join(oauthId, nickname, profileImgUrl)
        : user.modify(nickname, profileImgUrl);
  }

  public User join(String oauthId, String nickname, String profileImgUrl) {
    return userRepository.save(new User(oauthId, nickname, profileImgUrl));
  }

  @Transactional(readOnly = true)
  public UserResponse getUser(Long userId) {
    return UserResponse.from(findByUserId(userId));
  }

  @Transactional(readOnly = true)
  public User findByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public User getUserByRefreshToken(String refreshToken) {
    return userRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
  }

  @Transactional(readOnly = true)
  public long count() {
    return userRepository.count();
  }
}
