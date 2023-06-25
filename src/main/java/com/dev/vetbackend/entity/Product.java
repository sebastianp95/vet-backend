package com.dev.vetbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // using joined strategy for inheritance
@Data
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String manufacturer;
    private int quantity;
    private Long price;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
}
