package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.ProjectService;
import com.gabrielanceski.tccifrs.application.service.RequirementService;
import com.gabrielanceski.tccifrs.application.service.TaskService;
import com.gabrielanceski.tccifrs.domain.CurrentUser;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.presentation.domain.request.*;
import com.gabrielanceski.tccifrs.presentation.domain.response.ProjectResponse;
import com.gabrielanceski.tccifrs.presentation.domain.response.RequirementResponse;
import com.gabrielanceski.tccifrs.presentation.domain.response.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/project-management")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final RequirementService requirementService;
    private final TaskService taskService;

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

    @GetMapping("/projects/{projectId}/requirements")
    public ResponseEntity<Set<RequirementResponse>> getProjectRequirements(@PathVariable String projectId) {
        return ResponseEntity.ok(requirementService.listAll(projectId));
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskResponse>> listAllTasksByProjectId(@PathVariable String projectId) {
        return ResponseEntity.ok(taskService.listAllByProjectId(projectId));
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

    @GetMapping("/requirements/{requirementId}/tasks")
    public ResponseEntity<List<TaskResponse>> listAllTasksByRequirementId(@PathVariable String requirementId) {
        return ResponseEntity.ok(taskService.listAllByRequirementId(requirementId));
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskCreateRequest request, @CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(taskService.createTask(request, authenticatedUser));
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String taskId, @RequestBody @Valid TaskUpdateRequest request, @CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request, authenticatedUser));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> cancelTask(@PathVariable String taskId, @CurrentUser AuthenticatedUser authenticatedUser) {
        taskService.cancelTask(taskId, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTaskDetails(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.getDetails(taskId));
    }
}
