package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserCreateRequest(
    @NotNull @NotEmpty String name,
    @NotNull @NotEmpty String document,
    @NotNull @NotEmpty String password,
    @NotNull @NotEmpty String role
) {
}
