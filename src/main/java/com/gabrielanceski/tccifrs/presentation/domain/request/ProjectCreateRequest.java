package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProjectCreateRequest(
    @NotNull @NotEmpty String name,
    @NotNull @NotEmpty String description,
    @NotNull @NotEmpty @JsonProperty("project_manager_id") String projectManagerId,
    @NotNull @NotEmpty @JsonProperty("company_id") String companyId
) {
}
