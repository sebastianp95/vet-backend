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
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;
    private String ownerId;
    @Enumerated(EnumType.STRING)
    private Species species;
    @Pattern(regexp = "(M|F)")
    private String gender;
    private String furColor;    
    private String microchip;
    private String breed;
    private int age;
    private int weight;
    private String imageSrc;
    @Enumerated(EnumType.STRING)
    private ReproductiveStatus reproductiveStatus;
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PetVaccination> petVaccinations;
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MedicalHistory> medicalHistories;
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Exam> exams;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
}


