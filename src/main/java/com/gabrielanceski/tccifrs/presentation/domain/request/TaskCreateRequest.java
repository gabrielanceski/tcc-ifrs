package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TaskCreateRequest(
    @NotNull @NotEmpty String title,
    @NotNull @NotEmpty String description,
    @NotNull @NotEmpty @JsonProperty("requirement_id") String requirementId
) {
}
