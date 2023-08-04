package com.dev.vetbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


import java.util.List;

import com.dev.vetbackend.constants.ReproductiveStatus;
import com.dev.vetbackend.constants.Species;

@Entity
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Species species;
    @Pattern(regexp = "(M|F)")
    private String gender;
    private String ownerName;
    private String ownerEmail;
    private Long ownerId;
    private String breed;
    private int age;
    private int weight;
    private String imageSrc;
    @Enumerated(EnumType.STRING)
    private ReproductiveStatus reproductiveStatus;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pet")
    @JsonIgnore
    private List<PetVaccination> petVaccinations;
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MedicalHistory> medicalHistories;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
}


