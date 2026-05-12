package com.azerpost.app.service;

import com.azerpost.app.model.dto.PricingRuleResponse;
import com.azerpost.app.model.dto.StatisticsDto;
import com.azerpost.app.model.entity.PricingRule;
import com.azerpost.app.model.entity.StatusHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    StatisticsDto getDashboard();

    List<StatusHistory> getAuditLogs(Long shipmentId);

    List<PricingRuleResponse> getSettings();
}
