package com.dev.vetbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Vaccination extends Product {

    @Pattern(regexp = "(vaccine|vermifuge)")
    private String type; // 'vaccine', 'vermifuge', etc.
}
