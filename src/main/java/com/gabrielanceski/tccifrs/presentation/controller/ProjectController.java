package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.ProjectService;
import com.gabrielanceski.tccifrs.presentation.domain.request.ProjectCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.ProjectUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.ProjectResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectCreateRequest request) {
        return ResponseEntity.ok(projectService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listAll() {
        return ResponseEntity.ok(projectService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable String id) {
        return ResponseEntity.ok(projectService.getDetails(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable String id, @RequestBody @Valid ProjectUpdateRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }
}
