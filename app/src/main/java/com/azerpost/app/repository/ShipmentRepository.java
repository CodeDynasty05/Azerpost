package com.azerpost.app.repository;

import com.azerpost.app.model.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {
    Page<Shipment> findAll(Pageable pageable);

    Optional<Shipment> findByTrackingNumber(Integer trackingNumber);
    boolean existsByTrackingNumber(Integer trackingNumber);

    @EntityGraph(attributePaths = {"statusHistory"})
    Optional<Shipment> findWithHistoryById(Long id);

    @EntityGraph(attributePaths = {"trackingEvents"})
    Optional<Shipment> findWithEventByTrackingNumber(Integer trackingNumber);

    @EntityGraph(attributePaths = {"notifications"})
    Optional<Shipment> findWithNotificationById(Long id);
}
