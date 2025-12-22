package com.devmatch.backend.domain.project.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.project.enums.ProjectStatus;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.global.common.BaseEntity;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@NoArgsConstructor
@Entity
@Table(
    name = "projects",
    indexes = {@Index(name = "idx_creator_id", columnList = "creator_id")}
)
public class Project extends BaseEntity {

  private String title;
  private String description;
  private String techStack;
  private String roleAssignment;

  private Integer teamSize;
  private Integer currentTeamSize;
  private Integer durationWeeks;

  @ManyToOne
  private User creator;

  @Enumerated(EnumType.STRING)
  private ProjectStatus status;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "project", fetch = LAZY, orphanRemoval = true)
  private List<Application> applications;

  @Builder
  public Project(
      String title,
      String description,
      String techStack,
      Integer teamSize,
      User creator,
      Integer durationWeeks
  ) {
    this.title = title;
    this.description = description;
    this.techStack = techStack;
    this.teamSize = teamSize;
    this.creator = creator;
    this.status = ProjectStatus.RECRUITING;
    this.currentTeamSize = 0;
    this.roleAssignment = "";
    this.durationWeeks = durationWeeks;
  }

  public void changeStatus(ProjectStatus newStatus) {
    if (newStatus == this.status) {
      throw new CustomException(ErrorCode.PROJECT_SAME_STATUS);
    }
    this.status = newStatus;
  }

  public void increaseCurrentTeamSize() {
    if (this.currentTeamSize > this.teamSize) {
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Project ID: " + getId() +
          ", Current size: " + this.currentTeamSize + ", Max: " + this.teamSize);
    }

    if (this.currentTeamSize.equals(this.teamSize)) {
      throw new CustomException(ErrorCode.PROJECT_FULL_PEOPLE);
    }

    this.currentTeamSize++;

    if (this.currentTeamSize.equals(this.teamSize)) {
      this.status = ProjectStatus.COMPLETED;
    }
  }

  public void decreaseCurrentTeamSize() {
    if (this.currentTeamSize <= 0) {
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR,
          "Project ID: " + getId() + ", Current size: " + this.currentTeamSize);
    }
    this.currentTeamSize--;
    this.status = ProjectStatus.RECRUITING;
  }

  public void updateRoleAssignment(String roleAssignment) {
    this.roleAssignment = roleAssignment;
  }
}
