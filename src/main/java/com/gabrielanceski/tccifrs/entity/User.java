package com.gabrielanceski.tccifrs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"document"}),
        @UniqueConstraint(columnNames = {"email"})
    }
)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String document;

    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(mappedBy = "members")
    private Set<Team> teams;
}
