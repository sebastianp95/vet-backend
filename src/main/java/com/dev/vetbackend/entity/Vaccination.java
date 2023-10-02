package com.dev.vetbackend.entity;

import com.dev.vetbackend.constants.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Vaccination extends Product {


    @Pattern(regexp = "(vaccine|vermifuge)")
    private String type; // 'vaccine', 'vermifuge', etc.

    public Vaccination(String name, Long price, Long cost, String manufacturer, int quantity, String targetSpecies, String type, String imageSrc) {
        super(name, price, cost, manufacturer, quantity, targetSpecies, imageSrc);
        this.type = type;
        this.setProductType(ProductType.VACCINATION);
    }


}

