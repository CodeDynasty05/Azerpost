package com.azerpost.app.mapper;

import com.azerpost.app.model.entity.PricingRule;
import com.azerpost.app.model.dto.PricingRuleRequest;
import com.azerpost.app.model.dto.PricingRuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PricingMapper {

    PricingRule toEntity(PricingRuleRequest request);

    PricingRuleResponse toResponse(PricingRule rule);

    void updateEntityFromRequest(PricingRuleRequest request, @MappingTarget PricingRule rule);
}
