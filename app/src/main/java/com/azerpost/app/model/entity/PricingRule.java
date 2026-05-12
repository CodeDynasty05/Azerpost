package com.azerpost.app.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "pricing_rules")
public class PricingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String label;

    @Column(nullable = false)
    private Double firstTWeightKg;

    @Column(nullable = false)
    private Double secondTWeightKg;

    @Column(nullable = false)
    private Double firstTWeightPrice;

    @Column(nullable = false)
    private Double secondTWeightPrice;

    @Column(nullable = false)
    private Double thirdTWeightPrice;

    @Column(nullable = false)
    private Double distancePricePerKm;

    @Column(nullable = false)
    private Double localZonePrice;

    @Column(nullable = false)
    private Double regionalZonePrice;

    @Column(nullable = false)
    private Double internationalZonePrice;

    @Column(nullable = false)
    private Double expressSurcharge;

    @Column(nullable = false)
    private Boolean active = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
