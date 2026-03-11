package com.azerpost.app.controller;

import com.azerpost.app.model.AuthResponse;
import com.azerpost.app.model.dto.LoginRequest;
import com.azerpost.app.model.dto.RefreshTokenRequest;
import com.azerpost.app.model.dto.SignupRequest;
import com.azerpost.app.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request){
        return authService.signup(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request){
        return authService.refreshToken(request);
    }
}

