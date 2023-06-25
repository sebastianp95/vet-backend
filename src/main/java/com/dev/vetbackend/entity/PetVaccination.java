package com.dev.vetbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@IdClass(PetVaccinationId.class)
@Table(name = "pet_vaccination")
public class PetVaccination {

    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Id
    @Column(name = "vaccination_id")
    private String vaccinationId;

    @Column(name = "vaccination_name")
    private String vaccinationName;

    @Column(name = "vaccination_type")  // 'vaccine' or 'vermifuge'
    private String vaccinationType;

    @Column(name = "date")
    private LocalDate date;
}
