package com.azerpost.app.controller;

import com.azerpost.app.model.entity.ProofOfDelivery;
import com.azerpost.app.model.dto.ProofOfDeliveryAddRequest;
import com.azerpost.app.service.ProofOfDeliveryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proof-of-delivery")
@Validated
public class ProofOfDeliveryController {
    private final ProofOfDeliveryService proofOfDeliveryService;

    @PostMapping
    @PreAuthorize( "hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ProofOfDelivery addProofOfDelivery(@Valid @RequestBody ProofOfDeliveryAddRequest request){
        return proofOfDeliveryService.addProofOfDelivery(request);
    }

    @GetMapping("/{shipmentId}")
    @PreAuthorize( "hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ProofOfDelivery getProofByShipmentId(@PathVariable @Positive Long shipmentId){
        return proofOfDeliveryService.getProofByShipmentId(shipmentId);
    }
}
