package com.azerpost.app.security;

import com.azerpost.app.model.entity.User;
import com.azerpost.app.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final String SESSION_ID_CLAIM = "sessionId";

    private final SecretKey secretKey;
    private final SessionRepository sessionRepository;

    public JwtService(@Value("${issue.key}") String secret, SessionRepository sessionRepository) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.sessionRepository = sessionRepository;
    }

    public String generateAccessToken(UserDetails user, Long sessionId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SESSION_ID_CLAIM, sessionId);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(UserDetails user, Long sessionId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SESSION_ID_CLAIM, sessionId);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractSessionId(String token) {
        Number value = extractAllClaims(token).get(SESSION_ID_CLAIM, Number.class);
        return value == null ? null : value.longValue();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final Long sessionId = extractSessionId(token);

        return username.equals(userDetails.getUsername())
                && sessionId != null
                && belongsToActiveSession(sessionId, userDetails)
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean belongsToActiveSession(Long sessionId, UserDetails userDetails) {
        if (userDetails instanceof User user) {
            return sessionRepository.existsByIdAndUserId(sessionId, user.getId());
        }

        return false;
    }
}
