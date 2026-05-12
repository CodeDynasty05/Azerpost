package com.azerpost.app.repository;

import com.azerpost.app.model.entity.User;
import com.azerpost.app.model.enums.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"assignments"})
    @Query("SELECT u FROM User u JOIN u.authorities a WHERE a.authority = :role")
    List<User> findByAuthority(@Param("role") Role role);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"shipments"})
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameWithShipments(@Param("username") String username);

}


