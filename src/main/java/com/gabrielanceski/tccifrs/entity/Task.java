package com.gabrielanceski.tccifrs.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private String status;

    @ManyToOne
    @JoinColumn(name = "requirement_id", nullable = false)
    private Requirement requirement;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
