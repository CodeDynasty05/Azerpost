package com.azerpost.app.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PricingRuleRequest {
    @NotBlank
    private String label;

    @NotNull
    @Positive
    private Double firstTWeightKg;

    @NotNull
    @Positive
    private Double secondTWeightKg;

    @NotNull
    @PositiveOrZero
    private Double firstTWeightPrice;

    @NotNull
    @PositiveOrZero
    private Double secondTWeightPrice;

    @NotNull
    @PositiveOrZero
    private Double thirdTWeightPrice;

    @NotNull
    @PositiveOrZero
    private Double distancePricePerKm;

    @NotNull
    @PositiveOrZero
    private Double localZonePrice;

    @NotNull
    @PositiveOrZero
    private Double regionalZonePrice;

    @NotNull
    @PositiveOrZero
    private Double internationalZonePrice;

    @NotNull
    @PositiveOrZero
    private Double expressSurcharge;

    @NotNull
    private Boolean active;
}
