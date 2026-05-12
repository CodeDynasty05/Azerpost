package com.azerpost.app.mapper;

import com.azerpost.app.model.entity.ProofOfDelivery;
import com.azerpost.app.model.dto.ProofOfDeliveryAddRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProofOfDeliveryMapper {
    ProofOfDelivery toEntity(ProofOfDeliveryAddRequest request);
}
