package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TeamUpdateRequest(
    @NotNull @NotEmpty String id,
    @NotNull @NotEmpty String name,
    @JsonProperty("leader_id") @NotNull @NotEmpty String leaderId,
    @NotNull Set<String> members,
    @NotNull Set<String> projects
) {
}
