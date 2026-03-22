package com.taskmanagement.repository;

import com.taskmanagement.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTaskIdOrderByCreatedAtAsc(Long taskId, Pageable pageable);
}
