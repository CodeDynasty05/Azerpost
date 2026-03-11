package com.azerpost.app.service.impl;

import com.azerpost.app.exception.DuplicateResourceException;
import com.azerpost.app.model.AuthResponse;
import com.azerpost.app.model.User;
import com.azerpost.app.model.dto.LoginRequest;
import com.azerpost.app.model.dto.RefreshTokenRequest;
import com.azerpost.app.model.dto.SignupRequest;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

    public AuthResponse signup(SignupRequest request){

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException(
                    "Username '" + request.getUsername() + "' already exists"
            );
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setRole("USER");

        userRepository.save(newUser);

        UserDetails user = userDetailsService.loadUserByUsername(newUser.getUsername());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

    public AuthResponse refreshToken(RefreshTokenRequest request){

        String username = jwtService.extractUsername(request.getRefreshToken());

        UserDetails user = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtService.generateAccessToken(user);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(request.getRefreshToken());

        return response;
    }
}
