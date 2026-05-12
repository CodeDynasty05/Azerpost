package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ResourceNotFoundException;
import com.azerpost.app.mapper.PricingMapper;
import com.azerpost.app.mapper.ShipmentMapper;
import com.azerpost.app.model.entity.Address;
import com.azerpost.app.model.entity.PricingRule;
import com.azerpost.app.model.dto.*;
import com.azerpost.app.repository.AddressRepository;
import com.azerpost.app.repository.PricingRuleRepository;
import com.azerpost.app.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final PricingRuleRepository pricingRuleRepository;
    private final AddressRepository addressRepository;
    private final ShipmentMapper shipmentMapper;
    private final PricingMapper pricingMapper;

    @Override
    public PricingCalculateResponse calculate(PricingCalculateRequest request) {
        PricingRule rule = pricingRuleRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No active pricing rule found"));

        if (request.getWeight() == null) {
            throw new IllegalArgumentException("Weight is required");
        }
        if (request.getDistance() == null) {
            throw new IllegalArgumentException("Distance is required");
        }
        if (request.getShipmentType() == null) {
            throw new IllegalArgumentException("Shipment type is required");
        }
        if (request.getAddress() == null) {
            throw new IllegalArgumentException("Address is required");
        }

        Address address = resolveAndSaveAddress(request.getAddress());

        double weightPrice = resolveWeightPrice(rule, request.getWeight());
        double distancePrice = request.getDistance() * rule.getDistancePricePerKm();
        double zonePrice = resolveZonePrice(rule, address.getTariffZone());
        double expressSurcharge = request.getShipmentType().name().equals("EXPRESS")
                ? rule.getExpressSurcharge()
                : 0.0;

        double price = weightPrice + distancePrice + zonePrice + expressSurcharge;

        PricingCalculateResponse response = new PricingCalculateResponse();
        response.setPrice(price);
        response.setRuleLabel(rule.getLabel());
        return response;
    }

    @Override
    public List<PricingRuleResponse> getRules() {
        return pricingRuleRepository.findAll()
                .stream()
                .map(pricingMapper::toResponse)
                .toList();
    }

    @Override
    public PricingRuleResponse createRule(PricingRuleRequest request) {
        PricingRule rule = pricingMapper.toEntity(request);

        if (Boolean.TRUE.equals(request.getActive())) {
            disableAllRules();
        }

        return pricingMapper.toResponse(pricingRuleRepository.save(rule));
    }

    @Override
    public PricingRuleResponse updateRule(Long id, PricingRuleRequest request) {
        PricingRule rule = pricingRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pricing rule not found"));

        pricingMapper.updateEntityFromRequest(request, rule);

        if (Boolean.TRUE.equals(request.getActive())) {
            disableAllRules();
            rule.setActive(true);
        }

        return pricingMapper.toResponse(pricingRuleRepository.save(rule));
    }

    @Override
    public PricingRuleResponse changeStatus(Long id) {
        PricingRule rule = pricingRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pricing rule not found"));

        disableAllRules();
        rule.setActive(true);

        return pricingMapper.toResponse(pricingRuleRepository.save(rule));
    }

    protected Address resolveAndSaveAddress(AddressDto dto) {
        Address address = shipmentMapper.addressDtoToAddress(dto);
        address.setTariffZone(resolveTariffZone(dto));
        return addressRepository.save(address);
    }

    protected String resolveTariffZone(AddressDto dto) {
        if (dto.getCountry() == null || dto.getCountry().isBlank()) {
            return "DEFAULT";
        }

        String country = dto.getCountry().trim().toUpperCase();

        if ("AZERBAIJAN".equals(country) || "AZ".equals(country)) {
            return "LOCAL";
        }

        if ("TURKEY".equals(country) || "TR".equals(country) || "GEORGIA".equals(country) || "GE".equals(country)) {
            return "REGIONAL";
        }

        return "INTERNATIONAL";
    }

    protected double resolveWeightPrice(PricingRule rule, Double weight) {
        if (weight < rule.getFirstTWeightKg()) {
            return rule.getFirstTWeightPrice();
        }
        if (weight <= rule.getSecondTWeightKg()) {
            return rule.getSecondTWeightPrice();
        }
        return rule.getThirdTWeightPrice();
    }

    protected double resolveZonePrice(PricingRule rule, String tariffZone) {
        return switch (tariffZone.trim().toUpperCase()) {
            case "LOCAL" -> rule.getLocalZonePrice();
            case "REGIONAL" -> rule.getRegionalZonePrice();
            case "INTERNATIONAL" -> rule.getInternationalZonePrice();
            default -> throw new IllegalArgumentException("Unknown tariff zone: " + tariffZone);
        };
    }

    protected void disableAllRules() {
        List<PricingRule> rules = pricingRuleRepository.findAll();
        for (PricingRule rule : rules) {
            rule.setActive(false);
        }
        pricingRuleRepository.saveAll(rules);
    }
}
