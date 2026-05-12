package com.azerpost.app.service;

import com.azerpost.app.model.dto.PricingCalculateRequest;
import com.azerpost.app.model.dto.PricingCalculateResponse;
import com.azerpost.app.model.dto.PricingRuleRequest;
import com.azerpost.app.model.dto.PricingRuleResponse;

import java.util.List;

public interface PricingService {
    PricingCalculateResponse calculate(PricingCalculateRequest request);

    List<PricingRuleResponse> getRules();

    PricingRuleResponse createRule(PricingRuleRequest request);

    PricingRuleResponse updateRule(Long id, PricingRuleRequest request);

    PricingRuleResponse changeStatus(Long id);
}
