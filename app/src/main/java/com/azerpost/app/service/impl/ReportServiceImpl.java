package com.azerpost.app.service.impl;

import com.azerpost.app.model.dto.ShipmentFilter;
import com.azerpost.app.model.entity.ReturnEntity;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.model.enums.Role;
import com.azerpost.app.model.enums.Status;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.service.ReportService;
import com.azerpost.app.service.ReturnService;
import com.azerpost.app.service.ShipmentService;
import com.azerpost.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ShipmentService shipmentService;
    private final ReturnService returnService;
    private final UserRepository userRepository;

    @Override
    public Page<Shipment> getDeliveries(Pageable pageable) {
        ShipmentFilter shipmentFilter = new ShipmentFilter();
        shipmentFilter.setStatus(Status.DELIVERED);
        return shipmentService.getShipments(pageable,shipmentFilter);
    }

    @Override
    public Page<Shipment> getFailures(Pageable pageable) {
        ShipmentFilter shipmentFilter = new ShipmentFilter();
        shipmentFilter.setStatus(Status.DELIVERY_ATTEMPT_FAILED);
        return shipmentService.getShipments(pageable,shipmentFilter);
    }

    @Override
    public List<ReturnEntity> getReturns() {
        return  returnService.getAllReturns();
    }

    @Override
    public List<User> getCouriersPerformance() {
        return userRepository.findByAuthority(Role.ROLE_COURIER);
    }
}
