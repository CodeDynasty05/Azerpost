package com.azerpost.app.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String region;
    private String city;
    private String district;
    private String street;
    private String postalCode;

    @Column(nullable = false)
    private String tariffZone;
}
