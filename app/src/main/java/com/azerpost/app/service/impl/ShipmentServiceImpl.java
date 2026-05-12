package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ShipmentNotFoundException;
import com.azerpost.app.mapper.ShipmentMapper;
import com.azerpost.app.model.dto.*;
import com.azerpost.app.model.entity.*;
import com.azerpost.app.model.enums.Status;
import com.azerpost.app.repository.ShipmentRepository;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.service.ShipmentService;
import com.azerpost.app.specification.ShipmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final UserRepository userRepository;
    private final PricingServiceImpl pricingService;

    @Override
    public Shipment getShipmentById(Long id) {
        return findShipmentById(id);
    }

    @Override
    public Page<Shipment> getShipments(Pageable pageable, ShipmentFilter shipmentFilter) {
        Specification<Shipment> spec = ShipmentSpecification.withDynamicQuery(
                shipmentFilter.getStatus(),
                shipmentFilter.getReceiverName(),
                shipmentFilter.getShipmentType(),
                shipmentFilter.getMinPrice(),
                shipmentFilter.getMaxPrice()
        );
        return shipmentRepository.findAll(spec, pageable);
    }

    @Override
    public Shipment createShipment(ShipmentAddDto shipmentAddDto) {
        Shipment shipment = shipmentMapper.shipmentDtoToShipment(shipmentAddDto);

        shipment.setDeliveryDate(LocalDateTime.now().plusDays(14));
        shipment.setTrackingNumber(generateTrackingNumber());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Address address = pricingService.resolveAndSaveAddress(shipmentAddDto.getAddress());

        PricingCalculateRequest pricingRequest = new PricingCalculateRequest();
        pricingRequest.setWeight(shipmentAddDto.getWeight());
        pricingRequest.setDistance(shipmentAddDto.getDistance());
        pricingRequest.setShipmentType(shipmentAddDto.getShipmentType());
        pricingRequest.setAddress(shipmentAddDto.getAddress());

        PricingCalculateResponse pricingResponse = pricingService.calculate(pricingRequest);

        shipment.setSender(user);
        shipment.setAddress(address);
        shipment.setPrice(pricingResponse.getPrice());

        shipment = shipmentRepository.save(shipment);
        updateStatus(shipment.getId(), Status.CREATED, "Shipment created by user");
        updateStatus(shipment.getId(), Status.PRICE_CALCULATED,"Price calculated by system");
        return updateStatus(shipment.getId(), Status.REGISTERED, "Shipment registered by system");
    }

    @Override
    public Shipment getShipmentByTrackingNumber(Integer trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ShipmentNotFoundException(
                        "Shipment not found with tracking number: " + trackingNumber
                ));
    }

    @Override
    public Shipment updateShipment(Long id, ShipmentAddDto shipmentAddDto) {
        Shipment shipment = findShipmentById(id);
        shipmentMapper.updateShipmentFromDto(shipmentAddDto, shipment);
        return shipmentRepository.save(shipment);
    }

    @Override
    public Shipment cancelShipment(Long id) {
        Status currentStatus = findShipmentById(id).getStatus();
        if (
                currentStatus != Status.CREATED &&
                currentStatus != Status.PRICE_CALCULATED &&
                currentStatus != Status.REGISTERED
        ) {
            throw new IllegalArgumentException("Too Late to cancel shipment");
        }

        return updateStatus(id, Status.CANCELLED, "Shipment cancelled by user");
    }

    @Override
    public void deleteShipment(Long id) {
        shipmentRepository.delete(findShipmentById(id));
    }

    @Override
    public Shipment updateStatus(Long id, Status status, String note) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found"));
        shipment.setStatus(status);
        return setStatusHistory(shipment, status, note);
    }

    @Override
    public List<StatusHistory> getStatusHistory(Long id) {
        Shipment shipment = shipmentRepository.findWithHistoryById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found"));
        return shipment.getStatusHistory();
    }

    private Shipment findShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found"));
    }

    private Integer generateTrackingNumber() {
        int trackingNumber;
        do {
            trackingNumber = ThreadLocalRandom.current().nextInt(100000000, 1000000000);
        } while (shipmentRepository.existsByTrackingNumber(trackingNumber));
        return trackingNumber;
    }

    protected Shipment setStatusHistory(Shipment shipment, Status status,String note) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        StatusHistory history = new StatusHistory();
        history.setShipment(shipment);
        history.setStatus(status);
        history.setNote(note);
        history.setUpdatedBy(user);
        shipment.getStatusHistory().add(history);

        return shipmentRepository.save(shipment);
    }
}
