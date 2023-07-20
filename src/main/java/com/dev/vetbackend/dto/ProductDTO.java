package com.dev.vetbackend.dto;

import com.dev.vetbackend.constants.ProductType;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private ProductType productType;
    private String name;
    private String manufacturer;
    private String targetSpecies;
    private int quantity;
    private Long price;
    private Long cost;
    private String type; // 'vaccine', 'vermifuge', etc.
}
