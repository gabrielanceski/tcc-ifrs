package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PasswordResetResponse(
    String name,
    String document,
    @JsonProperty("temporary_password") String temporaryPassword
) {
}
