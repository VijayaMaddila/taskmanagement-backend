package com.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.taskmanagement.model.Project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("SELECT p FROM Project p WHERE p.is_deleted = false")
    Page<Project> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Project p JOIN TeamMember tm ON tm.team = p.team WHERE tm.user.id = :userId AND p.is_deleted = false")
    Page<Project> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Project p JOIN TeamMember tm ON tm.team = p.team WHERE tm.user.id = :userId AND p.is_deleted = false")
    long countByUserId(Long userId);
}
