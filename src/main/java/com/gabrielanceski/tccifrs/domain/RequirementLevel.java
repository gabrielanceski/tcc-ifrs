package com.gabrielanceski.tccifrs.domain;

public enum RequirementLevel {
    HIGH,
    MEDIUM,
    LOW;

    public static RequirementLevel fromString(String level) {
        try {
            return RequirementLevel.valueOf(level.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid requirement level: " + level);
        }
    }
}
