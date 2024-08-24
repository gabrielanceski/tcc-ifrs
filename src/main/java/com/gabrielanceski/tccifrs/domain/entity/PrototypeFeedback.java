package com.gabrielanceski.tccifrs.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "prototype_feedbacks")
@Data
public class PrototypeFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "prototype_id", nullable = false)
    private Prototype prototype;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
