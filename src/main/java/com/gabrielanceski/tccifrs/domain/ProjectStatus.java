package com.gabrielanceski.tccifrs.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum ProjectStatus {
    CREATED,
    IN_PROGRESS,
    FINISHED,
    CANCELED;

    public static ProjectStatus fromString(String status) {
        try {
            return ProjectStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid project status");
        }
    }
}
