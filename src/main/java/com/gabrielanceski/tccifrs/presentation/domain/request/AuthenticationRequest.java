package com.gabrielanceski.tccifrs.presentation.domain.request;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public record AuthenticationRequest(
        String document,
        String password
) {

    public Authentication toAuthentication() {
        return new UsernamePasswordAuthenticationToken(document, password);
    }
}

