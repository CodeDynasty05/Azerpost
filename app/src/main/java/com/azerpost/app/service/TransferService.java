package com.azerpost.app.service;

import com.azerpost.app.model.dto.ProofOfDeliveryAddRequest;
import com.azerpost.app.model.enums.TransferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TransferService {

        Page<ProofOfDeliveryAddRequest.TransferResponse> getTransfers(TransferStatus status, Pageable pageable);

}
