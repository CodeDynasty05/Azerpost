package com.azerpost.app.model.dto;

import com.azerpost.app.model.enums.ShipmentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PricingCalculateRequest {
    @NotNull
    @Positive
    private Double weight;

    @NotNull
    @Positive
    private Double distance;

    @NotNull
    private ShipmentType shipmentType;

    @NotNull
    @Valid
    private AddressDto address;
}
