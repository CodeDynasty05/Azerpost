package com.azerpost.app.model.dto;

import com.azerpost.app.model.enums.ShipmentType;
import com.azerpost.app.model.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipmentDto {
    private Long id;
    private String receiverName;
    private Double weight;
    private String dimension;
    private String description;
    private Status status;
    private Double price;
    private Integer trackingNumber;
    private ShipmentType shipmentType;
    private Double distance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveryDate;
    private AddressDto address;
    private Long senderId;
    private String senderUsername;
}
