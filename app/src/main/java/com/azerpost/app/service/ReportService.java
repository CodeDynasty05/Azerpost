package com.azerpost.app.service;

import com.azerpost.app.model.entity.ReturnEntity;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportService {
    Page<Shipment> getDeliveries(Pageable pageable);

    Page<Shipment> getFailures(Pageable pageable);

    List<ReturnEntity> getReturns();

    List<User> getCouriersPerformance();
}
