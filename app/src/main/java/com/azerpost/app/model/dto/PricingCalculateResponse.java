package com.azerpost.app.model.dto;

import lombok.Data;

@Data
public class PricingCalculateResponse {
    private Double price;
    private String ruleLabel;
}
