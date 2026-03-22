package com.taskmanagement.repository;

import com.taskmanagement.model.ProjectInvite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectInviteRepository extends JpaRepository<ProjectInvite, Long> {
    Optional<ProjectInvite> findByToken(String token);
    List<ProjectInvite> findByEmail(String email);
    Page<ProjectInvite> findByProjectId(Long projectId, Pageable pageable);
    Page<ProjectInvite> findByEmail(String email, Pageable pageable);
}
