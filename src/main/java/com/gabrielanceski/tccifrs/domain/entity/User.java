package com.gabrielanceski.tccifrs.domain.entity;

import com.gabrielanceski.tccifrs.domain.Role;
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

    private String name;
    private String email;
    private String password;
    private Boolean active;
    private Boolean blocked;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(mappedBy = "members")
    private Set<Team> teams;
}
