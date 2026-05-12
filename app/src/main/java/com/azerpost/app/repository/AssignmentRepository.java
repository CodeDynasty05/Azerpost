package com.azerpost.app.repository;

import com.azerpost.app.model.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByCourierId(Long courierId);

    Optional<Assignment> findByShipmentId(Long shipmentId);
}