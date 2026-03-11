package com.azerpost.app.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferResponse {
    private Long id;
    private String receiverName;
    private String description;
    private BigDecimal amount;
    private String date;
    private String status;

}

