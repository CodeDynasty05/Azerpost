package com.azerpost.app.controller;

import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/shipments")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<Shipment> getAllShipmentsOfCustomer(){
        return customerService.getAllShipmentsOfCustomer();
    }
}
