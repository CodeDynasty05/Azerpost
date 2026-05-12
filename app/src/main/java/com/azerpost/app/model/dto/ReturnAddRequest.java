package com.azerpost.app.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReturnAddRequest {
    @NotNull
    @Positive
    private Long shipmentId;

    @NotBlank
    @Size(max = 500)
    private String reason;
}
