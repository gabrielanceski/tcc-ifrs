package com.gabrielanceski.tccifrs.exception;

public class UnknownRoleException extends InvalidDataException {
    public UnknownRoleException(String role) {
        super("Unknown role name <" + role + ">");
    }
}
