package com.taskmanagement.repository;

import com.taskmanagement.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {


    @Query(value = "SELECT a FROM ActivityLog a JOIN FETCH a.user WHERE a.user.id = :userId ORDER BY a.createdAt DESC",
           countQuery = "SELECT COUNT(a) FROM ActivityLog a WHERE a.user.id = :userId")
    Page<ActivityLog> findLogsByUserId(@Param("userId") Long userId, Pageable pageable);

    
    @Query(value = "SELECT a FROM ActivityLog a JOIN FETCH a.user WHERE a.entityType = :entityType AND a.entityId = :entityId ORDER BY a.createdAt DESC",
           countQuery = "SELECT COUNT(a) FROM ActivityLog a WHERE a.entityType = :entityType AND a.entityId = :entityId")
    Page<ActivityLog> findLogsByEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId, Pageable pageable);


    @Query(value = "SELECT a FROM ActivityLog a JOIN FETCH a.user LEFT JOIN FETCH a.task WHERE a.task.id = :taskId ORDER BY a.createdAt DESC",
           countQuery = "SELECT COUNT(a) FROM ActivityLog a WHERE a.task.id = :taskId")
    Page<ActivityLog> findStatusHistoryByTaskId(@Param("taskId") Long taskId, Pageable pageable);

    @Query(value = "SELECT a FROM ActivityLog a JOIN FETCH a.user ORDER BY a.createdAt DESC",
           countQuery = "SELECT COUNT(a) FROM ActivityLog a")
    Page<ActivityLog> findRecentLogs(Pageable pageable);
}
