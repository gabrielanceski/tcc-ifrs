package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.RequirementService;
import com.gabrielanceski.tccifrs.domain.CurrentUser;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.presentation.domain.request.RequirementCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.RequirementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requirement")
@RequiredArgsConstructor
public class RequirementController {
    private final RequirementService requirementService;

    @PostMapping
    public ResponseEntity<RequirementResponse> createRequirement(@RequestBody @Valid RequirementCreateRequest request, @CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(requirementService.createRequirement(request, authenticatedUser));
    }

    @GetMapping("/{requirementId}")
    public ResponseEntity<RequirementResponse> getRequirement(@PathVariable String requirementId) {
        return ResponseEntity.ok(requirementService.getDetails(requirementId));
    }

}
