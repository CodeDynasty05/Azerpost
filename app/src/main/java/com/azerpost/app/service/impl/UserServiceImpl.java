package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ResourceNotFoundException;
import com.azerpost.app.mapper.UserMapper;
import com.azerpost.app.model.entity.Authority;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.model.dto.SignupRequest;
import com.azerpost.app.model.dto.UserDto;
import com.azerpost.app.model.enums.Role;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUser(Long id){
        return userMapper.userToUserDto(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id)));
    }

    @Override
    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream().map(userMapper::userToUserDto).toList();
    }

    @Override
    public UserDto updateUser(Long id, SignupRequest updateRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication != null ? authentication.getName() : null;

        if (currentUsername == null || !currentUsername.equals(existingUser.getUsername()) || authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name()))) {
            throw new AccessDeniedException("You can only update your own account");
        }

        userMapper.updateUserFromDto(updateRequest, existingUser);

        if (updateRequest.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        User updated = userRepository.save(existingUser);
        return userMapper.userToUserDto(updated);
    }

    @Override
    public UserDto updateUserStatus(Long id, Boolean status) {
        User user = getUserById(id);
        user.setStatus(status);
        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateRole(Long id, Role role) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        Authority newAuthority = new Authority();
        newAuthority.setAuthority(role);
        newAuthority.setUser(existingUser);
        existingUser.getAuthorities().add(newAuthority);

        User updated = userRepository.save(existingUser);
        return userMapper.userToUserDto(updated);
    }

    @Override
    public List<UserDto> getCouriers() {
        return userRepository.findByAuthority(Role.ROLE_COURIER).stream().map(userMapper::userToUserDto).toList();
    }

    private User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }
}
