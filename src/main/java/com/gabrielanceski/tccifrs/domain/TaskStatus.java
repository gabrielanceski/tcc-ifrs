package com.gabrielanceski.tccifrs.domain;

public enum TaskStatus {
    CREATED,
    TODO,
    IN_PROGRESS,
    DONE,
    CANCELED;

    public static TaskStatus fromString(String status) {
        try {
            return TaskStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid task status: " + status);
        }
    }
}
