package com.gabrielanceski.tccifrs.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(
    name = "user_role",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role"})
    }
)
@Data
public class UserRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;
//    ADMIN,
//    COMPANY,
//    STUDENT,
//    PROFESSOR,
//    GUEST
    @Override
    public String toString() {
        return role;
    }
}
