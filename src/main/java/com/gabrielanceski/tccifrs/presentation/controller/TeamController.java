package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.TeamService;
import com.gabrielanceski.tccifrs.presentation.domain.request.TeamCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.TeamResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public record TeamController(TeamService teamService) {

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamCreateRequest request) {
        return ResponseEntity.ok(teamService.createTeam(request));
    }

}
