package com.taskmanagement.repository;

import com.taskmanagement.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByAssignedToId(Long userId, Pageable pageable);
    Page<Task> findByStatus(Task.Status status, Pageable pageable);
    Page<Task> findByCreatedById(Long userId, Pageable pageable);

    long countByStatus(Task.Status status);

    @Query("SELECT COUNT(t) FROM Task t JOIN Team tm ON tm.project = t.project WHERE tm.user.id = :userId AND t.status = :status")
    long countByStatusAndUserId(Task.Status status, Long userId);

    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countGroupByStatus();
}
