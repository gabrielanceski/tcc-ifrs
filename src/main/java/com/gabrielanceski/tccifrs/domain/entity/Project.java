package com.gabrielanceski.tccifrs.domain.entity;

import com.gabrielanceski.tccifrs.domain.Auditable;
import com.gabrielanceski.tccifrs.domain.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

/**
 * Representa um projeto.
 * Projetos podem ter diferentes escopos, desde consultoria até desenvolvimento de software.
 * Projetos são temporários, possuem início e fim definidos, e são executados para atingir uma série de requisitos especificados.
 *
 * @see Requirement
 */
@Entity
@Table(name = "projects")
@Getter
@Setter
@ToString(exclude = {"teams", "requirements"})
public class Project extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String description;

    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private User projectManager; // PO kind of

    @ManyToOne
    private Company company;

    @ManyToMany
    @JoinTable(
        name = "project_teams",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teams;

    @OneToMany(mappedBy = "project")
    private Set<Requirement> requirements;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) && Objects.equals(name, project.name) && status == project.status && Objects.equals(description, project.description) && Objects.equals(company, project.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description, company);
    }
}
