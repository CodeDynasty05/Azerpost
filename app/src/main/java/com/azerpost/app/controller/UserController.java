package com.azerpost.app.controller;

import com.azerpost.app.model.dto.SignupRequest;
import com.azerpost.app.model.dto.UserDto;
import com.azerpost.app.model.enums.Role;
import com.azerpost.app.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto getUser(@PathVariable @Positive Long id){
        return userService.getUser(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable @Positive Long id, @Valid @RequestBody SignupRequest updateRequest){
        return userService.updateUser(id, updateRequest);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto updateUserStatus(@PathVariable @Positive Long id, @RequestParam("status") @NotNull Boolean status){
        return userService.updateUserStatus(id, status);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto updateUserRole(@PathVariable @Positive Long id, @RequestParam("role") @NotNull Role role){
        return userService.updateRole(id,role);
    }

    @GetMapping("/couriers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDto> getCouriers(){
        return userService.getCouriers();
    }
}
