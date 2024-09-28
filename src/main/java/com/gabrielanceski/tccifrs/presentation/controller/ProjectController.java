package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.ProjectService;
import com.gabrielanceski.tccifrs.application.service.RequirementService;
import com.gabrielanceski.tccifrs.domain.CurrentUser;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.presentation.domain.request.ProjectCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.ProjectUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.RequirementCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.RequirementUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.ProjectResponse;
import com.gabrielanceski.tccifrs.presentation.domain.response.RequirementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-management")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final RequirementService requirementService;

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectCreateRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponse>> listAll() {
        return ResponseEntity.ok(projectService.listAll());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable String id) {
        return ResponseEntity.ok(projectService.getDetails(id));
    }

    @PatchMapping("/projects/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable String id, @RequestBody @Valid ProjectUpdateRequest request, @CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(projectService.updateProject(id, request, authenticatedUser));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.cancelProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/requirements")
    public ResponseEntity<RequirementResponse> createRequirement(@RequestBody @Valid RequirementCreateRequest request, @CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(requirementService.createRequirement(request, authenticatedUser));
    }

    @PatchMapping("/requirements/{requirementId}")
    public ResponseEntity<RequirementResponse> updateRequirement(@PathVariable String requirementId, @RequestBody @Valid RequirementUpdateRequest request, @CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(requirementService.updateRequirement(requirementId, request, authenticatedUser));
    }

    @GetMapping("/requirements/{requirementId}")
    public ResponseEntity<RequirementResponse> getRequirementDetails(@PathVariable String requirementId) {
        return ResponseEntity.ok(requirementService.getDetails(requirementId));
    }

}
