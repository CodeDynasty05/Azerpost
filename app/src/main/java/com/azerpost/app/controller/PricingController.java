package com.azerpost.app.controller;

import com.azerpost.app.model.dto.PricingCalculateRequest;
import com.azerpost.app.model.dto.PricingCalculateResponse;
import com.azerpost.app.model.dto.PricingRuleRequest;
import com.azerpost.app.model.dto.PricingRuleResponse;
import com.azerpost.app.service.PricingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
@Validated
public class PricingController {

    private final PricingService pricingService;

    @PostMapping("/calculate")
    public PricingCalculateResponse calculate(@Valid @RequestBody PricingCalculateRequest request) {
        return pricingService.calculate(request);
    }

    @GetMapping("/rules")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PricingRuleResponse> getRules() {
        return pricingService.getRules();
    }

    @PostMapping("/rules")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PricingRuleResponse createRule(@Valid @RequestBody PricingRuleRequest request) {
        return pricingService.createRule(request);
    }

    @PutMapping("/rules/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PricingRuleResponse updateRule(
            @PathVariable @Positive Long id,
            @Valid @RequestBody PricingRuleRequest request
    ) {
        return pricingService.updateRule(id, request);
    }

    @PatchMapping("/rules/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PricingRuleResponse changeStatus(@PathVariable @Positive Long id) {
        return pricingService.changeStatus(id);
    }
}
