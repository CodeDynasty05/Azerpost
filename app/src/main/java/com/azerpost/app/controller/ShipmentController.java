package com.azerpost.app.controller;

import com.azerpost.app.model.dto.ShipmentFilter;
import com.azerpost.app.model.entity.StatusHistory;
import com.azerpost.app.model.dto.ShipmentAddDto;
import com.azerpost.app.model.dto.ShipmentDto;
import com.azerpost.app.mapper.ShipmentMapper;
import com.azerpost.app.model.enums.Status;
import com.azerpost.app.service.ShipmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
@Validated
public class ShipmentController {
    private final ShipmentService shipmentService;
    private final ShipmentMapper shipmentMapper;

    @GetMapping("/{id}")
    @PreAuthorize( "hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ShipmentDto getShipmentById(@PathVariable @Positive Long id){
        return shipmentMapper.shipmentToShipmentDto(shipmentService.getShipmentById(id));
    }

    @GetMapping
    @PreAuthorize( "hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public Page<ShipmentDto> getShipments(Pageable pageable, @Valid @RequestBody ShipmentFilter shipmentFilter){
        return shipmentService.getShipments(pageable, shipmentFilter)
                .map(shipmentMapper::shipmentToShipmentDto);
    }

    @PostMapping
    public ShipmentDto createShipment(@Valid @RequestBody ShipmentAddDto shipmentAddDto){
        return shipmentMapper.shipmentToShipmentDto(shipmentService.createShipment(shipmentAddDto));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ShipmentDto getShipmentByTrackingNumber(@PathVariable @Positive Integer trackingNumber){
        return shipmentMapper.shipmentToShipmentDto(shipmentService.getShipmentByTrackingNumber(trackingNumber));
    }

    @PutMapping("/{id}")
    public ShipmentDto updateShipment(@PathVariable @Positive Long id, @Valid @RequestBody ShipmentAddDto shipmentAddDto){
        return shipmentMapper.shipmentToShipmentDto(shipmentService.updateShipment(id,shipmentAddDto));
    }

    @PatchMapping("/{id}/cancel")
    public ShipmentDto cancelShipment(@PathVariable @Positive Long id){
        return shipmentMapper.shipmentToShipmentDto(shipmentService.cancelShipment(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize( "hasRole('ROLE_ADMIN')")
    public void deleteShipment(@PathVariable @Positive Long id){
        shipmentService.deleteShipment(id);
    }

    @PatchMapping("{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COURIER','ROLE_OPERATOR')")
    public ShipmentDto updateStatus(@PathVariable @Positive Long id,@RequestParam @NotNull Status status,@RequestParam(required = false) @Size(max = 500) String note){
        return shipmentMapper.shipmentToShipmentDto(shipmentService.updateStatus(id,status,note));
    }

    @GetMapping("{id}/status-history")
    public List<StatusHistory> getStatusHistory(@PathVariable @Positive Long id){
        return shipmentService.getStatusHistory(id);
    }
}
