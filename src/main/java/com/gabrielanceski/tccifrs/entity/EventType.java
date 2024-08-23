package com.gabrielanceski.tccifrs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "event_types")
@Data
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}
