package com.gabrielanceski.tccifrs.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

// Cada empresa pode ter N usuários vinculados
@Entity
@Table(
    name = "companies",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"document"})
    }
)
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String document;
    private String name;

    @Column(nullable = false)
    private String address;

    private String contacts;

    @OneToMany
    @JoinColumn(name = "company_id")
    private List<User> users;
}
