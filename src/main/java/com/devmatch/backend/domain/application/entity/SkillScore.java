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

  // 이 기술점수가 속한 지원서의 고유 식별자
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "application_id")
  private Application application;

  // 평가한 기술 명
  @Column(name = "tech_name", nullable = false)
  private String techName;

  // 기술 숙련도 점수(1점 = 초급 ~ 10점 = 전문가)
  @Column(name = "score", nullable = false)
  private int score;

  @Builder
  public SkillScore(Application application, String techName, int score) {
    this.application = application;
    this.techName = techName;
    this.score = score;
  }
}
