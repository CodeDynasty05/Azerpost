package com.azerpost.app.service.impl;

import com.azerpost.app.model.Transfer;
import com.azerpost.app.model.TransferResponse;
import com.azerpost.app.model.TransferStatus;
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
    public Page<TransferResponse> getTransfers(String status, Pageable pageable) {

        TransferStatus transferStatus = null;

        if (status != null && !status.isBlank()) {
            try {
                transferStatus = TransferStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid transfer status: " + status);
            }
        }

        Page<Transfer> transfers;

        if(transferStatus != null){
            transfers = transferRepository.findByStatus(
                   transferStatus,
                    pageable
            );
        } else {
            transfers = transferRepository.findAll(pageable);
        }

        return transfers.map(this::mapToResponse);
    }

    private TransferResponse mapToResponse(Transfer transfer){
        TransferResponse response = new TransferResponse();

        response.setId(transfer.getId());
        response.setReceiverName(transfer.getReceiverName());
        response.setDescription(transfer.getDescription());
        response.setAmount(transfer.getAmount());
        response.setDate(transfer.getTransferDate().toString());
        response.setStatus(transfer.getStatus().name());

        return response;
    }
}

