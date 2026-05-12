package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ShipmentNotFoundException;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.TrackingEvent;
import com.azerpost.app.repository.ShipmentRepository;
import com.azerpost.app.repository.TrackingRepository;
import com.azerpost.app.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final TrackingRepository trackingRepository;
    private final ShipmentRepository shipmentRepository;

    @Override
    public TrackingEvent addTrackingEvent(Long shipmentId, String description, String location) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found"));

        TrackingEvent event = new TrackingEvent();
        event.setShipment(shipment);
        event.setDescription(description);
        event.setLocation(location);

        shipment.getTrackingEvents().add(event);
        return trackingRepository.save(event);
    }

    @Override
    public List<TrackingEvent> getTrackingEvents(Integer trackingNumber) {
        Shipment shipment = shipmentRepository.findWithEventByTrackingNumber(trackingNumber).orElseThrow(
                () -> new ShipmentNotFoundException("Shipment not found"));

        return shipment.getTrackingEvents();
    }
}
