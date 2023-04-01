package com.dev.vetbackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class PetVermifugeId implements Serializable {
    private Pet pet;
    private String vermifugeId;

    // Equals and hashCode implementations.
}
