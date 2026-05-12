package com.azerpost.app.controller;

import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.service.DeliveryService;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
@Validated
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping("/start")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR','ROLE_COURIER')")
    public Shipment startShipment(@RequestParam @Positive Long shipmentId){
        return deliveryService.startShipment(shipmentId);
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR','ROLE_COURIER')")
    public Shipment confirmShipment(@RequestParam @Positive Long shipmentId){
        return deliveryService.confirmShipment(shipmentId);
    }

    @PostMapping("/fail")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR','ROLE_COURIER')")
    public Shipment failShipment(@RequestParam @Positive Long shipmentId,@RequestParam @NotBlank String note){
        return deliveryService.failShipment(shipmentId,note);
    }

    @PostMapping("/reschedule")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public Shipment rescheduleShipment(@RequestParam @Positive Long shipmentId, @RequestParam @NotNull @FutureOrPresent LocalDateTime newDeliveryDate){
        return deliveryService.rescheduleShipment(shipmentId,newDeliveryDate);
    }
}
