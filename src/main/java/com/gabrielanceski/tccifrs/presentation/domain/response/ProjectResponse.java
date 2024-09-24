package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gabrielanceski.tccifrs.domain.entity.Project;

import java.util.Set;
import java.util.stream.Collectors;

@JsonPropertyOrder({"id", "name", "description", "status", "company", "teams", "created_at", "updated_at"})
public record ProjectResponse(
    String id,
    String name,
    String description,
    String status,
    CompanyResponse company,
    Set<TeamResponse> teams,
    @JsonProperty("created_at") String createdAt,
    @JsonProperty("updated_at") String updatedAt
) {
    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStatus().name(),
            CompanyResponse.fromEntity(project.getCompany()),
            project.getTeams().stream().map(TeamResponse::fromEntity).collect(Collectors.toSet()),
            project.getCreatedAt().toString(),
            project.getUpdatedAt().toString()
        );
    }
}
