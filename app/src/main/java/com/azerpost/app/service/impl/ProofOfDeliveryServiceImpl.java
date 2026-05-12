package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ResourceNotFoundException;
import com.azerpost.app.mapper.ProofOfDeliveryMapper;
import com.azerpost.app.model.entity.ProofOfDelivery;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.dto.ProofOfDeliveryAddRequest;
import com.azerpost.app.repository.ProofOfDeliveryRepository;
import com.azerpost.app.service.ProofOfDeliveryService;
import com.azerpost.app.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProofOfDeliveryServiceImpl implements ProofOfDeliveryService {
    private final ProofOfDeliveryRepository proofOfDeliveryRepository;
    private final ProofOfDeliveryMapper proofOfDeliveryMapper;
    private final ShipmentService shipmentService;

    @Override
    public ProofOfDelivery addProofOfDelivery(ProofOfDeliveryAddRequest request) {
        Shipment shipment = shipmentService.getShipmentById(request.getShipmentId());
        ProofOfDelivery proofOfDelivery = proofOfDeliveryMapper.toEntity(request);
        proofOfDelivery.setShipment(shipment);
        shipment.setProofOfDelivery(proofOfDelivery);
        return proofOfDeliveryRepository.save(proofOfDelivery);
    }

    @Override
    public ProofOfDelivery getProofByShipmentId(Long shipmentId) {
        return proofOfDeliveryRepository.getProofOfDeliveryByShipment_Id(shipmentId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Proof of delivery not found"));}
}
