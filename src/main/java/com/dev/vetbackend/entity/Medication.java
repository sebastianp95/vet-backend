package com.dev.vetbackend.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Medication extends Product {
    // Medication specific attributes go here.
}