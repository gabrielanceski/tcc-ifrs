package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(
        String token,
        @JsonProperty("expires_at") Long expiresAt
) {
}
