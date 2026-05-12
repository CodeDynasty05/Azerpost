package com.azerpost.app.model.dto;

import com.azerpost.app.model.enums.ShipmentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipmentAddDto {
    @NotBlank
    private String receiverName;

    @NotBlank
    private String description;

    @NotNull
    private ShipmentType shipmentType;

    @NotNull
    @Valid
    private AddressDto address;

    @NotNull
    @Positive
    private Double weight;

    @NotBlank
    private String dimension;

    @NotNull
    @Positive
    private Double distance;

    @Data
    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;
    }
}
