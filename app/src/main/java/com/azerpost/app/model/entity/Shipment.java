package com.azerpost.app.model.entity;

import com.azerpost.app.model.enums.ShipmentType;
import com.azerpost.app.model.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "shipments")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String receiverName;
    private Double weight;
    private String dimension;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Double price;
    @Column(unique = true)
    private Integer trackingNumber;
    @Enumerated(EnumType.STRING)
    private ShipmentType shipmentType;
    private Double distance; // Future work

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deliveryDate;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "sender_id",referencedColumnName = "id")
    private User sender;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrackingEvent> trackingEvents = new ArrayList<>();

    @OneToOne(mappedBy = "shipment")
    private ProofOfDelivery proofOfDelivery;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();
}
