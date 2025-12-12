package com.devmatch.backend.global.init;

import com.devmatch.backend.domain.user.entity.User;
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
        User memberSystem = userService.join("system", "시스템");
        memberSystem.modifyApiKey(memberSystem.getUsername());

        User memberAdmin = userService.join("admin", "관리자");
        memberAdmin.modifyApiKey(memberAdmin.getUsername());

        User memberUser1 = userService.join("user1", "유저1");
        memberUser1.modifyApiKey(memberUser1.getUsername());

        User memberUser2 = userService.join("user2", "유저2");
        memberUser2.modifyApiKey(memberUser2.getUsername());

        User memberUser3 = userService.join("user3", "유저3");
        memberUser3.modifyApiKey(memberUser3.getUsername());
    }
}
