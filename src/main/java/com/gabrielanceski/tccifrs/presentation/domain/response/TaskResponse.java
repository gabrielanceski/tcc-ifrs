package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabrielanceski.tccifrs.domain.entity.Task;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private String status;
    @JsonProperty("requirement_id") private String requirementId;
    @JsonProperty("team_id") private String teamId;
    @JsonProperty("blocked_by_id") private String blockedById;
    @JsonProperty("created_at") private LocalDateTime createdAt;
    @JsonProperty("updated_at") private LocalDateTime updatedAt;

    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .requirementId(task.getRequirement().getId())
                .teamId(task.getTeam() != null ? task.getTeam().getId() : null)
                .blockedById(task.getBlockedBy() != null ? task.getBlockedBy().getId() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
