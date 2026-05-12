package com.azerpost.app.controller;

import com.azerpost.app.model.entity.TrackingEvent;
import com.azerpost.app.model.dto.TrackingEventRequest;
import com.azerpost.app.service.TrackingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
@Validated
public class TrackingController {
    private final TrackingService trackingService;


    @PostMapping("/events")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COURIER','ROLE_OPERATOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public TrackingEvent addTrackingEvent(@Valid @RequestBody TrackingEventRequest request) {
        return trackingService.addTrackingEvent(
                request.getShipmentId(),
                request.getDescription(),
                request.getLocation()
        );
    }

    @GetMapping("/{trackingNumber}")
    public List<TrackingEvent> getTrackingEvents(@PathVariable @Positive Integer trackingNumber) {
        return trackingService.getTrackingEvents(trackingNumber);
    }
}

