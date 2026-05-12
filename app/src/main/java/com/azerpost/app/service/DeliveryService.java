package com.azerpost.app.service;

import com.azerpost.app.model.entity.Shipment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface DeliveryService {

    Shipment startShipment(Long shipmentId);

    Shipment confirmShipment(Long shipmentId);

    Shipment failShipment(Long shipmentId,String note);

    Shipment rescheduleShipment(Long shipmentId, LocalDateTime newDeliveryDate);
}
