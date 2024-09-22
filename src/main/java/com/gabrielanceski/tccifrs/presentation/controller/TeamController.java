package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.TeamService;
import com.gabrielanceski.tccifrs.presentation.domain.request.TeamCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.TeamUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.UserDetailsRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.TeamResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public record TeamController(TeamService teamService) {

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamCreateRequest request) {
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @PostMapping("/associate/{userId}/{teamId}")
    public ResponseEntity<TeamResponse> associateUserToTeam(@PathVariable @NotNull @NotEmpty String userId, @PathVariable @NotNull @NotEmpty String teamId) {
        return ResponseEntity.ok(teamService.associateUserToTeam(userId, teamId));
    }

    @PostMapping("/promote/{userId}/{teamId}")
    public ResponseEntity<TeamResponse> promoteUserAsLeader(@PathVariable @NotNull @NotEmpty String userId, @PathVariable @NotNull @NotEmpty String teamId) {
        return ResponseEntity.ok(teamService.promoteUserAsLeader(userId, teamId));
    }

    @PostMapping("/remove-member/{userId}/{teamId}")
    public ResponseEntity<TeamResponse> removeMemberFromTeam(@PathVariable @NotNull @NotEmpty String userId, @PathVariable @NotNull @NotEmpty String teamId) {
        return ResponseEntity.ok(teamService.removeMemberFromTeam(userId, teamId));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeamDetails(@PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getTeamDetails(teamId));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getTeams() {
        return ResponseEntity.ok(teamService.listTeams());
    }

    @PutMapping
    public ResponseEntity<TeamResponse> updateTeam(@RequestBody @Valid TeamUpdateRequest request) {
        return ResponseEntity.ok(teamService.updateTeam(request));
    }
}
