package com.gabrielanceski.tccifrs.domain.entity;

import com.gabrielanceski.tccifrs.domain.Auditable;
import com.gabrielanceski.tccifrs.domain.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Tarefas de um projeto.
 * As tarefas podem ser criadas e editadas apenas por líderes de equipe, project managers, ou usuários com papel de ADMIN ou superior.
 * Uma tarefa pode ser editada por líderes de outras equipes, contato que estejam vinculadas ao mesmo projeto.
 * Uma tarefa precisa estar vinculada a um requisito.
 * Quando uma tarefa é criada, ela não possuirá equipe vinculada.
 * A equipe vinculada a uma tarefa é opcional.
 * Uma tarefa pode ser bloqueada por outra tarefa.
 *
 * @see Requirement
 * @see Team
 */
@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
public class Task extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "requirement_id", nullable = false)
    private Requirement requirement;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne
    @JoinColumn(name = "blocked_by_id")
    private Task blockedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status && Objects.equals(requirement, task.requirement) && Objects.equals(blockedBy, task.blockedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, requirement, blockedBy);
    }
}
