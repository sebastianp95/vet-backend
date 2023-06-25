package com.dev.vetbackend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class PetVaccinationId implements Serializable {
    private Pet pet;
    private String vaccinationId;
}
