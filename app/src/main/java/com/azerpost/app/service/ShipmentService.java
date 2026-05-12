package com.azerpost.app.service;

import com.azerpost.app.model.dto.ReturnAddRequest;
import com.azerpost.app.model.dto.ShipmentFilter;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.StatusHistory;
import com.azerpost.app.model.dto.ShipmentAddDto;
import com.azerpost.app.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShipmentService {
    Shipment getShipmentById(Long id);

    Page<Shipment> getShipments(Pageable pageable, ShipmentFilter shipmentFilter);

    Shipment createShipment(ShipmentAddDto shipmentAddDto);

    Shipment getShipmentByTrackingNumber(Integer trackingNumber);

    Shipment updateShipment(Long id, ShipmentAddDto shipmentAddDto);

    Shipment cancelShipment(Long id);

    void deleteShipment(Long id);

    Shipment updateStatus(Long id, Status status, String note);

    List<StatusHistory> getStatusHistory(Long id);
}
