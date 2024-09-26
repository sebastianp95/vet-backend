package com.dev.vetbackend.entity;

import com.dev.vetbackend.utils.StringListToJsonConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"pet"})
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateOfVisit;
    private String conductedBy;
    @Column(length = 500)
    private String reasonForVisit;
    @Column(length = 500)
    private String anamnesis;
    private int weight;
    private int age;
    private double temperature;
    private int heartRate;
    @Column(length = 500)
    private String generalCondition;
    @Column(length = 500)
    private String skinAndCoat;
    @Column(length = 500)
    private String respiratorySystem;
    @Column(length = 500)
    private String cardiovascularSystem;
    @Column(length = 500)
    private String musculoskeletalSystem;
    @Column(length = 500)
    private String genitourinary;
    @Column(length = 500)
    private String veterinarianObservations;
    @Column(length = 500)
    private String treatmentPlanAndRecommendations;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;
}

