package com.dev.vetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PetVaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccination_id")
    private Vaccination vaccination;
    @Column(name = "vaccination_name")
    private String vaccinationName;
    @Column(name = "vaccination_type")  // 'vaccine' or 'vermifuge'
    private String vaccinationType;
    @Column(name = "date")
    private LocalDate date;
}
