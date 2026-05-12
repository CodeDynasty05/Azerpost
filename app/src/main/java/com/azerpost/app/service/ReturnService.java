package com.azerpost.app.service;

import com.azerpost.app.model.entity.ReturnEntity;
import com.azerpost.app.model.dto.ReturnAddRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReturnService {
    ReturnEntity initiateReturn(ReturnAddRequest request);

    ReturnEntity approveReturn(Long id);

    ReturnEntity completeReturn(Long id);

    ReturnEntity getReturn(Long id);

    List<ReturnEntity> getAllReturns();
}
