package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabrielanceski.tccifrs.domain.entity.Project;
import com.gabrielanceski.tccifrs.domain.entity.Team;
import com.gabrielanceski.tccifrs.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TeamResponse(
    String id,
    String name,
    @JsonProperty("leader_id") String leaderId,
    Set<String> members,
    Set<String> projects
) {
  public static TeamResponse  fromEntity(Team team) {
    return new TeamResponse(
        team.getId(),
        team.getName(),
        team.getLeader().getId(),
        team.getMembers().stream().map(User::getId).collect(Collectors.toSet()),
        team.getProjects().stream().map(Project::getId).collect(Collectors.toSet())
    );
  }
}
