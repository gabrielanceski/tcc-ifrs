package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CompanyRequest(
    @NotNull String name,
    @NotNull String document,
    @Nullable String address,
    @Nullable String contacts
) {
}
