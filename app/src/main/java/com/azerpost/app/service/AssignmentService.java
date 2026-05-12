package com.azerpost.app.service;

import com.azerpost.app.model.dto.AssignmentRequest;
import com.azerpost.app.model.dto.AssignmentResponse;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse createAssignment(AssignmentRequest request);

    AssignmentResponse updateAssignment(Long id, AssignmentRequest request);

    AssignmentResponse getAssignment(Long id);

    List<AssignmentResponse> getAssignmentsByCourier(Long courierId);

    List<AssignmentResponse> getMyAssignments();
}