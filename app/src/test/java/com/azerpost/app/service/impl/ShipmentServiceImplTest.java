package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ShipmentNotFoundException;
import com.azerpost.app.mapper.ShipmentMapper;
import com.azerpost.app.model.dto.AddressDto;
import com.azerpost.app.model.dto.PricingCalculateResponse;
import com.azerpost.app.model.dto.ShipmentAddDto;
import com.azerpost.app.model.entity.Address;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.StatusHistory;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.model.enums.ShipmentType;
import com.azerpost.app.model.enums.Status;
import com.azerpost.app.repository.ShipmentRepository;
import com.azerpost.app.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {

    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private ShipmentMapper shipmentMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PricingServiceImpl pricingService;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createShipmentAssignsUserAddressPriceAndStatusHistory() {
        ShipmentAddDto request = new ShipmentAddDto();
        request.setWeight(2.0);
        request.setDistance(12.0);
        request.setShipmentType(ShipmentType.STANDARD);
        request.setAddress(new AddressDto());

        Shipment shipment = new Shipment();
        shipment.setId(15L);

        User user = new User();
        user.setUsername("alice");
        user.setStatus(true);

        Address address = new Address();
        PricingCalculateResponse pricing = new PricingCalculateResponse();
        pricing.setPrice(19.5);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("alice", null, List.of())
        );

        when(shipmentMapper.shipmentDtoToShipment(request)).thenReturn(shipment);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(pricingService.resolveAndSaveAddress(request.getAddress())).thenReturn(address);
        when(pricingService.calculate(any())).thenReturn(pricing);
        when(shipmentRepository.existsByTrackingNumber(any())).thenReturn(false);
        when(shipmentRepository.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(shipmentRepository.findById(15L)).thenReturn(Optional.of(shipment));

        Shipment result = shipmentService.createShipment(request);

        assertSame(shipment, result);
        assertSame(user, shipment.getSender());
        assertSame(address, shipment.getAddress());
        assertEquals(19.5, shipment.getPrice());
        assertEquals(Status.REGISTERED, shipment.getStatus());
        assertEquals(3, shipment.getStatusHistory().size());
    }

    @Test
    void cancelShipmentAllowsRegisteredStatus() {
        Shipment shipment = new Shipment();
        shipment.setId(8L);
        shipment.setStatus(Status.REGISTERED);

        when(shipmentRepository.findById(8L)).thenReturn(Optional.of(shipment));
        when(shipmentRepository.save(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Shipment result = shipmentService.cancelShipment(8L);

        assertSame(shipment, result);
        assertEquals(Status.CANCELLED, shipment.getStatus());
        assertEquals(1, shipment.getStatusHistory().size());
        assertEquals("Shipment cancelled by user", shipment.getStatusHistory().get(0).getNote());
    }

    @Test
    void cancelShipmentRejectsDeliveredStatus() {
        Shipment shipment = new Shipment();
        shipment.setId(8L);
        shipment.setStatus(Status.DELIVERED);

        when(shipmentRepository.findById(8L)).thenReturn(Optional.of(shipment));

        assertThrows(IllegalArgumentException.class, () -> shipmentService.cancelShipment(8L));
    }

    @Test
    void updateStatusThrowsWhenShipmentIsMissing() {
        when(shipmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ShipmentNotFoundException.class,
                () -> shipmentService.updateStatus(99L, Status.CANCELLED, "note"));
    }

    @Test
    void getStatusHistoryReturnsPersistedHistory() {
        Shipment shipment = new Shipment();
        StatusHistory first = new StatusHistory();
        StatusHistory second = new StatusHistory();
        shipment.getStatusHistory().add(first);
        shipment.getStatusHistory().add(second);

        when(shipmentRepository.findWithHistoryById(5L)).thenReturn(Optional.of(shipment));

        List<StatusHistory> result = shipmentService.getStatusHistory(5L);

        assertEquals(2, result.size());
        assertSame(first, result.get(0));
        assertSame(second, result.get(1));
    }

    @Test
    void getShipmentByTrackingNumberThrowsWhenMissing() {
        when(shipmentRepository.findByTrackingNumber(123456789)).thenReturn(Optional.empty());

        assertThrows(ShipmentNotFoundException.class,
                () -> shipmentService.getShipmentByTrackingNumber(123456789));
    }
}
