package com.devmatch.backend.domain.analysis.entity;

import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "analysis_results")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisResult extends BaseEntity {

  @OneToOne(mappedBy = "analysisResult")
  private Application application;

  @Column(name = "compatibility_score", precision = 5, scale = 2, nullable = false)
  private BigDecimal compatibilityScore;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String compatibilityReason;
}