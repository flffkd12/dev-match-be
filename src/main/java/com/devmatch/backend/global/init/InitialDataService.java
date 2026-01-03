package com.devmatch.backend.global.init;

//import com.devmatch.backend.domain.project.dto.request.ProjectCreateRequest;
//import com.devmatch.backend.domain.project.dto.request.ProjectUpdateRequest;
//import com.devmatch.backend.domain.project.dto.response.ProjectResponse;
//import com.devmatch.backend.domain.project.repository.ProjectRepository;
//import com.devmatch.backend.domain.project.service.ProjectService;
//import com.devmatch.backend.domain.user.dto.UserResponse;
//import com.devmatch.backend.domain.user.entity.User;
//import com.devmatch.backend.domain.user.repository.UserRepository;
//import com.devmatch.backend.domain.user.service.UserService;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Slf4j
//@Service
//@Transactional
//@Profile("dev")
//@RequiredArgsConstructor
//public class InitialDataService {
//
//  private final UserService userService;
//  private final UserRepository userRepository;
//  private final ProjectService projectService;
//  private final ProjectRepository projectRepository;
//
//  public void createInitialUsers() {
//    if (userRepository.count() > 0) {
//      userRepository.findAll().forEach(
//          user -> log.info("Existing User: ID={}, Nickname={}", user.getId(), user.getNickname()));
//      return;
//    }
//
//    userService.join("user1", "유저1", null);
//    userService.join("user2", "유저2", null);
//    userService.join("user3", "유저3", null);
//
//    userRepository.findAll().forEach(
//        user -> log.info("Created User: ID={}, Nickname={}", user.getId(), user.getNickname()));
//  }
//
//  public void createInitialProjects() {
//    if (projectRepository.count() > 0) {
//      projectRepository.findAll().forEach(
//          project -> log.info("Existing Project: ID={}, Title={}", project.getId(),
//              project.getTitle()));
//      return;
//    }
//
//    User user = userService.modifyOrJoin("user1", "유저1", null);
//    log.info("Project Creator: ID={}, Nickname={}", user.getId(), user.getNickname());
//
//    // 1. Create Project
//    ProjectCreateRequest createRequest = new ProjectCreateRequest(
//        "테스트 프로젝트 1",
//        "설명입니다.",
//        List.of("Java", "Spring"),
//        5,
//        4
//    );
//    ProjectResponse project = projectService.createProject(user.getId(), createRequest);
//    log.info("Created Project 1: ID={}, Title={}", project.projectId(), project.title());
//
//    // 2. Update Project
//    ProjectUpdateRequest updateRequest = new ProjectUpdateRequest(
//        "수정된 프로젝트 1",
//        "수정된 설명입니다.",
//        List.of("Java", "Spring", "JPA"),
//        6,
//        8,
//        "백엔드 3명"
//    );
//    projectService.updateProject(user.getId(), project.projectId(), updateRequest);
//    log.info("Updated Project 1: ID={}", project.projectId());
//
//    // 3. Create & Delete Project
//    ProjectCreateRequest deleteRequest = new ProjectCreateRequest(
//        "삭제될 프로젝트",
//        "삭제될 예정",
//        List.of("Python"),
//        3,
//        2
//    );
//    ProjectResponse toDelete = projectService.createProject(user.getId(), deleteRequest);
//    log.info("Created Project for Deletion: ID={}, Title={}", toDelete.projectId(),
//        toDelete.title());

import com.devmatch.backend.domain.project.dto.request.ProjectCreateRequest;
import com.devmatch.backend.domain.project.dto.request.ProjectUpdateRequest;
import com.devmatch.backend.domain.project.dto.response.ProjectResponse;
import com.devmatch.backend.domain.project.repository.ProjectRepository;
import com.devmatch.backend.domain.project.service.ProjectService;
import com.devmatch.backend.domain.user.dto.UserResponse;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.domain.user.repository.UserRepository;
import com.devmatch.backend.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@Profile("dev")
@RequiredArgsConstructor
public class InitialDataService {

  private final UserService userService;
  private final UserRepository userRepository;
  private final ProjectService projectService;
  private final ProjectRepository projectRepository;

  public void createInitialUsers() {
    if (userRepository.count() > 0) {
      return;
    }

    userService.join("user1", "유저1", null);
    userService.join("user2", "유저2", null);
    userService.join("user3", "유저3", null);
  }

  public void createInitialProjects() {
    if (projectRepository.count() > 0) {
      return;
    }

    User user = userService.modifyOrJoin("user1", "유저1", null);

    // 1. Create Project
    ProjectCreateRequest createRequest = new ProjectCreateRequest(
        "테스트 프로젝트 1",
        "설명입니다.",
        List.of("Java", "Spring"),
        5,
        4
    );
    ProjectResponse project = projectService.createProject(user.getId(), createRequest);

    // 2. Update Project
    ProjectUpdateRequest updateRequest = new ProjectUpdateRequest(
        "수정된 프로젝트 1",
        "수정된 설명입니다.",
        List.of("Java", "Spring", "JPA"),
        6,
        8,
        "백엔드 3명"
    );
    projectService.updateProject(user.getId(), project.projectId(), updateRequest);

    // 3. Create & Delete Project
    ProjectCreateRequest deleteRequest = new ProjectCreateRequest(
        "삭제될 프로젝트",
        "삭제될 예정",
        List.of("Python"),
        3,
        2
    );
    ProjectResponse toDelete = projectService.createProject(user.getId(), deleteRequest);
    projectService.deleteProject(user.getId(), toDelete.projectId());

    // --- Verify GET Methods ---
    log.info("=== Start Verification of GET Methods ===");

    UserResponse userResponse2 = userService.getUser(2L);
    ProjectResponse project2 = projectService.createProject(userResponse2.id(), createRequest);

    // Verify UserService.getUser
    UserResponse userResponse = userService.getUser(user.getId());
    log.info("[Verify] UserService.getUser({}): Nickname={}", user.getId(),
        userResponse.nickname());

    // Verify ProjectService.getProject
    ProjectResponse projectResponse = projectService.getProject(project.projectId());
    log.info("[Verify] ProjectService.getProject({}): Title={}, TechStacks={}",
        project.projectId(), projectResponse.title(), projectResponse.techStacks());

    // Verify ProjectService.getProjects (Pagination)
    Page<ProjectResponse> projectPage = projectService.getProjects(PageRequest.of(0, 10));
    log.info(
        "[Verify] ProjectService.getProjects(Page 0, Size 10): Total Elements={}, Content Size={}",
        projectPage.getTotalElements(), projectPage.getContent().size());
    projectPage.forEach(p -> log.info(" -> Project: ID={}, Title={}", p.projectId(), p.title()));

    // Verify ProjectService.getProjectsByUserId
    Page<ProjectResponse> myProjects = projectService.getProjectsByUserId(user.getId(),
        PageRequest.of(0, 10));
    log.info("[Verify] ProjectService.getProjectsByUserId({}, Page 0, Size 10): Total Elements={}",
        user.getId(), myProjects.getTotalElements());
    myProjects.forEach(p -> log.info(" -> My Project: ID={}, Title={}", p.projectId(), p.title()));

    log.info("=== End Verification of GET Methods ===");
  }
}
