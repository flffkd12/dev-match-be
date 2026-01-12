package com.devmatch.backend.domain.project.repository;

import com.devmatch.backend.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Override
  @NonNull
  @Query("SELECT p FROM Project p JOIN FETCH p.creator")
  Page<Project> findAll(@NonNull Pageable pageable);

  @Query("SELECT p FROM Project p JOIN FETCH p.creator WHERE p.creator.id = :creatorId")
  Page<Project> findAllByCreatorId(@Param("creatorId") Long creatorId, Pageable pageable);
}