package com.dev.vetbackend.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class PetCareItem extends Product {
    // PetCareItem specific attributes go here.
}