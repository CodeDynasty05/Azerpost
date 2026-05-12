package com.azerpost.app.service.impl;

import com.azerpost.app.model.entity.Transfer;
import com.azerpost.app.model.enums.TransferStatus;
import com.azerpost.app.model.dto.ProofOfDeliveryAddRequest;
import com.azerpost.app.repository.TransferRepository;
import com.azerpost.app.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    @Override
    public Page<ProofOfDeliveryAddRequest.TransferResponse> getTransfers(TransferStatus status, Pageable pageable) {

        Page<Transfer> transfers;

        if(status != null){
            transfers = transferRepository.findByStatus(
                   status,
                    pageable
            );
        } else {
            transfers = transferRepository.findAll(pageable);
        }

        return transfers.map(this::mapToResponse);
    }

    private ProofOfDeliveryAddRequest.TransferResponse mapToResponse(Transfer transfer){
        ProofOfDeliveryAddRequest.TransferResponse response = new ProofOfDeliveryAddRequest.TransferResponse();

        response.setId(transfer.getId());
        response.setReceiverName(transfer.getReceiverName());
        response.setDescription(transfer.getDescription());
        response.setAmount(transfer.getAmount());
        response.setDate(transfer.getTransferDate().toString());
        response.setStatus(transfer.getStatus().name());

        return response;
    }
}

