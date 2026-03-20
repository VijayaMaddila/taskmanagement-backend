package com.taskmanagement.repository;

import com.taskmanagement.model.ProjectInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectInviteRepository extends JpaRepository<ProjectInvite, Long> {
    Optional<ProjectInvite> findByToken(String token);
    List<ProjectInvite> findByProjectId(Long projectId);
    List<ProjectInvite> findByEmail(String email);
}
