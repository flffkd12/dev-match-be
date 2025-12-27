package com.devmatch.backend.domain.application.entity;

import com.devmatch.backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "skill_scores")
public class SkillScore extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "application_id")
  private Application application;

  @Column(name = "tech_stack", nullable = false)
  private String techStack;

  @Column(name = "tech_score", nullable = false)
  private Integer techScore;

  @Builder
  public SkillScore(Application application, String techName, Integer score) {
    this.application = application;
    this.techStack = techName;
    this.techScore = score;
  }
}
