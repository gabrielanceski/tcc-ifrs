package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.UserService;
import com.gabrielanceski.tccifrs.presentation.domain.request.UserCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.UserDetailsRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.UserUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.PasswordResetResponse;
import com.gabrielanceski.tccifrs.presentation.domain.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public record UserController(UserService userService) {

    @PostMapping("/details")
    public ResponseEntity<UserResponse> getDetails(@RequestBody @Valid UserDetailsRequest request) {
        return ResponseEntity.ok(userService.getUserDetails(request));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.patchUser(id, request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody @Valid UserDetailsRequest request) {
        return ResponseEntity.ok(userService.resetPassword(request));
    }

    @PostMapping("/company/associate/{userId}/{companyId}")
    public ResponseEntity<UserResponse> associateUserToCompany(@PathVariable String userId, @PathVariable String companyId) {
        return ResponseEntity.ok(userService.associateUserToCompany(userId, companyId));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

}
