package com.azerpost.app.controller;

import com.azerpost.app.model.dto.ProofOfDeliveryAddRequest;
import com.azerpost.app.model.enums.TransferStatus;
import com.azerpost.app.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @GetMapping
    public Page<ProofOfDeliveryAddRequest.TransferResponse> getTransfers(
            @RequestParam(required = false) TransferStatus status,
            Pageable pageable
    ) {
        return transferService.getTransfers(status, pageable);
    }

}

