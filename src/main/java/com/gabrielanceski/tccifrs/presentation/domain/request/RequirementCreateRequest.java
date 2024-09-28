package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RequirementCreateRequest(
    @NotNull @NotEmpty String name,
    @NotNull @NotEmpty String description,
    @NotNull @NotEmpty String goals,
    @NotNull @NotEmpty String specs,
    @NotNull @NotEmpty String collaboration,
    @NotNull @NotEmpty String innovation,
    @NotNull @NotEmpty String restrictions,
    @NotNull @NotEmpty @JsonProperty("importance_level") String importanceLevel,
    @NotNull @NotEmpty @JsonProperty("effort_level") String effortLevel,
    @NotNull @NotEmpty @JsonProperty("project_id") String projectId
) {
}
