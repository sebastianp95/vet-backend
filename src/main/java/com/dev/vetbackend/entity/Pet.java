package com.dev.vetbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Data;


import java.util.List;

@Entity
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long ownerId;
    private String breed;
    private String age;
    private String doB;
    private int weight;
    private String imageSrc;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pet")
    @JsonIgnore
    private List<PetVaccine> petVaccines;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
}


