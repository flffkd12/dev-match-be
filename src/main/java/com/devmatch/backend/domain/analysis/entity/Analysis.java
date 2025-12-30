package com.devmatch.backend.domain.analysis.entity;

import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.global.common.BaseEntity;
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
@Table(name = "analyses")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analysis extends BaseEntity {

  @OneToOne(mappedBy = "analysis")
  private Application application;

  @Column(precision = 5, scale = 2, nullable = false)
  private BigDecimal compatibilityScore;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String compatibilityReason;
}