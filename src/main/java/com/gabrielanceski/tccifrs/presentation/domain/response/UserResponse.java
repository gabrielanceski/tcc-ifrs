package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabrielanceski.tccifrs.domain.entity.Team;
import com.gabrielanceski.tccifrs.domain.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
    String id,
    String name,
    String document,
    String email,
    Boolean active,
    Boolean blocked,
    String role,
    @JsonProperty("company_id") @JsonInclude String companyId,
    Set<TeamResponse> teams
) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getDocument(),
            user.getEmail(),
            user.getActive(),
            user.getBlocked(),
            user.getRole().name(),
            user.getCompany() == null ? null : user.getCompany().getId(),
            user.getTeams().stream().map(TeamResponse::fromEntity).collect(Collectors.toSet())
        );
    }

    public static UserResponse fromEntity(User user, Set<Team> teams) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getDocument(),
                user.getEmail(),
                user.getActive(),
                user.getBlocked(),
                user.getRole().name(),
                user.getCompany() == null ? null : user.getCompany().getId(),
                teams.stream().map(TeamResponse::fromEntity).collect(Collectors.toSet())
        );
    }

    public static UserResponse simpleFromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            null,
            user.getEmail(),
            null,
            null,
            null,
            null,
            user.getTeams().stream().map(TeamResponse::fromEntity).collect(Collectors.toSet())
        );
    }
}
