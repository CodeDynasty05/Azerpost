package com.azerpost.app.service;

import com.azerpost.app.model.Transfer;
import com.azerpost.app.model.TransferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TransferService {

        Page<TransferResponse> getTransfers(String status, Pageable pageable);

}
