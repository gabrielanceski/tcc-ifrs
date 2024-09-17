package com.gabrielanceski.tccifrs.exception;

public class UserNotFoundException extends InvalidDataException {
    public UserNotFoundException(String userId) {
        super("User id <" + userId + "> not found");
    }

    public UserNotFoundException() {
        super("User not found");
    }
}
