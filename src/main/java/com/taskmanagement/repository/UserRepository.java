package com.taskmanagement.repository;

import com.taskmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.is_deleted = false")
    Page<User> findActiveUsers(Pageable pageable);
}
