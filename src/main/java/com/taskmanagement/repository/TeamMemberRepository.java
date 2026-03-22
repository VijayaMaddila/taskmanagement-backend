package com.taskmanagement.repository;

import com.taskmanagement.model.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    Page<TeamMember> findByTeamId(Long teamId, Pageable pageable);
    List<TeamMember> findByUserId(Long userId);
}
