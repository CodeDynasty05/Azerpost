package com.azerpost.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiverName;

    private String description;

    private BigDecimal amount;

    private LocalDateTime transferDate;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

}

