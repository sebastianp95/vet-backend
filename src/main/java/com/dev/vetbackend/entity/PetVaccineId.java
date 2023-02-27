package com.dev.vetbackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class PetVaccineId implements Serializable {
    private Pet pet;
    private String vaccineId;

    // Equals and hashCode implementations.
}
