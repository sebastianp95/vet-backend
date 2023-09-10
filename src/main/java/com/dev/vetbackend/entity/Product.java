package com.dev.vetbackend.entity;

import com.dev.vetbackend.constants.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "productType", // the name of the field that contains the type info
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Vaccination.class, name = "VACCINATION"),
        @JsonSubTypes.Type(value = Food.class, name = "FOOD"),
        @JsonSubTypes.Type(value = Medication.class, name = "MEDICATION"),
        @JsonSubTypes.Type(value = PetCareItem.class, name = "PETCAREITEM"),
        @JsonSubTypes.Type(value = Service.class, name = "SERVICE")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // using joined strategy for inheritance
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    private String name;
    private String manufacturer;
    private String targetSpecies;
    private int quantity;
    private Long price;
    private Long cost;
    private String imageSrc;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;

    public Product(String name, Long price, Long cost, String manufacturer, int quantity, String targetSpecies, String imageSrc) {
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.targetSpecies = targetSpecies;
        this.imageSrc = imageSrc;
    }

}
