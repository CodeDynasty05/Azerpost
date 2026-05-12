package com.azerpost.app.controller;

import com.azerpost.app.model.dto.AssignmentRequest;
import com.azerpost.app.model.dto.AssignmentResponse;
import com.azerpost.app.service.AssignmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
@Validated
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public AssignmentResponse createAssignment(@Valid @RequestBody AssignmentRequest request) {
        return assignmentService.createAssignment(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public AssignmentResponse updateAssignment(
            @PathVariable @Positive Long id,
            @Valid @RequestBody AssignmentRequest request
    ) {
        return assignmentService.updateAssignment(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public AssignmentResponse getAssignment(@PathVariable @Positive Long id) {
        return assignmentService.getAssignment(id);
    }

    @GetMapping("/courier/{courierId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public List<AssignmentResponse> getAssignmentsByCourier(@PathVariable @Positive Long courierId) {
        return assignmentService.getAssignmentsByCourier(courierId);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_COURIER')")
    public List<AssignmentResponse> getMyAssignments() {
        return assignmentService.getMyAssignments();
    }
}
