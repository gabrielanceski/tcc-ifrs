package com.gabrielanceski.tccifrs.domain;

import com.gabrielanceski.tccifrs.exception.UnknownRoleException;

public enum Role {
    MASTER,
    ADMIN,
    COMPANY,
    STUDENT,
    PROFESSOR,
    GUEST;

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownRoleException(role);
        }
    }
}
