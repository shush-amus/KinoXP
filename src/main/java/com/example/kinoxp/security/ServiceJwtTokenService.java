package com.example.kinoxp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class ServiceJwtTokenService {

    private final SecretKey signingKey;

    public ServiceJwtTokenService(@Value("${pricing.service.jwt-secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createServiceToken() {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject("kinoxp-main-app")
                .issuer("kinoxp-main-app")
                .audience().add("pricing-service").and()
                .claim("scope", "pricing:calculate")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(5, ChronoUnit.MINUTES)))
                .signWith(signingKey)
                .compact();
    }
}
