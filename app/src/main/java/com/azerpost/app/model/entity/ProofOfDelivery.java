package com.azerpost.app.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "proof_of_delivery")
public class ProofOfDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String phoneNumber;

    private String confirmationUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipment_id", unique = true)
    private Shipment shipment;
}
