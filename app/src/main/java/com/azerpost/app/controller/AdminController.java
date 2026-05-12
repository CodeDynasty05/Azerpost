package com.azerpost.app.controller;

import com.azerpost.app.model.dto.PricingRuleResponse;
import com.azerpost.app.model.dto.StatisticsDto;
import com.azerpost.app.model.entity.PricingRule;
import com.azerpost.app.model.entity.StatusHistory;
import com.azerpost.app.service.AdminService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Validated
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/dashboard")
    public StatisticsDto getDashboard(){
        return adminService.getDashboard();
    }

    @GetMapping("/audit-logs")
    public List<StatusHistory> getAuditLogs(@RequestParam @Positive Long shipmentId){
        return adminService.getAuditLogs(shipmentId);
    }

    @GetMapping("/settings")
    public List<PricingRuleResponse> getSettings(){
        return adminService.getSettings();
    }
}
