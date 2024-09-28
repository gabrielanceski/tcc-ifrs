package com.gabrielanceski.tccifrs.presentation.domain.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserDetailsRequest(
    @NotNull @NotEmpty String document
) {
}
