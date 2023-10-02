package com.dev.vetbackend.entity;

import com.dev.vetbackend.utils.StringListToJsonConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Column(length = 800)
    private String reasonForVisit;
    @Convert(converter = StringListToJsonConverter.class)
    private List<String> currentMedications;

    @Convert(converter = StringListToJsonConverter.class)
    private List<String> knownAllergies;

    @Convert(converter = StringListToJsonConverter.class)
    private List<String> previousSurgeriesOrIllness;
    @Column(length = 800)
    private String veterinarianObservations;
    @Column(length = 800)
    private String treatmentPlanAndRecommendations;
    private int weight;
    private int age;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

}

