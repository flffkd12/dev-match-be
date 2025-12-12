package com.devmatch.backend.global.init;

import com.devmatch.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Profile("dev")
@RequiredArgsConstructor
public class InitialDataService {

  private final UserService userService;

  public void createInitialUsers() {
    if (userService.count() > 0) {
      return;
    }

    userService.join("user1", "유저1");
    userService.join("user2", "유저2");
    userService.join("user3", "유저3");
  }
}
