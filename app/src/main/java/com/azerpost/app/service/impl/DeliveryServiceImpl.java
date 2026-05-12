package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ProcessForbidden;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.dto.ShipmentAddDto;
import com.azerpost.app.model.enums.Status;
import com.azerpost.app.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final ShipmentServiceImpl shipmentService;

    @Override
    public Shipment startShipment(Long shipmentId) {
        if(shipmentService.getShipmentById(shipmentId).getStatus() != Status.ASSIGNED_TO_COURIER){
            throw new ProcessForbidden("Shipment is not assigned to courier");
        }
        return shipmentService.updateStatus(shipmentId, Status.PICKED_UP_BY_COURIER,null);
    }

    @Override
    public Shipment confirmShipment(Long shipmentId) {
        if(shipmentService.getShipmentById(shipmentId).getStatus() != Status.OUT_FOR_DELIVERY){
            throw new ProcessForbidden("Shipment is not out for delivery");
        }
        return shipmentService.updateStatus(shipmentId, Status.DELIVERED,null);
    }

    @Override
    public Shipment failShipment(Long shipmentId,String note) {
        if(shipmentService.getShipmentById(shipmentId).getStatus() != Status.OUT_FOR_DELIVERY){
            throw new ProcessForbidden("Shipment is not out for delivery");
        }
        return shipmentService.updateStatus(shipmentId,Status.DELIVERY_ATTEMPT_FAILED,note);
    }

    @Override
    public Shipment rescheduleShipment(Long shipmentId, LocalDateTime newDeliveryDate) {
        Shipment shipment = shipmentService.getShipmentById(shipmentId);
        shipment.setDeliveryDate(newDeliveryDate);
        return shipmentService.updateStatus(shipmentId,Status.OUT_FOR_DELIVERY,null);
    }
}
