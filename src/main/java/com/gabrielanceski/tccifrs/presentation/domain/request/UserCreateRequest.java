package com.gabrielanceski.tccifrs.presentation.domain.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
    @NotNull @NotEmpty String name,
    @NotNull @NotEmpty String document,
    @NotNull @NotEmpty String password,
    @NotNull @NotEmpty String role
) {
}
