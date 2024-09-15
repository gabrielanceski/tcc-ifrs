package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserResponse(
    String id,
    String name,
    String document,
    boolean active,
    boolean blocked,
    String role,
    @JsonProperty("company_id") String companyId,
    List<?> teams
) {
}
