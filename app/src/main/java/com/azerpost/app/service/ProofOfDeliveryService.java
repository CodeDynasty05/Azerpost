package com.azerpost.app.service;

import com.azerpost.app.model.entity.ProofOfDelivery;
import com.azerpost.app.model.dto.ProofOfDeliveryAddRequest;
import org.springframework.stereotype.Service;

@Service
public interface ProofOfDeliveryService {
    ProofOfDelivery addProofOfDelivery(ProofOfDeliveryAddRequest request);

    ProofOfDelivery getProofByShipmentId(Long shipmentId);
}
