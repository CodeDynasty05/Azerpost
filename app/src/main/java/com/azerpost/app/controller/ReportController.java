package com.azerpost.app.controller;

import com.azerpost.app.model.entity.ReturnEntity;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/deliveries")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR','ROLE_ADMIN')")
    public Page<Shipment> getDeliveries(Pageable pageable) {
        return reportService.getDeliveries(pageable);
    }

    @GetMapping("/failures")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR','ROLE_ADMIN')")
    public Page<Shipment> getFailures(Pageable pageable) {
        return reportService.getFailures(pageable);
    }

    @GetMapping("/returns")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR','ROLE_ADMIN')")
    public List<ReturnEntity> getReturns() {
        return reportService.getReturns();
    }

    @GetMapping("/couriers/performance")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR','ROLE_ADMIN')")
    public List<User> getCouriersPerformance() {
        return reportService.getCouriersPerformance();
    }
}
