package com.azerpost.app.service.impl;

import com.azerpost.app.model.dto.PricingRuleResponse;
import com.azerpost.app.model.dto.StatisticsDto;
import com.azerpost.app.model.dto.UserDto;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.StatusHistory;
import com.azerpost.app.model.enums.Role;
import com.azerpost.app.model.enums.Status;
import com.azerpost.app.repository.ReturnRepository;
import com.azerpost.app.repository.ShipmentRepository;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ShipmentServiceImpl shipmentService;
    private final PricingServiceImpl pricingService;
    private final UserServiceImpl userService;
    private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;
    private final ReturnRepository returnRepository;

    @Override
    public StatisticsDto getDashboard() {
        List<UserDto> users = userService.getAllUsers();
        StatisticsDto statisticsDto = new StatisticsDto();
        List<Shipment> shipments = shipmentRepository.findAll();

        statisticsDto.setTotalUser(users.size());
        statisticsDto.setTotalShipment(shipments.size());
        statisticsDto.setTotalCourier(userRepository.findByAuthority(Role.ROLE_COURIER).size());
        statisticsDto.setTotalOperator(userRepository.findByAuthority(Role.ROLE_OPERATOR).size());
        statisticsDto.setTotalReturn(Math.toIntExact(returnRepository.count()));
        statisticsDto.setSuccessShipment((int) shipments.stream()
                .filter(shipment -> shipment.getStatus() == Status.DELIVERED)
                .count());
        statisticsDto.setFailedShipment((int) shipments.stream()
                .filter(shipment -> shipment.getStatus() == Status.DELIVERY_ATTEMPT_FAILED)
                .count());

        return statisticsDto;
    }

    @Override
    public List<StatusHistory> getAuditLogs(Long shipmentId) {
        return shipmentService.getStatusHistory(shipmentId);
    }

    @Override
    public List<PricingRuleResponse> getSettings() {
        return pricingService.getRules();
    }
}
