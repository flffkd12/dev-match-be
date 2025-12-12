package com.devmatch.backend.global.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class BaseInitData {

  private final InitialDataService initialDataService;

  @Bean
  ApplicationRunner baseInitDataApplicationRunner() {
    return args -> {
      initialDataService.createInitialUsers();
    };
  }
}