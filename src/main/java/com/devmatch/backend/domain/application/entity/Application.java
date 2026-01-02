package com.devmatch.backend.domain.application.entity;

import com.devmatch.backend.domain.application.enums.ApplicationStatus;
import com.devmatch.backend.domain.project.entity.Project;
import com.devmatch.backend.domain.user.entity.User;
import com.devmatch.backend.global.common.BaseEntity;
import com.devmatch.backend.global.exception.CustomException;
import com.devmatch.backend.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "applications")
public class Application extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "applicant_id")
  private User applicant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  @OneToMany(mappedBy = "application", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<SkillScore> skillScores;

  @Column(precision = 5, scale = 2, nullable = false)
  private BigDecimal compatibilityScore;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String compatibilityReason;

  @Builder
  public Application(
      User user,
      Project project,
      BigDecimal compatibilityScore,
      String compatibilityReason
  ) {
    this.applicant = user;
    this.project = project;
    this.status = ApplicationStatus.PENDING;
    this.skillScores = new ArrayList<>();
    this.compatibilityScore = compatibilityScore;
    this.compatibilityReason = compatibilityReason;
  }

  public void addSkillScore(String techStack, Integer techScore) {
    SkillScore newSkillScore = SkillScore.builder()
        .application(this)
        .techName(techStack)
        .score(techScore)
        .build();
    this.skillScores.add(newSkillScore);
  }

  public void updateStatus(ApplicationStatus status) {
    if (status == this.status) {
      throw new CustomException(ErrorCode.APPLICATION_SAME_STATUS);
    }
    this.status = status;
  }
}
