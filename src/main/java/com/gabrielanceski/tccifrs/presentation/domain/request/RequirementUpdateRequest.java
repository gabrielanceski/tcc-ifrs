package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RequirementUpdateRequest {
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
}
