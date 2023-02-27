package com.dev.vetbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@IdClass(PetVaccineId.class)
@Table(name = "pet_vaccine")
public class PetVaccine {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Id
    @Column(name = "vaccine_id")
    private String vaccineId;

    @Column(name = "vaccine_name")
    private String vaccineName;
    @Column(name = "date")
    private LocalDate date;

}
