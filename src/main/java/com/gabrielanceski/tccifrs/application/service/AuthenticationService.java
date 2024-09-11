package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.presentation.domain.request.AuthenticationRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public record AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("authenticate() - trying to authenticate user {}", request.document());
        log.debug("authenticate() - request <{}>", request);

        try {
            Authentication authentication = authenticationManager.authenticate(request.toAuthentication());

            log.info("authenticate() - user {} authenticated", request.document());

            return jwtService.generateToken(authentication);

        } catch (Exception e) {
            log.error("authenticate() - error while authenticating user {} - Exception <{}>", request.document(), e.getMessage());
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

}
