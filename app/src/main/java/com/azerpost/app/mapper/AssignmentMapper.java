package com.azerpost.app.mapper;

import com.azerpost.app.model.entity.Assignment;
import com.azerpost.app.model.dto.AssignmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AssignmentMapper {

    @Mapping(target = "shipmentId", source = "shipment.id")
    @Mapping(target = "trackingNumber", source = "shipment.trackingNumber")
    AssignmentResponse toResponse(Assignment assignment);
}
