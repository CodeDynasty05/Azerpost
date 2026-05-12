package com.azerpost.app.service.impl;

import com.azerpost.app.exception.AccountDisabledException;
import com.azerpost.app.exception.DuplicateResourceException;
import com.azerpost.app.exception.ResourceNotFoundException;
import com.azerpost.app.mapper.UserMapper;
import com.azerpost.app.model.dto.LoginRequest;
import com.azerpost.app.model.dto.RefreshTokenRequest;
import com.azerpost.app.model.dto.ShipmentAddDto;
import com.azerpost.app.model.dto.SignupRequest;
import com.azerpost.app.model.dto.UserDto;
import com.azerpost.app.model.entity.Authority;
import com.azerpost.app.model.entity.Session;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.model.enums.Role;
import com.azerpost.app.repository.SessionRepository;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public ShipmentAddDto.AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new AccountDisabledException("Account disabled. Please contact support.");
        }

        Session session = createSession(user);
        String accessToken = jwtService.generateAccessToken(user, session.getId());
        String refreshToken = jwtService.generateRefreshToken(user, session.getId());

        session.setRefreshToken(refreshToken);
        sessionRepository.save(session);

        ShipmentAddDto.AuthResponse response = new ShipmentAddDto.AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public ShipmentAddDto.AuthResponse signup(SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException(
                    "Username '" + request.getUsername() + "' already exists"
            );
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException(
                    "Email '" + request.getEmail() + "' already exists"
            );
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setStatus(true);

        Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_USER);
        authority.setUser(newUser);
        newUser.getAuthorities().add(authority);

        User savedUser = userRepository.save(newUser);
        Session session = createSession(savedUser);

        String accessToken = jwtService.generateAccessToken(savedUser, session.getId());
        String refreshToken = jwtService.generateRefreshToken(savedUser, session.getId());

        session.setRefreshToken(refreshToken);
        sessionRepository.save(session);

        ShipmentAddDto.AuthResponse response = new ShipmentAddDto.AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public ShipmentAddDto.AuthResponse refreshToken(RefreshTokenRequest request) {
        String incomingRefreshToken = request.getRefreshToken();
        Session session = sessionRepository.findByRefreshToken(incomingRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        User user = session.getUser();
        if (!user.isEnabled()) {
            throw new AccountDisabledException("Account disabled. Please contact support.");
        }
        if (!jwtService.isTokenValid(incomingRefreshToken, user)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateAccessToken(user, session.getId());
        String newRefreshToken = jwtService.generateRefreshToken(user, session.getId());

        session.setRefreshToken(newRefreshToken);
        sessionRepository.save(session);

        ShipmentAddDto.AuthResponse response = new ShipmentAddDto.AuthResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        return response;
    }

    public void logout(String authorizationHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ResourceNotFoundException("User not found");
        }
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is required");
        }
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new ResourceNotFoundException("User not found");
        }

        String token = authorizationHeader.substring(7);
        Long sessionId = jwtService.extractSessionId(token);
        if (sessionId == null || !sessionRepository.existsByIdAndUserId(sessionId, user.getId())) {
            throw new IllegalArgumentException("Invalid access token");
        }

        sessionRepository.deleteById(sessionId);
    }

    public UserDto whoAmI() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ResourceNotFoundException("User not found");
        }
        String username = authentication.getName();

        return userMapper.userToUserDto(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"))
        );
    }

    private Session createSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setRefreshToken("pending-" + UUID.randomUUID());
        return sessionRepository.save(session);
    }
}
