package com.azerpost.app.service;

import com.azerpost.app.model.dto.SignupRequest;
import com.azerpost.app.model.dto.UserDto;
import com.azerpost.app.model.enums.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto getUser(Long id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long id, SignupRequest updateRequest);

    UserDto updateUserStatus(Long id, Boolean status);

    UserDto updateRole(Long id, Role role);

    List<UserDto> getCouriers();
}
