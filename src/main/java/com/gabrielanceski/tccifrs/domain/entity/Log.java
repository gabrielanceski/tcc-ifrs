package com.gabrielanceski.tccifrs.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Getter
@Setter
@ToString
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "caused_by")
    private User causedBy;

    // Essa coluna pode ser nula, mas vai servir para exibir os logs para a equipe vinculada a umn projeto, nao precisa ser obrigatorio
    @ManyToOne
    private Project relatedProject;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
