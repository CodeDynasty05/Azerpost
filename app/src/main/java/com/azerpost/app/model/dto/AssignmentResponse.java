package com.azerpost.app.model.dto;

import com.azerpost.app.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentResponse {
    private Long id;
    private Long shipmentId;
    private Integer trackingNumber;
    private User courier;
    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
}
