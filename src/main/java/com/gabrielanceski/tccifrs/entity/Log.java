package com.gabrielanceski.tccifrs.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
