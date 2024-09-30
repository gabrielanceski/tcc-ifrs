package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabrielanceski.tccifrs.domain.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String document;
    private String email;
    private Boolean active;
    private Boolean blocked;
    private String role;
    @JsonProperty("company_id") @JsonInclude private String companyId;
    private Set<TeamResponse> teams;

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
