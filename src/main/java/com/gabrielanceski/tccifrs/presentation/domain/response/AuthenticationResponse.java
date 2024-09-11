package com.gabrielanceski.tccifrs.presentation.domain.response;

public record AuthenticationResponse(
        String token,
        Long expiresAt
) {
}
