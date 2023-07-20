package com.dev.vetbackend.services;

import com.dev.vetbackend.constants.ProductType;
import com.dev.vetbackend.dto.ProductDTO;
import com.dev.vetbackend.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<Product> findAllByUserAndProductType(Pageable pageable, Long id, String name, String manufacturer, String targetSpecies, ProductType productType, String vaccinationType);

    Product save(Product newProduct);
    Product update(Long id, Product updatedProduct);
    Product getProductById(Long id);

    boolean deleteById(Long id);

}
