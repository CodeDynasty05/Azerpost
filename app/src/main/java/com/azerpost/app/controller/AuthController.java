package com.azerpost.app.controller;

import com.azerpost.app.model.dto.*;
import com.azerpost.app.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ShipmentAddDto.AuthResponse login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/register")
    public ShipmentAddDto.AuthResponse signup(@Valid @RequestBody SignupRequest request){
        return authService.signup(request);
    }

    @PostMapping("/refresh")
    public ShipmentAddDto.AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request){
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authorizationHeader){
        authService.logout(authorizationHeader);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public UserDto whoAmI(){
        return authService.whoAmI();
    }
}
