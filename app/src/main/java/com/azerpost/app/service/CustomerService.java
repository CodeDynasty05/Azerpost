package com.azerpost.app.service;

import com.azerpost.app.model.entity.Shipment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    List<Shipment> getAllShipmentsOfCustomer();
}
