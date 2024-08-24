package com.gabrielanceski.tccifrs.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(
    name = "teams",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
    }
)
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(
        name = "leader_id",
        nullable = false
    )
    private User leader;

    @ManyToMany
    @JoinTable(
        name = "team_members",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    @ManyToMany(mappedBy = "teams")
    private Set<Project> projects;

}
