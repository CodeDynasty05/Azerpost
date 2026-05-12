package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ProcessForbidden;
import com.azerpost.app.exception.ResourceNotFoundException;
import com.azerpost.app.mapper.AssignmentMapper;
import com.azerpost.app.model.entity.Assignment;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.model.dto.AssignmentRequest;
import com.azerpost.app.model.dto.AssignmentResponse;
import com.azerpost.app.model.enums.Status;
import com.azerpost.app.repository.AssignmentRepository;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ShipmentServiceImpl shipmentService;
    private final UserRepository userRepository;
    private final AssignmentMapper assignmentMapper;

    @Override
    public AssignmentResponse createAssignment(AssignmentRequest request) {
        Shipment shipment = shipmentService.getShipmentById(request.getShipmentId());

        if(shipment.getStatus() != Status.PREPARED){
            throw new ProcessForbidden("Shipment is not in PREPARED status");
        }

        User courier = userRepository.findById(request.getCourierId())
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found"));

        Assignment assignment = assignmentRepository.findByShipmentId(request.getShipmentId())
                .orElse(new Assignment());

        if(!courier.isEnabled()){
            throw new DisabledException("This Courier is not enabled");
        }

        assignment.setShipment(shipment);
        assignment.setCourier(courier);
        shipmentService.updateStatus(request.getShipmentId(), Status.ASSIGNED_TO_COURIER,null);

        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    public AssignmentResponse updateAssignment(Long id, AssignmentRequest request) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        Shipment shipment = shipmentService.getShipmentById(request.getShipmentId());

        if(shipment.getStatus() != Status.ASSIGNED_TO_COURIER){
            throw new ProcessForbidden("Shipment is not in ASSIGNED_TO_COURIER status");
        }

        User courier = userRepository.findById(request.getCourierId())
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found"));

        if(!courier.isEnabled()){
            throw new DisabledException("This Courier is not enabled");
        }

        assignment.setShipment(shipment);
        assignment.setCourier(courier);

        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    public AssignmentResponse getAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
        return assignmentMapper.toResponse(assignment);
    }

    @Override
    public List<AssignmentResponse> getAssignmentsByCourier(Long courierId) {
        return assignmentRepository.findAllByCourierId(courierId)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AssignmentResponse> getMyAssignments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User courier = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found"));

        return assignmentRepository.findAllByCourierId(courier.getId())
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
    }
}