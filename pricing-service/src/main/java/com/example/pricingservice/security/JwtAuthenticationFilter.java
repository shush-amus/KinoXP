package com.example.pricingservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ServiceJwtTokenValidator serviceJwtTokenValidator;

    public JwtAuthenticationFilter(ServiceJwtTokenValidator serviceJwtTokenValidator) {
        this.serviceJwtTokenValidator = serviceJwtTokenValidator;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/actuator/health".equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Bearer token.");
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = serviceJwtTokenValidator.validate(token);
            String audience = claims.getAudience().stream().findFirst().orElse("");
            String scope = claims.get("scope", String.class);

            if (!"pricing-service".equals(audience) || !"pricing:calculate".equals(scope)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token does not grant access to pricing service.");
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    List.of(new SimpleGrantedAuthority("SCOPE_" + scope))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token.");
        }
    }
}
