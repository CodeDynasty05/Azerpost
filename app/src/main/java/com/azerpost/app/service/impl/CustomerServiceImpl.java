package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ResourceNotFoundException;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.service.CustomerService;
import com.azerpost.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;

    @Override
    public List<Shipment> getAllShipmentsOfCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepository.findByUsernameWithShipments(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));

        return user.getShipments();
    }
}
