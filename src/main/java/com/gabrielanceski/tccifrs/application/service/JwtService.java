package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.presentation.domain.response.AuthenticationResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtService {
    private static final long EXPIRATION_TIME = 3600;
    private static final String ISSUER = "tccifrs";
    private final JwtEncoder jwtEncoder;

    public AuthenticationResponse generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(EXPIRATION_TIME, ChronoUnit.SECONDS))
                .subject(authentication.getName())
                .claim("role", roles)
                .build();

        Jwt token = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        return new AuthenticationResponse(
                token.getTokenValue(),
                Objects.requireNonNull(token.getExpiresAt()).toEpochMilli()
        );
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> jwt.getClaimAsStringList("role")
                .stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
        );
        return converter;
    }
}
