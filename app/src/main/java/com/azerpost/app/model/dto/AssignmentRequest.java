package com.azerpost.app.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AssignmentRequest {
    @NotNull
    @Positive
    private Long shipmentId;

    @NotNull
    @Positive
    private Long courierId;
}
