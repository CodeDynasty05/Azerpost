package com.azerpost.app.security;

import com.azerpost.app.model.entity.User;
import com.azerpost.app.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private final SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    private final JwtService jwtService =
            new JwtService("PABqfpxFqflPT+WQ3+F8wEB6kwa88RZX8hy88LDRyhc=", sessionRepository);

    @Test
    void tokenBecomesInvalidWhenSessionIsDeleted() {
        User user = new User();
        user.setId(7L);
        user.setUsername("alice");

        when(sessionRepository.existsByIdAndUserId(11L, 7L)).thenReturn(true, false);

        String accessToken = jwtService.generateAccessToken(user, 11L);

        assertTrue(jwtService.isTokenValid(accessToken, user));

        assertFalse(jwtService.isTokenValid(accessToken, user));
    }
}
