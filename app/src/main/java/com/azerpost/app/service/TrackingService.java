package com.azerpost.app.service;

import com.azerpost.app.model.entity.TrackingEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrackingService {
    TrackingEvent addTrackingEvent(Long shipmentId, String description, String location);

    List<TrackingEvent> getTrackingEvents(Integer trackingNumber);
}
