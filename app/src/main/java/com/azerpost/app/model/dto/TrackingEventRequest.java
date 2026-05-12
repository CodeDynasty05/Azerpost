package com.azerpost.app.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TrackingEventRequest {
    @NotNull
    @Positive
    private Long shipmentId;

    @NotBlank
    private String description;

    @NotBlank
    private String location;
}
