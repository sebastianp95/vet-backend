package com.dev.vetbackend.entity;

import com.dev.vetbackend.constants.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Vaccination extends Product {

    public Vaccination() {
        this.setProductType(ProductType.VACCINATION);
    }

    @Pattern(regexp = "(vaccine|vermifuge)")
    private String type; // 'vaccine', 'vermifuge', etc.

}

