package com.devmatch.backend.domain.analysis.repository;

import com.devmatch.backend.domain.analysis.entity.Analysis;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

  Optional<Analysis> findByApplicationId(Long applicationId);
}