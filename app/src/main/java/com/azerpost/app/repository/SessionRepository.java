package com.azerpost.app.repository;

import com.azerpost.app.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByRefreshToken(String refreshToken);

    boolean existsByIdAndUserId(Long id, Long userId);
}
