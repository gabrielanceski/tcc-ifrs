package com.gabrielanceski.tccifrs.presentation.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateRequest {
    private String name;
    private String description;
    private String status;
    @JsonProperty("project_manager_id") private String projectManagerId;
    @JsonProperty("company_id") private String companyId;
    private Set<String> teams;
}
