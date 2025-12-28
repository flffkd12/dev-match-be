package com.devmatch.backend.domain.application.entity;

import com.devmatch.backend.domain.analysis.entity.AnalysisResult;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
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

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "analysis_result_id")
  private AnalysisResult analysisResult;

  @Builder
  public Application(User user, Project project) {
    this.applicant = user;
    this.project = project;
    this.status = ApplicationStatus.PENDING;
    this.skillScores = new ArrayList<>();
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

  public void setAnalysisResult(AnalysisResult analysisResult) {
    if (this.analysisResult != null) {
      throw new CustomException(ErrorCode.APPLICATION_ALREADY_ANALYZED);
    }
    this.analysisResult = analysisResult;
  }
}
