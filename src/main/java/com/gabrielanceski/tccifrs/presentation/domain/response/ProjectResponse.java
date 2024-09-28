package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gabrielanceski.tccifrs.domain.entity.Project;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
public class ProjectResponse {
    private String id;
    private String name;
    private String description;
    private String status;
    @JsonProperty("project_manager") private UserResponse projectManager;
    private CompanyResponse company;
    private Set<TeamResponse> teams;
    @JsonProperty("created_at") private String createdAt;
    @JsonProperty("updated_at") private String updatedAt;

    public static ProjectResponse fromEntity(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus().name())
                .projectManager(UserResponse.simpleFromEntity(project.getProjectManager()))
                .company(CompanyResponse.fromEntity(project.getCompany()))
                .teams(project.getTeams().stream()
                        .map(TeamResponse::fromEntity)
                        .collect(Collectors.toSet()))
                .createdAt(project.getCreatedAt().toString())
                .updatedAt(project.getUpdatedAt().toString())
                .build();
    }

}
