package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ResourceNotFoundException;
import com.azerpost.app.model.User;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
