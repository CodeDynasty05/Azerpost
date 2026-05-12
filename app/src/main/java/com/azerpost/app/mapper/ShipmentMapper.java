package com.azerpost.app.mapper;

import com.azerpost.app.model.entity.Address;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.dto.AddressDto;
import com.azerpost.app.model.dto.ShipmentAddDto;
import com.azerpost.app.model.dto.ShipmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ShipmentMapper {

    @Mapping(target = "address", source = "address")
    Shipment shipmentDtoToShipment(ShipmentAddDto shipmentAddDto);

    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderUsername", source = "sender.username")
    ShipmentDto shipmentToShipmentDto(Shipment shipment);

    Address addressDtoToAddress(AddressDto addressDto);

    void updateShipmentFromDto(ShipmentAddDto shipmentAddDto, @MappingTarget Shipment shipment);

}
