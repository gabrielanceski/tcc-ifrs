package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private String status;
    @JsonProperty("requirement_id") private String requirementId;
    @JsonProperty("team_id") private String teamId;
    @JsonProperty("blocked_by_id") private String blockedById;
}
