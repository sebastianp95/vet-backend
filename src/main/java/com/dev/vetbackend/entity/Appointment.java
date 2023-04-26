package com.dev.vetbackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pet_name", nullable = false)
    private String petName;

    private String reason;

    private String phoneNumber;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
}