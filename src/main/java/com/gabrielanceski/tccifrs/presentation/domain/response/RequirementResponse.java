package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabrielanceski.tccifrs.domain.entity.Requirement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
@Builder
public class RequirementResponse {
    private String id;
    private String name;
    private String description;
    private String goals;
    private String specs;
    private String collaboration;
    private String innovation;
    private String restrictions;
    @JsonProperty("importance_level") private String importanceLevel;
    @JsonProperty("effort_level") private String effortLevel;
    @JsonProperty("project_id") private String projectId;
    @JsonProperty("reporter") private UserResponse reporter;

    public static RequirementResponse fromEntity(Requirement requirement) {
        return RequirementResponse.builder()
                .id(requirement.getId())
                .name(requirement.getName())
                .description(requirement.getDescription())
                .goals(requirement.getGoals())
                .specs(requirement.getSpecs())
                .collaboration(requirement.getCollaboration())
                .innovation(requirement.getInnovation())
                .restrictions(requirement.getRestrictions())
                .importanceLevel(requirement.getImportanceLevel().name())
                .effortLevel(requirement.getEffortLevel().name())
                .projectId(requirement.getProject().getId())
                .reporter(UserResponse.simpleFromEntity(requirement.getReporter()))
                .build();
    }
}
