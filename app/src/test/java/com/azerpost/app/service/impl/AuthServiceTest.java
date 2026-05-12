package com.azerpost.app.service.impl;

import com.azerpost.app.exception.AccountDisabledException;
import com.azerpost.app.exception.DuplicateResourceException;
import com.azerpost.app.mapper.UserMapper;
import com.azerpost.app.model.dto.LoginRequest;
import com.azerpost.app.model.dto.RefreshTokenRequest;
import com.azerpost.app.model.dto.ShipmentAddDto;
import com.azerpost.app.model.dto.SignupRequest;
import com.azerpost.app.model.dto.UserDto;
import com.azerpost.app.model.entity.Session;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.repository.SessionRepository;
import com.azerpost.app.repository.UserRepository;
import com.azerpost.app.security.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void loginCreatesSessionAndReturnsTokens() {
        LoginRequest request = new LoginRequest();
        request.setUsername("alice");
        request.setPassword("secret");

        User user = enabledUser(7L, "alice", "alice@example.com");
        Session createdSession = new Session();
        createdSession.setId(11L);
        createdSession.setUser(user);

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(createdSession);
        when(jwtService.generateAccessToken(user, 11L)).thenReturn("access-1");
        when(jwtService.generateRefreshToken(user, 11L)).thenReturn("refresh-1");

        ShipmentAddDto.AuthResponse response = authService.login(request);

        assertEquals("access-1", response.getAccessToken());
        assertEquals("refresh-1", response.getRefreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository, times(2)).save(sessionCaptor.capture());
        Session firstPersisted = sessionCaptor.getAllValues().get(0);
        Session rotatedSession = sessionCaptor.getAllValues().get(1);
        assertSame(user, firstPersisted.getUser());
        assertNotNull(firstPersisted.getRefreshToken());
        assertEquals("refresh-1", rotatedSession.getRefreshToken());
    }

    @Test
    void loginRejectsDisabledUser() {
        LoginRequest request = new LoginRequest();
        request.setUsername("alice");
        request.setPassword("secret");

        User user = enabledUser(7L, "alice", "alice@example.com");
        user.setStatus(false);

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        assertThrows(AccountDisabledException.class, () -> authService.login(request));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void signupRejectsDuplicateEmail() {
        SignupRequest request = new SignupRequest();
        request.setUsername("alice");
        request.setPassword("password123");
        request.setEmail("alice@example.com");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateResourceException.class, () -> authService.signup(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void refreshTokenRotatesTokensForExistingSession() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-1");

        User user = enabledUser(7L, "alice", "alice@example.com");
        Session session = new Session();
        session.setId(11L);
        session.setUser(user);
        session.setRefreshToken("refresh-1");

        when(sessionRepository.findByRefreshToken("refresh-1")).thenReturn(Optional.of(session));
        when(jwtService.isTokenValid("refresh-1", user)).thenReturn(true);
        when(jwtService.generateAccessToken(user, 11L)).thenReturn("access-2");
        when(jwtService.generateRefreshToken(user, 11L)).thenReturn("refresh-2");

        ShipmentAddDto.AuthResponse response = authService.refreshToken(request);

        assertEquals("access-2", response.getAccessToken());
        assertEquals("refresh-2", response.getRefreshToken());
        assertEquals("refresh-2", session.getRefreshToken());
        verify(sessionRepository).save(session);
    }

    @Test
    void logoutDeletesOnlyCurrentSession() {
        User user = enabledUser(7L, "alice", "alice@example.com");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        when(jwtService.extractSessionId("access-token")).thenReturn(11L);
        when(sessionRepository.existsByIdAndUserId(11L, 7L)).thenReturn(true);

        authService.logout("Bearer access-token");

        verify(sessionRepository).deleteById(11L);
    }

    @Test
    void whoAmIReturnsMappedCurrentUser() {
        User user = enabledUser(7L, "alice", "alice@example.com");
        UserDto dto = new UserDto();
        dto.setUsername("alice");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(dto);

        UserDto result = authService.whoAmI();

        assertSame(dto, result);
    }

    private User enabledUser(Long id, String username, String email) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("encoded");
        user.setStatus(true);
        return user;
    }
}
