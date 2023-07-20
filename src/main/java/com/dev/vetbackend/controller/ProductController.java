package com.dev.vetbackend.controller;

import com.dev.vetbackend.constants.ProductType;
import com.dev.vetbackend.entity.Product;
import com.dev.vetbackend.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "30") Integer resultsPerPage,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String targetSpecies,
            @RequestParam(required = false) ProductType productType,
            @RequestParam(required = false) String vaccinationType
    ) {

        Pageable pageable = PageRequest.of(page, resultsPerPage, Sort.by(sortBy));
        List<Product> products = productService.findAllByUserAndProductType(pageable, id, name, manufacturer, targetSpecies, productType, vaccinationType);
        return ResponseEntity.ok(products);
    }



    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
        Product product = productService.save(newProduct);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product updatedProduct, @PathVariable Long id) {
        try {
            Product updated = productService.update(id, updatedProduct);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product-types")
    public ResponseEntity<List<String>> getProductTypes() {
        List<String> productTypes = Arrays.stream(ProductType.values())
                .map(ProductType::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productTypes);
    }

}
