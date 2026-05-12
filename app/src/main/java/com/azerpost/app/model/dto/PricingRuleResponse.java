package com.azerpost.app.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PricingRuleResponse {
    private Long id;
    private String label;
    private Double firstTWeightKg;
    private Double secondTWeightKg;
    private Double firstTWeightPrice;
    private Double secondTWeightPrice;
    private Double thirdTWeightPrice;
    private Double distancePricePerKm;
    private Double localZonePrice;
    private Double regionalZonePrice;
    private Double internationalZonePrice;
    private Double expressSurcharge;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
