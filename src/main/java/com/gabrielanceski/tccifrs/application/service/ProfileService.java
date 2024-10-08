package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.entity.User;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.presentation.domain.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfileService {

    public UserResponse getUserProfile(AuthenticatedUser user) {
        if (user == null || user.getEntity() == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        User entity = user.getEntity();
        return UserResponse.fromEntity(entity);
    }

}
