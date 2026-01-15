package com.devmatch.backend.domain.application.repository;

import com.devmatch.backend.domain.application.entity.Application;
import com.devmatch.backend.domain.application.enums.ApplicationStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

  @Query("SELECT a FROM Application a JOIN FETCH a.applicant WHERE a.applicant.id = :applicantId")
  List<Application> findAllByApplicantId(@Param("applicantId") Long applicantId);

  @Query("SELECT a FROM Application a JOIN FETCH a.applicant WHERE a.project.id = :applicantId")
  List<Application> findAllByProjectId(@Param("applicantId") Long applicantId);

  boolean existsByApplicantIdAndProjectId(Long applicantId, Long projectId);

  @Query("SELECT a FROM Application a JOIN FETCH a.applicant WHERE a.project.id = :projectId AND a.status = :status")
  List<Application> findByProjectIdAndStatus(@Param("projectId") Long projectId,
      @Param("status") ApplicationStatus status);

}