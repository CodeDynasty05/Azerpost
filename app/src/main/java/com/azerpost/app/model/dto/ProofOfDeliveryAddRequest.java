package com.azerpost.app.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProofOfDeliveryAddRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String confirmationUrl;

    @NotNull
    @Positive
    private Long shipmentId;

    @Data
    public static class TransferResponse {
        private Long id;
        private String receiverName;
        private String description;
        private BigDecimal amount;
        private String date;
        private String status;

    }
}
