package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.ProfileService;
import com.gabrielanceski.tccifrs.domain.CurrentUser;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.presentation.domain.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public UserResponse getUserProfile(@CurrentUser AuthenticatedUser user) {
        return profileService.getUserProfile(user);
    }

}
