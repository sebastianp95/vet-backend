package com.dev.vetbackend.entity;

import com.dev.vetbackend.constants.Reason;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
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
    @Enumerated(EnumType.STRING)
    private Reason reason;
    @Column(name = "pet_owner_name")
    private String petOwnerName;
    private String phoneNumber;
    private String email;
    @Transient
    private String message;
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
}
